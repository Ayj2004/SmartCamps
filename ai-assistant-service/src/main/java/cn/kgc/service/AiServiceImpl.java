package cn.kgc.service;

import cn.hutool.core.util.ObjectUtil;
import cn.kgc.client.CourseClient;
import cn.kgc.dto.QuestionRequest;
import cn.kgc.dto.StudyPlan;
import cn.kgc.utils.StringUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/26 10:42
 */
@Service
public class AiServiceImpl implements AiService {
    @Autowired
    private CourseClient courseClient;

    @Autowired(required = false)
    private ChatClient chatClient;

    @Autowired
    private OpenAiChatOptions openAiChatOptions;

    /**
     * 提问
     *
     * @param request
     * @return
     */
    @Override
    public String askQuestion(QuestionRequest request) {
        if (ObjectUtil.isNull(chatClient)) {
            System.out.println("deepseek：api-key无效");
            return getMockResponse(request);
        }
        try {
            return chatClient.prompt()
                    .user(u -> u.text(buildPrompt(request)))
                    .options(openAiChatOptions)
                    .call()
                    .content();
        } catch (Exception e) {
            return getMockResponse(request);
        }
    }

    /**
     * 某个专业的学习计划
     *
     * @param major
     * @param studentId
     * @return
     */
    @Override
    public StudyPlan generateStudyPlan(String major, String studentId) {
        List<String> courseList = courseClient.getCoursenameByMajor(major);
        String courseContent = StringUtil.joinWithComma(courseList);

        StudyPlan studyPlan = new StudyPlan();
        studyPlan.setStudentId(studentId);
        studyPlan.setMajor(major);

        // 修复点1：chatClient为空时，设置模拟数据并直接返回，避免后续空指针
        if (ObjectUtil.isNull(chatClient)) {
            System.out.println("deepseek：api-key无效 或者响应超时");
            studyPlan.setPlanContent(getMockResponse(major));
            studyPlan.setSuggestion("此计划由模拟数据生成，请根据实际情况调整"); // 补全模拟场景的suggestion
            return studyPlan;
        }

        // 修复点2：添加try-catch捕获AI调用异常，兜底返回模拟数据
        try {
            String prompt = String.format("""
                    请为  %s 专业的学生根据课程 %s 制定一个学期的学习计划
                    包含：学习目标、时间安排、学习资源、评估方法。
                    用中文回复，结构清晰。
                    """, major, courseContent); // 修复文案笔误：结构请求→结构清晰
            String content = chatClient.prompt()
                    .user(u -> u.text(prompt))
                    .options(openAiChatOptions)
                    .call()
                    .content();
            studyPlan.setPlanContent(content);
            studyPlan.setSuggestion("此计划由AI生成，请根据实际情况调整");
        } catch (Exception e) {
            // 异常时返回模拟数据，避免接口抛错
            System.out.println("生成学习计划异常：" + e.getMessage());
            studyPlan.setPlanContent(getMockResponse(major));
            studyPlan.setSuggestion("此计划由模拟数据生成，请根据实际情况调整");
        }
        return studyPlan;
    }

    /**
     * 根据兴趣推荐课程
     *
     * 简单实现思路：
     * 1. 根据兴趣关键词粗略映射一个“最相关专业”
     * 2. 通过 course-service 查询该专业下的课程列表
     * 3. 做一个简单的过滤/排序（目前先直接返回前若干门课程）
     * 4. 若远程调用失败，则返回一组内置的推荐课程
     *
     * @param interests 兴趣描述
     * @return 推荐课程名称列表
     */
    @Override
    public List<String> recommendCourses(String interests) {
        try {
            String major = mapInterestsToMajor(interests);
            List<String> courseNames = courseClient.getCoursenameByMajor(major);
            if (courseNames == null || courseNames.isEmpty()) {
                return getDefaultRecommendCourses();
            }
            // 简单限制数量，避免列表过长
            int limit = Math.min(courseNames.size(), 8);
            return courseNames.subList(0, limit);
        } catch (Exception e) {
            System.out.println("推荐课程调用异常：" + e.getMessage());
            return getDefaultRecommendCourses();
        }
    }

    /**
     * 将兴趣关键词粗略映射到专业名称
     */
    private String mapInterestsToMajor(String interests) {
        if (interests == null) {
            return "计算机科学";
        }
        String lower = interests.toLowerCase();
        if (lower.contains("ai") || lower.contains("人工智能") || lower.contains("machine learning") || lower.contains("ml")) {
            return "人工智能";
        }
        if (lower.contains("大数据") || lower.contains("data") || lower.contains("数据分析")) {
            return "数据科学";
        }
        if (lower.contains("安全") || lower.contains("network") || lower.contains("攻防") || lower.contains("渗透")) {
            return "网络安全";
        }
        if (lower.contains("java") || lower.contains("后端") || lower.contains("spring")) {
            return "软件工程";
        }
        return "计算机科学";
    }

    /**
     * 远程服务异常时的一组兜底推荐课程
     */
    private List<String> getDefaultRecommendCourses() {
        return java.util.Arrays.asList(
                "程序设计基础（C/Java）",
                "数据结构与算法",
                "数据库系统原理",
                "计算机网络",
                "操作系统",
                "软件工程导论",
                "Web 开发基础",
                "Linux 与服务器基础"
        );
    }

