package cn.kgc.service;

import cn.kgc.dto.QuestionRequest;
import cn.kgc.dto.StudyPlan;

/**
 * ai业务场景
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/26 10:34
 */
public interface AiService {
    /**
     * 提问
     *
     * @param request
     * @return
     */
    public String askQuestion(QuestionRequest request);

    /**
     * 某个专业的学习计划
     * @param major
     * @param studentId
     * @return
     */
    public StudyPlan generateStudyPlan(String major,String studentId);

    /**
     * 根据学生的兴趣关键词推荐课程
     * （内部会结合课程服务的课程数据做一个简单的智能推荐）
     *
     * @param interests 学生的兴趣或学习方向描述，例如“Java 后端开发”“人工智能与大数据分析”等
     * @return 推荐课程名称列表
     */
    public java.util.List<String> recommendCourses(String interests);
    /**
     * 学习路线图
     * @param studentId
     * @return
     */

    /**
     * 推荐课程
     * @param studentId
     * @return
     */
}
