package cn.kgc.mapper;

import cn.kgc.pojo.User;

/**
 * 负责查询user表数据
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/23 17:21
 */
public interface UserMapper {
    /**
     * 根据学号查询用户信息
     * @param studentId 学号
     * @return 学生对象
     */
    public User getUserByStudentId(String studentId);

    /**
     * 添加用户
     * @param user 用户对象
     * @return 添加结果
     */
    public int addUser(User user);
}
