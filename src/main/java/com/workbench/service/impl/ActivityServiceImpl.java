package com.workbench.service.impl;

import com.settings.dao.UserDao;
import com.settings.domain.User;
import com.vo.PaginationVo;
import com.workbench.dao.ActivityDao;
import com.workbench.dao.ActivityRemarkDao;
import com.workbench.domain.Activity;
import com.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityDao activityDao = null;
    @Resource
    private ActivityRemarkDao activityRemarkDao = null;
    @Resource
    private UserDao userDao = null;

    public void setActivityRemarkDao(ActivityRemarkDao activityRemarkDao) {
        this.activityRemarkDao = activityRemarkDao;
    }
    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }


    @Override
    public boolean save(Activity activity) {
        boolean flag = true;
        int count = activityDao.save(activity);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVo pageList(Map<String, Object> map) {
        int total = activityDao.getTotalByCondition(map);

        List<Activity> dataList = activityDao.getActivityListByCondition(map);

        PaginationVo<Activity> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        return vo;
    }


    @Override
    @Transactional
    public boolean delete(String[] id) {
        boolean flag = true;
        //查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByAids(id);
        int count2 = activityRemarkDao.deleteByAids(id);

        if (count1!=count2){
            flag = false;
        }

        int count3 = activityDao.delete(id);
        if (count3!=id.length){
            flag = false;
        }
//        System.out.println(3/0);

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        List<User> list = userDao.getUserList();
        Activity a = activityDao.getById(id);

        Map<String,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("a",a);
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;
        int count = activityDao.update(activity);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity a = activityDao.detail(id);
        return a;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> list = activityDao.getActivityListByClueId(clueId);
        return list;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {
        List<Activity> list = activityDao.getActivityListByNameAndNotByClueId(map);
        return list;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        return activityDao.getActivityListByName(aname);
    }
}
