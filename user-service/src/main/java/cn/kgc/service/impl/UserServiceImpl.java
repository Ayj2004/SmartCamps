package cn.kgc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.kgc.dto.UserDto;
import cn.kgc.mapper.UserMapper;
import cn.kgc.pojo.User;
import cn.kgc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/24 08:44
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     *
     * @param studentId 学号
     * @param password  密码
     * @return 用户对象
     */
    @Override
    public User login(String studentId, String password) {

        User user = userMapper.getUserByStudentId(studentId);

        if (user != null && user.getPassword().equals(password)) {
            //正确，则返回用户对象
            return user;
        }
        //用户名或密码错误，则返回空null
        return null;
    }

    @Override
    public UserDto detail(String studentId) {
        User user = userMapper.getUserByStudentId(studentId);
        UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
        return userDto;
    }

    /**
     * 注册
     *
     * @param user 用户对象
     * @return 是否成功
     */
    @Override
    public boolean register(User user) {
        int rows = userMapper.addUser(user);
        return rows > 0 ? true : false;
    }
}
