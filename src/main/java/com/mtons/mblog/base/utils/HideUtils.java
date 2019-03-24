package com.mtons.mblog.base.utils;

import org.w3c.dom.Document;
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

public class HideUtils {
    /**
     * 渲染隐藏的内容 作废
     * @param content 帖子内容
     * @param canSee 是否可以查看隐藏内容
     * @return
     */
    public  static String RenderHide(String content, boolean canSee) {
        String regex = "&lt;hide(.)+?&lt;/hide&gt;";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            String hideStr = matcher.group();
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                StringReader read = new StringReader(hideStr);
                InputSource source = new InputSource(read);
                Document document = db.parse(source);
                NodeList nodeList = document.getElementsByTagName("hide");
                String nodeValue = nodeList.item(0).getTextContent();
                if (canSee) {
                    content = matcher.replaceFirst(nodeValue);
                } else {
                    content = matcher.replaceFirst("<b style=\"color: red;\">此处内容评论后显示</b>");
                }

                matcher = pattern.matcher(content);
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return content;
    }

}
