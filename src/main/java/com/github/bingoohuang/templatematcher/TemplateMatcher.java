package com.github.bingoohuang.templatematcher;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j @UtilityClass
public class TemplateMatcher {
    public TemplateMatch match(List<Template> templates, String content) {
        if (StringUtils.isBlank(content)) return null;

        TemplateMatch lastMatched = null;
        for (val template : templates) {
            val matched = match(template, content);
            if (matched.isMatched()) {
                if (lastMatched == null || matched.getConsts() > lastMatched.getConsts()) {
                    lastMatched = matched;
                }
            }
        }

        return lastMatched;
    }

    public TemplateMatch match(Template template, String content) {
        val builder = TemplateMatch.builder().code(template.getCode());

        int pos = 0;
        int consts = 0;
        TemplatePart previousVar = null;

        for (val part : template.getParts()) {
            if (part.isConstant()) {
                int index = content.indexOf(part.getValue(), pos);
                if (index < 0) {
                    if (!part.isConstantIgnore()) {
                        return builder.matched(false).build();
                    }

                    index = content.length();
                } else {
                    if (!part.isConstantIgnore()) ++consts;
                }

                if (previousVar != null) {
                    builder.var(previousVar.getValue(), content.substring(pos, index));
                    previousVar = null;
                }
                pos = index + part.getValue().length();

            } else {
                if (previousVar != null) {
                    builder.var(previousVar.getValue(), "");
                }

                previousVar = part;
            }
        }

        if (previousVar != null) {
            builder.var(previousVar.getValue(), StringUtils.substring(content, pos));
        }

        return builder.matched(true).consts(consts).build();
    }

    public Template parseTemplate(
            String templateCode, String templateContent, String openBracket, String closeBracket) {
        int pos = 0;

        int length = templateContent.length();
        val builderParts = new ArrayList<TemplatePart.TemplatePartBuilder>();
        TemplatePart.TemplatePartBuilder lastConstPartBuilder = null;
        String lastValue = null;
        boolean lastVar = false;
        while (pos < length) {
            int startPos = templateContent.indexOf(openBracket, pos);
            if (startPos >= 0) {
                int fromIndex = startPos + openBracket.length();
                int startPrevent = templateContent.indexOf(openBracket, fromIndex);
                int endPos = templateContent.indexOf(closeBracket, fromIndex);
                if (endPos < 0 || (endPos >= 0 && startPrevent >= 0 && startPrevent < endPos)) {
                    throw new RuntimeException("模板" + templateCode + "格式错误，" + openBracket + "和" + closeBracket + "不匹配");
                }

                if (startPos > pos) {
                    lastValue = templateContent.substring(pos, startPos);
                    lastConstPartBuilder = TemplatePart.builder().constant(true).value(lastValue);
                    builderParts.add(lastConstPartBuilder);
                    lastVar = false;
                }

                if (lastVar) {
                    log.warn("continuous variables found in template code {}, content {}",
                            templateCode, templateContent);
                }
                lastVar = true;
                String varName = templateContent.substring(fromIndex, endPos);
                if (varName.endsWith(".DATA")) {
                    varName = varName.substring(0, varName.length() - 5);
                }

                builderParts.add(TemplatePart.builder().constant(false).value(varName));
                pos = endPos + closeBracket.length();
            } else {
                builderParts.add(TemplatePart.builder().constant(true).value(templateContent.substring(pos)));
                break;
            }
        }

        if (lastConstPartBuilder != null && StringUtils.isBlank(lastValue)) {
            lastConstPartBuilder.constantIgnore(true);
        }

        val parts = new ArrayList<TemplatePart>(builderParts.size());
        for (val part : builderParts) {
            parts.add(part.build());
        }

        return Template.builder().code(templateCode).parts(parts).build();
    }
}
