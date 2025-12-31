package cn.kgc.controller;

import cn.kgc.dto.QuestionRequest;
import cn.kgc.dto.StudyPlan;
import cn.kgc.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/26 11:05
 */
@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("ask")
    public String askQuestion(@RequestBody QuestionRequest request) {
        System.out.println("请求参数：" + request.getQuestion());
        System.out.println("请求参数：" + request.getStudentId());
        return aiService.askQuestion(request);
    }

    /**
     * 专业学习计划
     * @param major 专业名称
     * @param studentId 学生学号
     * @return 学习计划内容
     */
    @GetMapping("study-plan")
    public StudyPlan generateStudyPlan(@RequestParam("major") String major,
                                       @RequestParam("studentId") String studentId) {
        // 注意：原代码只返回planContent，这里改为返回完整StudyPlan对象（更合理，前端可按需取值）
        // 若需保持原返回String逻辑，可改回 return aiService.generateStudyPlan(major, studentId).getPlanContent();
        return aiService.generateStudyPlan(major, studentId);
    }

    /**
     * AI翻译接口
     * @param content 待翻译内容
     * @param targetLang 目标语言（zh/en/jp/ko等）
     * @return 翻译结果
     */
    @GetMapping("translate")
    public String aiTranslate(@RequestParam("content") String content,
                              @RequestParam("targetLang") String targetLang) {
        return aiService.aiTranslate(content, targetLang);
    }

    /**
     * AI计算机接口（解决数学/编程问题）
     * @return 计算结果+解题思路
     */
    @PostMapping("calculator")
    public String aiCalculator(@RequestBody Map<String, String> request) {
        String problem = request.get("problem");
        return aiService.aiCalculator(problem);
    }

    /**
     * AI诗词创作接口
     * @param theme 诗词主题
     * @param type 诗词类型（五言绝句/七言律诗/现代诗等）
     * @return 创作的诗词
     */
    @GetMapping("poetry")
    public String aiPoetry(@RequestParam("theme") String theme,
                           @RequestParam("type") String type) {
        return aiService.aiPoetry(theme, type);
    }

    /**
     * 推荐课程
     */
    @GetMapping("/recommend-courses")
    public List<String> recommendCourses(@RequestParam("interests") String interests) {
        return aiService.recommendCourses(interests);
    }

    /**
     * 获取配置信息
     */
    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxTokens", 500);
        config.put("temperature", 0.7);
        config.put("model", "deepseek-chat");
        return config;
    }

    /**
     * 健康检查接口（供前端测试连接）
     * 前端调用 /api/ai/health 即可判断后端服务是否可用
     */
    @GetMapping("/health")
    public Map<String, Object> checkHealth() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP"); // UP表示服务正常，DOWN表示异常
        health.put("timestamp", System.currentTimeMillis()); // 响应时间戳
        health.put("service", "ai-assistant-service"); // 补充服务名称，方便前端识别
        health.put("version", "1.0.0"); // 补充版本信息，可选
        return health;
    }
}