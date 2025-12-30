package cn.kgc.dto;

import lombok.Data;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/26 10:38
 */
@Data
public class StudyPlan {
    private String studentId;
    private String major;
    private String planContent;
    private String suggestion;
}
