package com;

import com.settings.dao.UserDao;
import com.settings.service.UserService;
import com.settings.service.impl.UserServiceImpl;
import com.workbench.dao.ActivityDao;
import com.workbench.domain.Clue;
import com.workbench.service.impl.ActivityServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class app {
    public static void main(String[] args) {
//       ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//        ActivityServiceImpl activityServiceImpl = (ActivityServiceImpl) ac.getBean("activityServiceImpl");
//        ActivityDao userdao = (ActivityDao) ac.getBean("activityDao");
//        System.out.println(activityServiceImpl.getClass().getName());
//        System.out.println(userdao.getClass().getName());
//        System.out.println(activityServiceImpl);
//        System.out.println(userdao);
        Object a = "20";
        Clue clue = new Clue();
        clue.setId((String) a);
    }
}
