package cn.kgc.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.kgc.dto.CourseDto;
import cn.kgc.dto.Result;
import cn.kgc.dto.ResultUtil;
import cn.kgc.pojo.Course;
import cn.kgc.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/24 15:58
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    /**
     * 根据专业查询课程信息
     *
     * @param major 专业
     * @return 课程列表
     */
    @GetMapping("/major")
    public Result getCourseByMajor(@RequestParam("major") String major) {
        List<Course> courseList = courseService.getCourseByMajor(major);
        List<CourseDto> courseDtos = BeanUtil.copyToList(courseList, CourseDto.class);
        if (courseList != null && courseList.size() > 0) {
            return ResultUtil.returnSuccess("查询成功", courseDtos);
        }
        return ResultUtil.returnFail("此专业暂无课程内容");
    }

    /**
     * 根据专业查询课程信息
     *
     * @param majorName 专业
     * @return 课程列表
     */
    @GetMapping("/major/{majorName}")
    public List<String> getCoursenameByMajor(@PathVariable("majorName") String majorName) {
        return courseService.getCoursenameByMajor(majorName);
    }
}
