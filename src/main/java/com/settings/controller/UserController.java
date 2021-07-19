package com.settings.controller;

import com.settings.domain.User;
import com.settings.service.impl.UserServiceImpl;
import com.utils.MD5Util;
import com.utils.UUIDUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Resource
    private UserServiceImpl userService = null;
    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/settings/user/login.do",method= RequestMethod.POST)
    @ResponseBody
    public Map login(String loginAct, String loginPwd, HttpServletRequest request){
        Map<String,Object> map = new HashMap();
//        ModelAndView mv = new ModelAndView();
        loginPwd = MD5Util.getMD5(loginPwd);
        try {
            User user = userService.login(loginAct, loginPwd);
            request.getSession().setAttribute("user",user);
            map.put("success",true);
//            mv.addObject("success",true);
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
//            mv.addObject("success",false);
//            mv.addObject("msg",msg);
            map.put("success",false);
            map.put("msg",msg);
        }
        return map;
    }
}
