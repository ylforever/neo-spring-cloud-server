package com.neo.springcloud.rest;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceInstance;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingMaintainService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;

/**
 * 课程管理服务
 *
 * @author neo
 * @since 2025-02-12
 */
@RestController
@RequestMapping("/v1/lesson")
public class LessonController {
    private static final Logger LOGGER = LogManager.getLogger(LessonController.class);

    @Resource
    private DiscoveryClient discoveryClient;

    @Resource
    private NacosServiceManager nacosServiceManager;

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    /**
     * 插入课程数据
     *
     * @return 分配的课程编码
     */
    @PostMapping("/insert-lesson")
    public String insertLesson(@RequestBody String str) {
        List<ServiceInstance> instances= discoveryClient.getInstances("cloud-server-one");
        ServiceInstance instance = instances.get(0);

        Properties properties = nacosDiscoveryProperties.getNacosProperties();
        NamingService namingService = nacosServiceManager.getNamingService(properties);

        try {
            List<Instance>  instancesList = namingService.getAllInstances("cloud-server-one");
            Instance instance1 = instancesList.get(0);
            instance1.setWeight(98);
            instance1.addMetadata("cpu.usage", "95");

            NamingMaintainService namingMaintainService = nacosServiceManager.getNamingMaintainService(properties);
            namingMaintainService.updateInstance(instance1.getServiceName(), instance1);
        } catch (NacosException e) {
            e.printStackTrace();
        }


        LOGGER.info("Insert lesson:{}", str);
        return str;
    }
}
