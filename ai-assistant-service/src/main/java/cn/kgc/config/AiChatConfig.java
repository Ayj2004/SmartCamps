package cn.kgc.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/26 10:27
 */
@Configuration
public class AiChatConfig {
    /**
     * 绑定配置文件
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.ai.openai.chat.options")
    public OpenAiChatOptions openAiChatOptions() {
        return OpenAiChatOptions.builder().build();
    }

    /**
     * 创建模型
     *
     * @param openAiChatModel 模型参数
     * @return
     */
    @Bean
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel).build();
    }

}
