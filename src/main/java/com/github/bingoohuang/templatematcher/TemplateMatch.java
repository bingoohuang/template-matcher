package com.github.bingoohuang.templatematcher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Map;

@Value @Builder @AllArgsConstructor
public class TemplateMatch {
    private final boolean matched;
    private final String code;
    @Singular private final Map<String, String> vars;
    private final int consts;
}
