package cn.kgc.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 课程实体类
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/24 15:49
 */
@Data
public class Course {
    private Integer id;
    private String courseCode;
    private String courseName;
    private String description;
    private String teacher;
    private int credit;
    private int hours;
    private String major;
    private int maxStudents;
    private int currentStudents;
    private Date createTime;
    private Date updateTime;
}
