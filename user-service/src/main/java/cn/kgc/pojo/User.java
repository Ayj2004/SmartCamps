package cn.kgc.pojo;

import lombok.Data;

import java.util.Date;

/**
 * 实体类（封装数据表记录）
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/23 17:14
 */
@Data
public class User {
    private Integer id;
    private String studentId;
    private String username;
    private String password;
    private String major;
    private Integer grade;
    private String email;
    private String phone;
    private Date createTime;
    private Date updateTime;
}
