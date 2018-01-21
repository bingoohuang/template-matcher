package com.github.bingoohuang.templatematcher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value @Builder @AllArgsConstructor
public class Template {
    private final String code;
    private final List<TemplatePart> parts;
}
