package com.bizblog.modules.hook.interceptor.impl;

import com.bizblog.modules.service.CommentService;
import com.bizblog.modules.data.AccountProfile;
import com.bizblog.modules.data.PostVO;
import com.bizblog.modules.hook.interceptor.InterceptorHookSupport;
import com.bizblog.web.controller.site.ChannelController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Beldon
 */
@Component
public class HidenContentPugin extends InterceptorHookSupport {
    @Autowired
    private CommentService commentService;

    private static final String SHOW = "<blockquote style=\"color: red;\">隐藏内容，请<a href=\"#chat_text\">回复</a>后刷新查看</blockquote>";

    @Override
    public String[] getInterceptor() {
        //说明要拦截的controller
        return new String[]{ChannelController.class.getName()};
    }

    @Override
    public void preHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView) throws Exception {
        PostVO ret = (PostVO) modelAndView.getModelMap().get("view");
        Object editing = modelAndView.getModel().get("editing");
        if (null == editing && ret != null) {
            PostVO post = new PostVO();
            BeanUtils.copyProperties(ret, post);
            String content = post.getContent();
            //要考虑属性
            content = content.replaceAll("&lt;hide&gt;", "<hide>");
            content = content.replaceAll("&lt;/hide&gt;", "</hide>");
            post.setContent(RenderHide(content, !check(ret.getId(), ret.getAuthor().getId())));
            modelAndView.getModelMap().put("view", post);
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws Exception {

    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws Exception {

    }

    private String replace(String content) {
        String c = content.replaceAll("<hide>([\\s\\S]*)</hide>", SHOW);
        c = c.replaceAll("&lt;hide&gt;([\\s\\S]*)&lt;/hide&gt;", SHOW);
        return c;
    }


    /**
     * 渲染隐藏的内容 作废
     * @param content 帖子内容
     * @param canSee 是否可以查看隐藏内容
     * @return
     */
    private String RenderHide(String content, boolean canSee) {
        String regex = "<hide>(.)+?</hide>";
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
                    content = matcher.replaceFirst(SHOW);
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

    private boolean check(long id, long userId) {
        //伪静态化获取不到权限
        Subject subject = SecurityUtils.getSubject();
        AccountProfile profile = (AccountProfile) subject.getPrincipal();
        if (profile != null) {
            if (profile.getId() == userId) {
                return false;
            }
            return commentService.countByAuthorIdAndPostId(profile.getId(), id) <= 0;
        }
        return true;
    }
}
