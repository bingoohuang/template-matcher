package templatematcher;


import lombok.val;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

public class TemplateMatcherTest {
    @Test
    public void testWx() {
        val demo = "亲～明天要上课啦～\n" +
                "课程名：减脂塑身瑜伽（小班课）\n" +
                "授课教练：王蕊\n" +
                "上课地址：国创园7栋301  场地：大教室\n" +
                "上课时间：2015-09-26  15:00-16:00";

        String key = wxTemplateMatcher.match(demo);
        assertThat(key).isEqualTo("上课提醒通知");
    }


    static Map<String, String> templates = new HashMap<String, String>() {
        {
            put("上课提醒通知", "{{first.DATA}}\n" +
                    "课程名：{{keyword1.DATA}}\n" +
                    "授课教练：{{keyword2.DATA}}\n" +
                    "上课地址：{{keyword3.DATA}}\n" +
                    "{{remark.DATA}}");

            put("预约提醒",
                    "{{first.DATA}}\n" +
                            "课程名称：{{keyword1.DATA}}\n" +
                            "预约人数：{{keyword2.DATA}}\n" +
                            "上课时间：{{keyword3.DATA}}\n" +
                            "{{remark.DATA}}");

            put("预约取消",
                    "{{first.DATA}}\n" +
                            "预约人：{{keyword1.DATA}}\n" +
                            "预约项目：{{keyword2.DATA}}\n" +
                            "预约时间：{{keyword3.DATA}}\n" +
                            "取消原因：{{keyword4.DATA}}\n" +
                            "{{remark.DATA}}");

            put("服务评价提醒",
                    "{{first.DATA}}\n" +
                            "课程名称：{{keyword1.DATA}}\n" +
                            "上课时间：{{keyword2.DATA}}\n" +
                            "{{remark.DATA}}");

            put("课程变动通知",
                    "{{first.DATA}}\n" +
                            "原：{{keyword1.DATA}}\n" +
                            "现：{{keyword2.DATA}}\n" +
                            "变动类型：{{keyword3.DATA}}\n" +
                            "{{remark.DATA}}");

            put("会员卡到期提醒",
                    "{{first.DATA}}\n" +
                            "您的{{name.DATA}}有效期至{{expDate.DATA}}\n" +
                            "{{remark.DATA}}");

            put("缺课提醒",
                    "{{first.DATA}}\n" +
                            "学员姓名：{{keyword1.DATA}}\n" +
                            "缺课时间：{{keyword2.DATA}}\n" +
                            "{{remark.DATA}}");

            put("开班人数达标提醒",
                    "{{first.DATA}}\n" +
                            "班级名称：{{keyword1.DATA}}\n" +
                            "开班时间：{{keyword2.DATA}}\n" +
                            "已报名人数：{{keyword3.DATA}}\n" +
                            "{{remark.DATA}}");

            put("预约违约提醒",
                    "{{first.DATA}}\n" +
                            "会员卡号：{{keyword1.DATA}}\n" +
                            "预约上课日期：{{keyword2.DATA}}\n" +
                            "预约上课时间：{{keyword3.DATA}}\n" +
                            "{{remark.DATA}}");

            put("申请审核提醒",
                    "{{first.DATA}}\n" +
                            "会员卡：{{keyword1.DATA}}\n" +
                            "实收金额：{{keyword2.DATA}}\n" +
                            "{{remark.DATA}}");

            put("审核通知",
                    "{{first.DATA}}\n" +
                            "操作内容：{{keyword1.DATA}}\n" +
                            "审核结果：{{keyword2.DATA}}\n" +
                            "原因说明：{{keyword3.DATA}}\n" +
                            "联系电话：{{keyword4.DATA}}\n" +
                            "{{remark.DATA}}");
        }
    };

    static WxTemplateMatcher wxTemplateMatcher = new WxTemplateMatcher(templates);
}
