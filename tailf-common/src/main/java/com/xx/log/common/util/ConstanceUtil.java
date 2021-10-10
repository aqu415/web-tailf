package com.xx.log.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstanceUtil {

    public static final String CMD_BIND = "BIND";

    // nio 粘包处理识别的结尾符号
    public static final String DECODER_DELIMITER = "$;&";

    // 返回值行内属性值分隔符
    public static final String VERTICAL_DELIMITER = "#&";

    // 返回值行间分隔符
    public static final String SEMICOLON = ";";

    // 界面树节点上展示的符号
    public static final String RIGHT_ARROW = "->";
}
