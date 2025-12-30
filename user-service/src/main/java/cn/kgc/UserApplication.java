package cn.kgc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * springboot项目启动类
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/23 15:52
 */
@SpringBootApplication
@MapperScan("cn.kgc.mapper")
@EnableFeignClients
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
