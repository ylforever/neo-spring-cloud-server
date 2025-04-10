package com.neo.springcloud.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
