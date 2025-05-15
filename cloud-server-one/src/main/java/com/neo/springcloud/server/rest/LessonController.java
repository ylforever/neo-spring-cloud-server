package com.neo.springcloud.server.rest;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
        LOGGER.info("Insert lesson:{}", str);
        return str;
    }
}
