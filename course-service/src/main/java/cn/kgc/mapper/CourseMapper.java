package cn.kgc.mapper;

import cn.kgc.pojo.Course;
import feign.Param;

import java.util.List;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/24 15:52
 */
public interface CourseMapper {
    /**
     * 根据专业查询课程信息
     * @param major 专业
     * @return 课程列表
     */
    public List<Course> getCourseByMajor(String major);

    /**
     * 根据专业查询课程名称
     * @param major 专业
     * @return 课程列表
     */
    public List<String> getCoursenameByMajor(String major);


    List<String> selectNamesByMajor(@Param("major") String major);
}
