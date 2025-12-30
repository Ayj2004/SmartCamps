package cn.kgc.service;

import cn.kgc.pojo.Course;

import java.util.List;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/24 15:54
 */
public interface CourseService {
    /**
     * 根据专业查询课程信息
     * @param major
     * @return
     */
    public List<Course> getCourseByMajor(String major);

    /**
     * 根据专业查询课程名称
     * @param major 专业
     * @return 课程列表
     */
    public List<String> getCoursenameByMajor(String major);
}
