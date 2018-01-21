[![Build Status](https://travis-ci.org/bingoohuang/template-matcher.svg?branch=master)](https://travis-ci.org/bingoohuang/template-matcher)
[![Coverage Status](https://coveralls.io/repos/github/bingoohuang/template-matcher/badge.svg?branch=master)](https://coveralls.io/github/bingoohuang/template-matcher?branch=master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.bingoohuang/template-matcher/badge.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.github.bingoohuang/template-matcher/)
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# template-matcher
template matcher for weixin and sms

## API
```java
val template1 = TemplateMatcher.parseTemplate("CANCEL1",
            "{{first.DATA}}\n" +
            "预约人：{{keyword1.DATA}}\n" +
            "预约项目：{{keyword2.DATA}}\n" +
            "预约时间：{{keyword3.DATA}}\n" +
            "取消原因：{{keyword4.DATA}}\n" +
            "{{remark.DATA}}", "{{", "}}");

val match1 = TemplateMatcher.match(template1,
            "您好，您有一项预约已取消\n" +
            "预约人：杨一凡\n" +
            "预约项目：张丽 阴瑜伽（小班课）\n" +
            "预约时间：2015-09-28  16:00-17:00\n" +
            "取消原因：会员临时有事");
```

## DEMO1
```
{{first.DATA}}
班级名称：{{keyword1.DATA}}
开班时间：{{keyword2.DATA}}
已报名人数：{{keyword3.DATA}}
{{remark.DATA}}
```
matches content
```
您好，贵馆有一门课程的报名人数未达最低要求：5人。
班级名称：阴瑜伽（小班课）
开班时间：2015-09-26  15:00-16:00
报名人数：3人
系统已自动取消了已订课会员的预约
```

## DEMO2

```
[#merchantname#]#character##username#（#mobile#）已成功预订#date#的#course#。
```
matches

```
[XX瑜伽]会员杨一凡（13789767897）已成功预订2月14日20:00-21:00的阴瑜伽（小班课）
```