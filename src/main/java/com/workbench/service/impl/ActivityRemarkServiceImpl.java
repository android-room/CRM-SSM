package com.workbench.service.impl;

import com.workbench.dao.ActivityDao;
import com.workbench.dao.ActivityRemarkDao;
import com.workbench.domain.ActivityRemark;
import com.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkDao activityRemarkDao = null;
    public void setActivityRemarkDao(ActivityRemarkDao activityRemarkDao) {
        this.activityRemarkDao = activityRemarkDao;
    }


    @Override
    public List<ActivityRemark> getRemarkList(String id) {
        return activityRemarkDao.getRemarkList(id);
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        int count = activityRemarkDao.deleteRemark(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {

        boolean flag = true;
        int count = activityRemarkDao.saveRemark(activityRemark);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemarkBtn(ActivityRemark activityRemark) {
        boolean flag = true;
        int count = activityRemarkDao.updateRemarkBtn(activityRemark);
        if (count != 1){
            flag = false;
        }
        return flag;
    }
}
