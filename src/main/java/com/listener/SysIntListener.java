package com.listener;

import com.settings.domain.DicValue;
import com.settings.service.DicService;
import com.settings.service.impl.DicServiceImpl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysIntListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("监听器");
//        WebApplicationContext ctx = null;
//        String key = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
//        Object attr = sce.getServletContext().getAttribute(key);
//        if (attr != null){
//            ctx = (WebApplicationContext)attr;
//        }
        ServletContext application = sce.getServletContext();
        DicService ds = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext()).getBean(DicServiceImpl.class);
        Map<String,List<DicValue>> map = ds.getAll();
        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }

        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }
}
