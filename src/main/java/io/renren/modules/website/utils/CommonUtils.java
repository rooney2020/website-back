package io.renren.modules.website.utils;

import java.util.UUID;

/**
 * 请修改注释
 *
 * @author zhaoliyuan
 * @date 2021.01.18
 */
public class CommonUtils {

    /**
     * 生成uuid
     */
    public static Integer uuid() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 删除uuid中的"-"
        String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
        // 获取uuid 的hashcode
        int a = uuid.hashCode();
        // hashcode 的绝对值 因为uuid的hashcode可能为负数
        return Math.abs(a);
    }
}
