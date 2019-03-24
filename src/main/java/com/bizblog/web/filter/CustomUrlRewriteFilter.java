package com.bizblog.web.filter;

import com.bizblog.config.ShiroConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.servlet.DispatcherType;
import java.util.*;

@Configuration
@AutoConfigureOrder(10)
public class CustomUrlRewriteFilter {
    @Bean
    public FilterRegistrationBean urlRewrite(){
        UrlRewriteFilter rewriteFilter=new UrlRewriteFilter();
        FilterRegistrationBean registration = new FilterRegistrationBean(rewriteFilter);
        registration.setUrlPatterns(Arrays.asList("/*"));
        registration.setDispatcherTypes(DispatcherType.ASYNC, DispatcherType.ERROR, DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.REQUEST);
        Map initParam=new HashMap();
        //打包jar没办法加载/resources/urlrewrite.xml，如需要使用这种方法，需重写UrlRewriteFilter的loadUrlRewriterLocal方法,改变他加载配置文件的方式
        initParam.put("confPath","urlrewrite.xml");
        initParam.put("infoLevel","INFO");
        registration.setInitParameters(initParam);
        return  registration;
    }
}