package cn.kgc.service;

import cn.kgc.dto.UserDto;
import cn.kgc.pojo.User;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/24 08:43
 */
public interface UserService {
    /**
     * 登录
     *
     * @param studentId 学号
     * @param password  密码
     * @return 用户对象
     */
    public User login(String studentId, String password);

    /**
     * 获取用户信息 包含所修课程
     *
     * @param studentId 学号
     * @return 用户对象
     */
    public UserDto detail(String studentId);

    /**
     * 注册
     *
     * @param user 用户对象
     * @return 是否成功
     */
    public boolean register(User user);
}
