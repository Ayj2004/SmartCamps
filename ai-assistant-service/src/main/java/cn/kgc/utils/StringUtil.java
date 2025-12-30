package cn.kgc.utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XiaoMo
 * @version 1.0
 * @date 2025/12/26 16:20
 */
public class StringUtil {
    public static String joinWithComma(List<String> stringList) {
        if (stringList == null) {
            return null;
        }

        return stringList.stream()
                .filter(item -> item != null) // 过滤 null 值
                .collect(Collectors.joining(","));
    }
}
