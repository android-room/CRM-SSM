package com.workbench.controller;

import com.settings.domain.User;
import com.settings.service.UserService;
import com.utils.DateTimeUtil;
import com.vo.PaginationVo;
import com.workbench.domain.Tran;
import com.workbench.service.CustomerService;
import com.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TranController {

    @Autowired
    private TranService tranService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;

    @RequestMapping("workbench/transaction/add.do")
    public ModelAndView add(){
        ModelAndView mv=new ModelAndView();
        List<User> uList = userService.getUserList();
        mv.addObject("uList",uList);
        mv.setViewName("save.jsp");
        return mv;
    }

    @RequestMapping("workbench/transaction/getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){
        return customerService.getCustomerName(name);
    }

    @RequestMapping("workbench/transaction/pageList.do")
    @ResponseBody
    public PaginationVo<Tran> pageList(Integer pageNo,Integer pageSize){
        int skipCount = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);
        PaginationVo vo = tranService.pageList(map);
        return vo;
    }

    @RequestMapping("workbench/transaction/detail.do")
    public ModelAndView detail(String id, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();
        Tran t = tranService.detail(id);


        String stage = t.getStage();
//        ServletContext application = request.getServletContext().getAttribute("pMap");
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);



        mv.addObject("t",t);
        mv.setViewName("detail.jsp");
        return mv;
    }

    @RequestMapping("workbench/transaction/changeStage.do")
    @ResponseBody
    public Map<String, Object> changeStage(String id, String money, String stage, String expectedDate, HttpServletRequest request){

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        t.setEditTime(DateTimeUtil.getSysTime());

        boolean flag = tranService.changeStage(t);
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));
        Map<String,Object> map = new HashMap<>();

        map.put("success",flag);
        map.put("t",t);

        return map;
    }

    @RequestMapping("workbench/transaction/get.do")
    @ResponseBody
    public List get(){
        List<Map> list = tranService.get();
        System.out.println(list);
        return list;
    }
}

