package cn.kgc.dto;

import lombok.Data;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/25 08:55
 */
@Data
public class CourseDto {
    private String courseCode;
    private String courseName;
    private String description;
    private String teacher;
    private int credit;
    private int hours;
    private String major;
    private int maxStudents;
    private int currentStudents;
}
