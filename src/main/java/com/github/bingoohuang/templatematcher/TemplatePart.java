package com.github.bingoohuang.templatematcher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value @Builder @AllArgsConstructor
public class TemplatePart {
    private final boolean constant; // 是否是常量
    private final String value; // 常量值，或者变量名
    private final boolean constantIgnore; // 最后一个常量，并且是空白，则可以忽略
}
