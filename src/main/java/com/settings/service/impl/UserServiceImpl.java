package com.settings.service.impl;

import com.exception.LoginException;
import com.settings.dao.UserDao;
import com.settings.domain.User;
import com.settings.service.UserService;
import com.utils.DateTimeUtil;
import com.workbench.domain.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao = null;
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(rollbackFor = LoginException.class)
    public User login(String loginAct, String loginPwd){
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userDao.login(map);
        if (user == null){
            throw new LoginException("账号密码不存在");
        }
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime)<0){
            throw new LoginException("账号以失效");
        }
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号以锁定");
        }
        return user;
    }


    public List<User> getUserList() {
        return userDao.getUserList();
    }

}
