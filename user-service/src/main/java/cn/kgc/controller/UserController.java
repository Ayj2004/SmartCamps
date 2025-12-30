package cn.kgc.controller;

import cn.kgc.client.CourseClient;
import cn.kgc.dto.CourseDto;
import cn.kgc.dto.Result;
import cn.kgc.dto.ResultUtil;
import cn.kgc.dto.UserDto;
import cn.kgc.pojo.User;
import cn.kgc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/23 15:54
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CourseClient courseClient;


    @GetMapping("/hello")
    public Result hello() {
        return ResultUtil.returnSuccess("hello world 鲁东大学实训");
    }

    /**
     * 登录
     *
     * @param studentId 学号
     * @param password  密码
     * @return 用户对象
     */

    @GetMapping("login")
    public Result login(@RequestParam("studentId") String studentId, @RequestParam("password") String password) {
        User user = userService.login(studentId, password);


        if (user != null) {
            return ResultUtil.returnDataSuccess(user);
        }
        return ResultUtil.returnFail("用户名或密码错误");
    }

    /**
     * 获取用户详情
     *
     * @param studentId
     * @return
     */
    @GetMapping("detail/{studentId}")
    public Result detail(@PathVariable("studentId") String studentId) {
        UserDto userDto = userService.detail(studentId);
        Result result = courseClient.getCourseByMajor(userDto.getMajor());
        if (userDto != null) {
            if(result.getCode().equals("0")){
                List<CourseDto> courseDtoList=(List<CourseDto>) result.getData();
                userDto.setCourseList(courseDtoList);
            }
            return ResultUtil.returnDataSuccess(userDto);
        }
        return ResultUtil.returnFail("用户不存在");
    }

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @PostMapping("register")
    public Result register(@RequestBody User user) {
        boolean register = userService.register(user);
        if (register) {
            return ResultUtil.returnSuccess("注册成功");
        }
        return ResultUtil.returnFail("注册失败");
    }
}
