package com.workbench.controller;

import com.settings.domain.User;
import com.settings.service.impl.UserServiceImpl;
import com.utils.DateTimeUtil;
import com.utils.UUIDUtil;
import com.vo.PaginationVo;
import com.workbench.domain.Activity;
import com.workbench.domain.ActivityRemark;
import com.workbench.service.ActivityService;
import com.workbench.service.impl.ActivityRemarkServiceImpl;
import com.workbench.service.impl.ActivityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ActivityController {
    @Autowired
    private ActivityServiceImpl activityService = null;
    @Autowired
    private ActivityRemarkServiceImpl activityRemarkService = null;
    @Autowired
    private UserServiceImpl userService = null;



    @RequestMapping("workbench/activity/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        List<User> list = userService.getUserList();
        return list;
    }

    @RequestMapping("workbench/activity/save.do")
    @ResponseBody
    public boolean save(Activity activity, HttpServletRequest request){
        String id = UUIDUtil.getUUID();
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        activity.setId(id);
        activity.setCreateBy(createBy);
        activity.setCreateTime(createTime);
        System.out.println(activity);
        boolean flag = activityService.save(activity);
        return flag;
    }

    @RequestMapping("workbench/activity/pageList.do")
    @ResponseBody
    public PaginationVo<Activity> pageList(String name, String owner, String startDate, String endDate, Integer pageNo, Integer pageSize){
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);

        PaginationVo vo = activityService.pageList(map);

        return vo;
    }

    @RequestMapping("workbench/activity/delete.do")
    @ResponseBody
    public boolean delete(String id[]){
        boolean flag = activityService.delete(id);
        return flag;
    }

    @RequestMapping("workbench/activity/getUserListAndActivity.do")
    @ResponseBody
    public Map<String,Object> getUserListAndActivity(String id){

        Map<String,Object> map = activityService.getUserListAndActivity(id);

        return map;
    }

    @RequestMapping("workbench/activity/update.do")
    @ResponseBody
    public boolean update(Activity activity,HttpServletRequest request){
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        activity.setEditTime(DateTimeUtil.getSysTime());
        activity.setEditBy(editBy);
        boolean a = activityService.update(activity);
        return a;
    }

    @RequestMapping("workbench/activity/detail.do")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Activity a = activityService.detail(id);
        mv.addObject("a",a);
        mv.setViewName("/workbench/activity/detail.jsp");
        return mv;
    }

    @RequestMapping("workbench/activity/getRemarkList.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkList(String id){
        List<ActivityRemark> activityRemark = activityRemarkService.getRemarkList(id);
        return activityRemark;
    }

    @RequestMapping("workbench/activity/deleteRemark.do")
    @ResponseBody
    public boolean deleteRemark(String id){
        return activityRemarkService.deleteRemark(id);
    }

    @RequestMapping("workbench/activity/saveRemark.do")
    @ResponseBody
    public Map<String,Object> saveRemark(ActivityRemark activityRemark,HttpServletRequest request){
        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateBy(((User) request.getSession().getAttribute("user")).getName());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setEditFlag("0");
        System.out.println(activityRemark);
        boolean flag = activityRemarkService.saveRemark(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",activityRemark);
        return map;
    }

    @RequestMapping("workbench/activity/updateRemarkBtn.do")
    @ResponseBody
    public Map<String,Object> updateRemarkBtn(ActivityRemark activityRemark,HttpServletRequest request){
        activityRemark.setEditBy(((User) request.getSession().getAttribute("user")).getName());
        activityRemark.setEditFlag("1");
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        boolean flag = activityRemarkService.updateRemarkBtn(activityRemark);
        Map<String,Object> map = new HashMap<>();
        map.put("success",flag);
        map.put("ar",activityRemark);
        return map;
    }

}
