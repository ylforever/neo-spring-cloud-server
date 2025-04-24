package com.neo.springcloud.service;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.neo.springcloud.util.ServerMetricUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/**
 * 服务健康指标采集，上传管理
 *
 * @author neo
 * @since 2025/4/21
 * @version 1.0
 */
@Component
public class ServiceHealthMetric {
    private static final Logger LOGGER = LogManager.getLogger(ServiceHealthMetric.class);

    @Resource
    private NacosServiceManager nacosServiceManager;

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    /**
     * 服务端口
     */
    @Value("${server.port:-1}")
    private int port;

    /**
     * 服务名
     */
    @Value("${spring.application.name:}")
    private String serverName;

    /** 
     * 搜集服务器指标数据。每10秒采集一次,刷新nacos服务器的数据
     *
     * @author neo
     * @since 2025/4/21
     */ 
    @Scheduled(cron = "0/10 * * * * ? ")
    public void collectMetrics(){
        LOGGER.info("Collect metrics begin.");

        try {
            Properties properties = nacosDiscoveryProperties.getNacosProperties();
            NamingService namingService = nacosServiceManager.getNamingService(properties);

            // 获取服务的所有实例
            List<Instance> instancesList = namingService.getAllInstances(serverName);

            // 获取当前实例
            String hostIp = ServerMetricUtil.getHostIp();
            Instance curInstance = null;
            for (Instance instance : instancesList) {
                if (instance.getIp().equals(hostIp) && instance.getPort() == port) {
                    curInstance = instance;
                    break;
                }
            }

            // 更新自定义指标
            curInstance.addMetadata("cpu.usage", String.valueOf(ServerMetricUtil.getCPUUsage()));
            curInstance.addMetadata("memory.usage", String.valueOf(ServerMetricUtil.getMemoryUsage()));

            // 也可以更新权重(这里随便指定一个值，看修改的效果。实现应用根据自身项目的业务规则处理)
            curInstance.setWeight(98);

            // 更新实例
            NamingMaintainService namingMaintainService = nacosServiceManager.getNamingMaintainService(properties);
            namingMaintainService.updateInstance(curInstance.getServiceName(), curInstance);

        } catch (Exception e) {
            LOGGER.error("Collect metrics fail.", e);
        }

        LOGGER.info("Collect metrics end.");
    }
}
