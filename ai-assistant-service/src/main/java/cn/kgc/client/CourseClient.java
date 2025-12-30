package cn.kgc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
     * @param majorName 专业
     * @return 课程列表
     */
    @GetMapping("/course/major/{majorName}")
    public List<String> getCoursenameByMajor(@PathVariable("majorName") String majorName);
}
