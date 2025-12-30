package cn.kgc.dto;

import lombok.Data;

import java.util.List;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/25 08:51
 */
@Data
public class UserDto {
    private String studentId;
    private String username;
    private String major;
    private Integer grade;
    //此学生所学课程
    private List<CourseDto> courseList;

}
