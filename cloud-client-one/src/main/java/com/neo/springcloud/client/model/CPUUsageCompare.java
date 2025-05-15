package com.neo.springcloud.client.model;

import lombok.Getter;
import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于CPU利用率的比较模型
 *
 * @author neo
 * @since 2025/4/25
 * @version 1.0
 */
@Getter
public class CPUUsageCompare {
    /**
     * CPU利用率
     */
    private final int cpuUsage;

    /**
     * 服务实例
     */
    private final ServiceInstance serviceInstance;

    public CPUUsageCompare(int value, ServiceInstance serviceInstance){
        this.cpuUsage = value;
        this.serviceInstance = serviceInstance;
    }

    /** 
     * 模型转换
     *
     * @param serviceInstanceList 服务实例列表
     * @return 比较模型列表
     * @author neo
     * @since 2025/4/25
     */ 
    public static List<CPUUsageCompare> valueOf(List<ServiceInstance> serviceInstanceList) {
        List<CPUUsageCompare> serviceInstanceCompares = new ArrayList<>();
        for (ServiceInstance instance : serviceInstanceList) {
            String cpuUsage  = instance.getMetadata().get("cpu.usage");

            int iCPUUsage = 0;
            if (cpuUsage != null && !cpuUsage.isEmpty()) {
                iCPUUsage = Integer.parseInt(cpuUsage);
            }

            serviceInstanceCompares.add(new CPUUsageCompare(iCPUUsage, instance));
        }

        return serviceInstanceCompares;
    }
}
