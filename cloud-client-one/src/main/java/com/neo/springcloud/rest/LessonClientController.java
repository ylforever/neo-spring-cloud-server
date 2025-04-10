package com.neo.springcloud.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 客户端测试接口
 *
 * @author neo
 * @since 2025/4/10
 * @version 1.0
 */
@RestController
@RequestMapping("/v1/lesson-client")
public class LessonClientController {
    private static final Logger LOGGER = LogManager.getLogger(LessonClientController.class);

    @Resource
    private RestTemplate restTemplate;
    
    /**
     * 插入课程数据
     *
     * @return 分配的课程编码
     */
    @PostMapping("/insert-lesson")
    public String insertLesson(@RequestBody String str)
    {
        LOGGER.info("Insert lesson:{}", str);
        return restTemplate.postForObject("http://cloud-server-one/spring-cloud-server/v1/lesson/insert-lesson", str, String.class);
    }
}
