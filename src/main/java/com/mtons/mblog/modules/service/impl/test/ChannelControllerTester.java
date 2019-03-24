package com.mtons.mblog.modules.service.impl.test;

import com.mtons.mblog.base.utils.HideUtils;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChannelControllerTester {
    @Before
    public void testBefore(){
        System.out.println("testBefore");
    }

    @Test
    public void testHide(){
        String content = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>1.增加部分隐藏内容，回复后可见</p>\n" +
                "<p>2.修改编辑器，增加&ldquo;隐藏&rdquo;按钮（待办）</p>\n" +
                "<p>3.发布帖子时加上SEO关键字（待办）</p>\n" +
                "<p>4.站点地图（待办）</p>\n" +
                "<p>5.定时推送到搜索引擎（待办）</p>\n" +
                "<p>6.注册邀请码功能（待办）</p>\n" +
                "<p>7.网站美化（待办）</p>\n" +
                "<p>8.内容付费功能（待办）</p>\n" +
                "<p><hide>;测试隐藏内容，你能看到吗？</hide></p>\n" +
                "<p>&nbsp;</p>\n" +
                "</body>\n" +
                "</html>";
        content = HideUtils.RenderHide(content, true);
        System.out.println(content);
    }
}
