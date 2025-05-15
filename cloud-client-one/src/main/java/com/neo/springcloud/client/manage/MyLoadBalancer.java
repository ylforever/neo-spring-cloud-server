package com.neo.springcloud.client.manage;

import com.neo.springcloud.client.model.CPUUsageCompare;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * 自定义负载均衡器. 基于CPU利用率选择调用的服务实例.
 *
 * @author neo
 * @since 2025/4/24
 * @version 1.0
 */
public class MyLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Logger LOGGER = LogManager.getLogger(MyLoadBalancer.class);

    private final String serviceId;

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public MyLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                          String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map((serviceInstances) -> this.processInstanceResponse(supplier, serviceInstances));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback)supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }

        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            LOGGER.error("No servers available for service:{} ", serviceId);
            return new EmptyResponse();
        }

        /**
         * 选择一个CPU利用率最低的服务实例返回.
         */
        List<CPUUsageCompare> compareModelList = CPUUsageCompare.valueOf(instances);
        Optional<CPUUsageCompare> minUsageInstance = compareModelList.stream().min((o1, o2) -> {
            if (o1.getCpuUsage() > o2.getCpuUsage()) {
                return 1;
            } else if (o1.getCpuUsage() < o2.getCpuUsage()) {
                return -1;
            } else {
                return 0;
            }
        });

        if (minUsageInstance.isPresent()) {
            return new DefaultResponse(minUsageInstance.get().getServiceInstance());
        } else {
            return new EmptyResponse();
        }
    }
}
