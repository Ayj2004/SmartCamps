package cn.kgc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨域配置类（与AiChatConfig同包）
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/26
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 配置跨域规则
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有接口生效
                // 允许前端域名（开发环境可通配，生产环境替换为真实前端地址，如http://localhost:8080）
                .allowedOriginPatterns("*")
                // 允许的请求方法（覆盖前端可能用到的GET/POST等）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // 允许所有请求头
                .allowCredentials(true) // 允许携带Cookie（如需前端传token/凭证需开启）
                .maxAge(3600); // 预检请求缓存时间（减少OPTIONS请求次数）
    }
}