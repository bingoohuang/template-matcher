package templatematcher;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.val;

import java.util.Map;

public class WxTemplateMatcher {
    Map<String, WxTemplate> wxTemplateMap;

    public WxTemplateMatcher(Map<String, String> templates) {
        wxTemplateMap = parseWxTemplate(templates);
    }

    private Map<String, WxTemplate> parseWxTemplate(Map<String, String> templates) {
        Map<String, WxTemplate> wxTemplates = Maps.newHashMapWithExpectedSize(templates.size());

        for (val entry : templates.entrySet()) {
            wxTemplates.put(entry.getKey(), parseWxTemplate(entry.getValue()));
        }

        return wxTemplates;
    }

    Splitter splitter = Splitter.on('\n').trimResults();

    private WxTemplate parseWxTemplate(String template) {
        val templateLines = splitter.splitToList(template);
        val builder = WxTemplate.builder();
        for (val line : templateLines) {

        }


        return builder.build();
    }

    public String match(String content) {
        return null;
    }
}