    private String buildPrompt(QuestionRequest request) {
        return String.format("""
                你现在是一个智能校园助手，正在帮助大学生解决问题。
                学生信息：
                - 学号：%s 
                - 问题：%s
                
                 请用中文回答，要求如下：
                 1、专业、准确、有帮助
                 2、语气友好
                 3、提供具体可行的方案
                 4、添加一些表情符号增强亲和力                
                """, request.getStudentId(), request.getQuestion());
    }

    /**
     * 模拟响应
     *
     * @param major
     * @return
     */
    private String getMockResponse(String major) {
        return String.format("""
                %s 专业学习计划(模拟)
                一、学习目标
                1、掌握核心专业知识
                2、完成项目实战开发
                3、提高解决问题能力
                
                二、事件安排（16周）
                1、1-4周 基础学习
                2、5-8周 核心技能练习
                3、9-12周 项目开发
                4、13-16周 复习巩固                              
                """, major);
    }

    /**
     * 模拟响应
     *
     * @param request
     * @return
     */
    private String getMockResponse(QuestionRequest request) {
        return String.format("""
                【模拟响应】 学生%s 提问：%s
                
                建议：
                1、明确学习目标，指定详细学习计划
                2、寻找优质资源
                
                """, request.getStudentId(), request.getQuestion());
    }

    /**
     * AI翻译实现
     */
    @Override
    public String aiTranslate(String content, String targetLang) {
        if (ObjectUtil.isNull(chatClient)) {
            return getMockTranslateResponse(content, targetLang);
        }
        try {
            String prompt = String.format("""
                    请将以下内容翻译成%s：
                    %s
                    要求：翻译准确、语句通顺，保留原义。
                    """, targetLang, content);
            return chatClient.prompt()
                    .user(u -> u.text(prompt))
                    .options(openAiChatOptions)
                    .call()
                    .content();
        } catch (Exception e) {
            System.out.println("AI翻译异常：" + e.getMessage());
            return getMockTranslateResponse(content, targetLang);
        }
    }

    /**
     * AI计算机实现
     */
    @Override
    public String aiCalculator(String problem) {
        if (ObjectUtil.isNull(chatClient)) {
            return getMockCalculatorResponse(problem);
        }
        try {
            String prompt = String.format("""
                    请解决以下数学/编程问题：
                    %s
                    要求：
                    1. 给出详细解题步骤
                    2. 最终结果清晰标注
                    3. 语言通俗易懂
                    """, problem);
            return chatClient.prompt()
                    .user(u -> u.text(prompt))
                    .options(openAiChatOptions)
                    .call()
                    .content();
        } catch (Exception e) {
            System.out.println("AI计算机异常：" + e.getMessage());
            return getMockCalculatorResponse(problem);
        }
    }

    /**
     * AI诗词创作实现
     */
    @Override
    public String aiPoetry(String theme, String type) {
        if (ObjectUtil.isNull(chatClient)) {
            return getMockPoetryResponse(theme, type);
        }
        try {
            String prompt = String.format("""
                    请以“%s”为主题创作一首%s，要求：
                    1. 符合对应诗词体裁的格式要求
                    2. 意境优美，语句通顺
                    3. 原创性强，避免抄袭
                    """, theme, type);
            return chatClient.prompt()
                    .user(u -> u.text(prompt))
                    .options(openAiChatOptions)
                    .call()
                    .content();
        } catch (Exception e) {
            System.out.println("AI诗词创作异常：" + e.getMessage());
            return getMockPoetryResponse(theme, type);
        }
    }

    // 翻译模拟响应
    private String getMockTranslateResponse(String content, String targetLang) {
        if ("en".equals(targetLang) || "英语".equals(targetLang)) {
            return "[模拟翻译] " + content + " -> Mock translation of '" + content + "'";
        } else if ("jp".equals(targetLang) || "日语".equals(targetLang)) {
            return "[模拟翻译] " + content + " -> 「" + content + "」の模擬翻訳";
        } else {
            return "[模拟翻译] " + content + " -> 目标语言[" + targetLang + "]模拟翻译结果";
        }
    }

    // 计算机模拟响应
    private String getMockCalculatorResponse(String problem) {
        return String.format("""
                【模拟解题】%s
                1. 解题思路：根据基础数学/编程逻辑分析问题
                2. 解题步骤：
                   - 步骤1：明确问题核心需求
                   - 步骤2：梳理解题所需公式/语法
                   - 步骤3：逐步推导计算
                3. 最终结果：模拟计算结果（请使用真实AI服务获取准确答案）
                """, problem);
    }

    // 诗词创作模拟响应
    private String getMockPoetryResponse(String theme, String type) {
        return String.format("""
                【模拟%s·%s】
                （因AI服务暂不可用，展示模拟诗词）
                云卷云舒映%s，
                风来风去意悠然。
                浮生若梦皆成景，
                一笔挥毫赋锦篇。
                """, type, theme, theme);
    }
}