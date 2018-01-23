package com.github.bingoohuang.templatematcher;

import com.google.common.collect.Lists;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class TemplateMatcherTest {
    @Test
    public void testMatchList() {
        val demo = "您好，您有一项预约已取消\n" +
                "预约人：杨一凡\n" +
                "预约项目：张丽 阴瑜伽（小班课）\n" +
                "预约时间：2015-09-28  16:00-17:00\n" +
                "取消原因：会员临时有事";
        val template1 = TemplateMatcher.parseTemplate("CANCEL1",
                "{{first.DATA}}\n" +
                        "预约人：{{keyword1.DATA}}\n" +
                        "预约项目：{{keyword2.DATA}}\n" +
                        "预约时间：{{keyword3.DATA}}\n" +
                        "取消原因：{{keyword4.DATA}}\n" +
                        "{{remark.DATA}}", "{{", "}}");
        val template2 = TemplateMatcher.parseTemplate("CANCEL2",
                "{{first.DATA}}\n" +
                        "预约人：{{keyword1.DATA}}\n" +
                        "预约项目：{{keyword2.DATA}}\n" +
                        "预约时间：{{keyword3.DATA}}\n" +
                        "{{remark.DATA}}", "{{", "}}");

        TemplateMatch match = TemplateMatcher.match(Lists.newArrayList(template1, template2), demo);

        assertThat(match.isMatched()).isTrue();
        assertThat(match.getVars()).isEqualTo(ofMap(
                "first", "您好，您有一项预约已取消",
                "keyword1", "杨一凡",
                "keyword2", "张丽 阴瑜伽（小班课）",
                "keyword3", "2015-09-28  16:00-17:00",
                "keyword4", "会员临时有事",
                "remark", ""
                )
        );
    }

    @Test
    public void testMatch1() {
        val demo = "您好，您有一项预约已取消\n" +
                "预约人：杨一凡\n" +
                "预约项目：张丽 阴瑜伽（小班课）\n" +
                "预约时间：2015-09-28  16:00-17:00\n" +
                "取消原因：会员临时有事";
        val template1 = TemplateMatcher.parseTemplate("CANCEL1",
                "{{first.DATA}}\n" +
                        "预约人：{{keyword1.DATA}}\n" +
                        "预约项目：{{keyword2.DATA}}\n" +
                        "预约时间：{{keyword3.DATA}}\n" +
                        "取消原因：{{keyword4.DATA}}\n" +
                        "{{remark.DATA}}", "{{", "}}");
        assertThat(template1.getCode()).isEqualTo("CANCEL1");
        assertThat(template1.getParts().size()).isEqualTo(11);

        val match1 = TemplateMatcher.match(template1, demo);
        assertThat(match1.isMatched()).isTrue();
        assertThat(match1.getVars()).isEqualTo(ofMap(
                "first", "您好，您有一项预约已取消",
                "keyword1", "杨一凡",
                "keyword2", "张丽 阴瑜伽（小班课）",
                "keyword3", "2015-09-28  16:00-17:00",
                "keyword4", "会员临时有事",
                "remark", ""
                )
        );
    }

    @Test
    public void testMatchLastConstant() {
        val demo = "您好，您有一项预约已取消,预约人：杨一凡,预约项目：张丽 阴瑜伽（小班课）," +
                "预约时间：2015-09-28  16:00-17:00,取消原因：会员临时有事,谢谢！";
        val template1 = TemplateMatcher.parseTemplate("CANCEL1",
                "{{first.DATA}},预约人：{{keyword1.DATA}},预约项目：{{keyword2.DATA}}," +
                        "预约时间：{{keyword3.DATA}},取消原因：{{keyword4.DATA}},{{remark.DATA}}谢谢！",
                "{{", "}}");
        assertThat(template1.getCode()).isEqualTo("CANCEL1");
        assertThat(template1.getParts().size()).isEqualTo(12);

        val match1 = TemplateMatcher.match(template1, demo);
        assertThat(match1.isMatched()).isTrue();
        assertThat(match1.getVars()).isEqualTo(ofMap(
                "first", "您好，您有一项预约已取消",
                "keyword1", "杨一凡",
                "keyword2", "张丽 阴瑜伽（小班课）",
                "keyword3", "2015-09-28  16:00-17:00",
                "keyword4", "会员临时有事",
                "remark", ""
                )
        );
    }

    @Test
    public void testMatch2() {
        val demo = "您好，您有一项预约已取消\n" +
                "预约人：杨一凡\n" +
                "预约项目：张丽 阴瑜伽（小班课）\n" +
                "预约时间：2015-09-28  16:00-17:00\n" +
                "取消原因：会员临时有事";

        val template2 = TemplateMatcher.parseTemplate("CANCEL2",
                "{{first.DATA}}\n" +
                        "预约人：{{keyword1.DATA}}\n" +
                        "预约项目：{{keyword2.DATA}}\n" +
                        "预约时间：{{keyword3.DATA}}\n" +
                        "{{remark.DATA}}", "{{", "}}");
        assertThat(template2.getCode()).isEqualTo("CANCEL2");
        assertThat(template2.getParts().size()).isEqualTo(9);

        val match2 = TemplateMatcher.match(template2, demo);
        assertThat(match2.isMatched()).isTrue();
        assertThat(match2.getVars()).isEqualTo(ofMap(
                "first", "您好，您有一项预约已取消",
                "keyword1", "杨一凡",
                "keyword2", "张丽 阴瑜伽（小班课）",
                "keyword3", "2015-09-28  16:00-17:00",
                "remark", "取消原因：会员临时有事"
                )
        );
    }

    @Test
    public void testNonMatch1() {
        val demo = "预约成功！\n" +
                "课程名称：阴瑜伽（张丽，小班课）\n" +
                "预约人数：1人\n" +
                "上课时间：2月14日 20:00-21:00\n" +
                "感谢你的预约。";

        val template = TemplateMatcher.parseTemplate("CANCEL2",
                "{{first.DATA}}\n" +
                        "预约人：{{keyword1.DATA}}\n" +
                        "预约项目：{{keyword2.DATA}}\n" +
                        "预约时间：{{keyword3.DATA}}\n" +
                        "{{remark.DATA}}", "{{", "}}");

        val match = TemplateMatcher.match(template, demo);
        assertThat(match.isMatched()).isFalse();
    }

    @Test
    public void testContinuousVar() {
        val template = TemplateMatcher.parseTemplate("CANCEL1",
                "{{first.DATA}}{{second.DATA}},预约人：{{keyword1.DATA}},预约项目：{{keyword2.DATA}}," +
                        "预约时间：{{keyword3.DATA}},取消原因：{{keyword4.DATA}},{{remark.DATA}}谢谢！", "{{", "}}");
        val demo = "您好，您有一项预约已取消,预约人：杨一凡,预约项目：张丽 阴瑜伽（小班课）," +
                "预约时间：2015-09-28  16:00-17:00,取消原因：会员临时有事,谢谢！";

        val match1 = TemplateMatcher.match(template, demo);
        assertThat(match1.isMatched()).isTrue();
        assertThat(match1.getCode()).isEqualTo("CANCEL1");
        assertThat(match1.getConsts()).isEqualTo(6);
        assertThat(match1.getVars()).isEqualTo(ofMap(
                "first", "",
                "second", "您好，您有一项预约已取消",
                "keyword1", "杨一凡",
                "keyword2", "张丽 阴瑜伽（小班课）",
                "keyword3", "2015-09-28  16:00-17:00",
                "keyword4", "会员临时有事",
                "remark", ""
                )
        );
    }

    @Test
    public void testBadTemplate() {
        try {
            TemplateMatcher.parseTemplate("CANCEL1",
                    "{{first.DATA{{second.DATA}},预约人：{{keyword1.DATA}},预约项目：{{keyword2.DATA}}," +
                            "预约时间：{{keyword3.DATA}},取消原因：{{keyword4.DATA}},{{remark.DATA}}谢谢！", "{{", "}}");
            Assert.fail();
        } catch (Exception ex) {
            assertThat(ex.getMessage()).isEqualTo("模板CANCEL1格式错误，{{和}}不匹配");
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testPrivateConstructor() throws Exception {
        val constructor = TemplateMatcher.class.getDeclaredConstructor();
        assertWithMessage("Constructor is private")
                .that(Modifier.isPrivate(constructor.getModifiers())).isTrue();

        constructor.setAccessible(true);
        constructor.newInstance();
    }

    private Map<String, String> ofMap(String... kvs) {
        Map<String, String> result = new HashMap<String, String>(kvs.length / 2);
        for (int i = 0, ii = kvs.length; i + 1 < ii; i += 2) {
            result.put(kvs[i], kvs[i + 1]);
        }

        return result;
    }
}
