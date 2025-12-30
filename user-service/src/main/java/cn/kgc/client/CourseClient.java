package cn.kgc.client;

import cn.kgc.dto.CourseDto;
import cn.kgc.dto.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/25 14:12
 */
@FeignClient(name = "course-service")
public interface CourseClient {
    /**
     * 根据专业查询课程信息
     *
     * @param major 专业
     * @return 课程列表
     */
    @GetMapping("/course/major")
    public Result getCourseByMajor(@RequestParam("major") String major);
}
