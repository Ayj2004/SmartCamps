package cn.kgc.service.impl;

import cn.kgc.mapper.CourseMapper;
import cn.kgc.pojo.Course;
import cn.kgc.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/24 15:55
 */
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Course> getCourseByMajor(String major) {
        return courseMapper.getCourseByMajor(major);
    }

    @Override
    public List<String> getCoursenameByMajor(String major) {
        return courseMapper.selectNamesByMajor(major);
    }
}


