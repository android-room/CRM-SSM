package com.workbench.controller;

import com.settings.domain.User;
import com.settings.service.UserService;
import com.settings.service.impl.UserServiceImpl;
import com.utils.DateTimeUtil;
import com.utils.UUIDUtil;
import com.vo.PaginationVo;
import com.workbench.domain.Activity;
import com.workbench.domain.Clue;
import com.workbench.domain.Tran;
import com.workbench.service.ActivityService;
import com.workbench.service.ClueService;
import com.workbench.service.impl.ActivityServiceImpl;
import com.workbench.service.impl.ClueServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {
    @Autowired
    private ActivityService activityService = null;
    @Autowired
    private ClueService clueService = null;
    @Autowired
    private UserService userService = null;

    @RequestMapping("workbench/clue/getUserList.do")
    @ResponseBody
    public List<User> addBtn(){
        return userService.getUserList();
    }

    @RequestMapping("workbench/clue/save.do")
    @ResponseBody
    public boolean save(Clue clue, HttpServletRequest request){
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(((User) request.getSession().getAttribute("user")).getName());
        boolean flag = clueService.save(clue);
        return flag;
    }

    @RequestMapping("workbench/clue/pageList.do")
    @ResponseBody
    public PaginationVo<Clue> pageList(Integer pageNo,Integer pageSize,String fullname,String company,String phone,String source,String owner,String mphone,String state){
        int skipCount = (pageNo-1)*pageSize;

        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        PaginationVo vo = clueService.pageList(map);
        return vo;
    }

    @RequestMapping("workbench/clue/detail.do")
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue c = clueService.detail(id);
        mv.addObject("c",c);
        mv.setViewName("/workbench/clue/detail.jsp");

        return mv;
    }

    @RequestMapping("workbench/clue/getActivityListByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByClueId(String clueId){
        List<Activity> list = activityService.getActivityListByClueId(clueId);
        return list;
    }

    @ResponseBody
    @RequestMapping("workbench/clue/unbund.do")
    public boolean unbund(String id){
        boolean flag = clueService.unbund(id);
        return flag;
    }

    @RequestMapping("workbench/clue/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public List<Activity> getActivityListByNameAndNotByClueId(String aname,String clueId){
        Map<String,String> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);
        List<Activity> list = activityService.getActivityListByNameAndNotByClueId(map);
        return list;
    }

    @RequestMapping("workbench/clue/bundBtn.do")
    @ResponseBody
    public boolean bundBtn(String cid,String[] aid){
        boolean flag = clueService.bundBtn(cid,aid);
        return flag;
    }

    @RequestMapping("workbench/clue/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String aname){
        return activityService.getActivityListByName(aname);
    }

    @RequestMapping("workbench/clue/convert.do")
    public ModelAndView convert(String clueId,String flag,HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        Tran t = null;
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        if ("a".equals(flag)){
            //接收到数据
            t = new Tran();
            String id = UUIDUtil.getUUID();
            String activityId = request.getParameter("activityId");
            String createTime= DateTimeUtil.getSysTime();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");

            t.setId(id);
            t.setActivityId(activityId);
            t.setCreateTime(createTime);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
        }

        boolean flag1 = clueService.convert(clueId,t,createBy);
        if (flag1){
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }
        mv.addObject(flag1);
        return mv;
    }
}
