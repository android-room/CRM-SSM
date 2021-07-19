package com.workbench.service.impl;

import com.utils.DateTimeUtil;
import com.utils.UUIDUtil;
import com.vo.PaginationVo;
import com.workbench.dao.*;
import com.workbench.domain.*;
import com.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    //线索相关
    @Resource
    private ClueDao clueDao = null;
    @Resource
    private ClueActivityRelationDao clueActivityRelationDao = null;
    @Resource
    private ClueRemarkDao clueRemarkDao = null;


    //客户相关
    @Resource
    private CustomerDao customerDao = null;
    @Resource
    private CustomerRemarkDao customerRemarkDao = null;

    //联系人相关
    @Resource
    private ContactsDao contactsDao = null;
    @Resource
    private ContactsRemarkDao contactsRemarkDao = null;
    @Resource
    private ContactsActivityRelationDao contactsActivityRelationDao = null;


    //交易相关
    @Resource
    private TranDao tranDao = null;
    @Resource
    private TranHistoryDao tranHistoryDao = null;



    @Override
    public boolean save(Clue clue) {
        boolean flag = true;
        int count = clueDao.save(clue);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVo pageList(Map<String, Object> map) {
        int total = clueDao.getTotalByCondition(map);
        List<Clue> dataList = clueDao.getClueListByCondition(map);

        PaginationVo<Clue> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public Clue detail(String id) {
        return clueDao.detail(id);
    }

    @Override
    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueDao.unbund(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bundBtn(String cid, String[] aid) {
        boolean flag = true;
        for (int i = 0; i < aid.length; i++) {
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid[i]);
            car.setClueId(cid);

            int count = clueActivityRelationDao.bundBtn(car);
            if (count != 1){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    @Transactional
    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        Clue c = clueDao.getById(clueId);
        String company = c.getCompany();

        Customer cus = customerDao.getCustomerByName(company);
        if (cus==null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setWebsite(c.getWebsite());
            cus.setPhone(c.getPhone());
            cus.setOwner(c.getOwner());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setName(company);
            cus.setDescription(c.getDescription());
            cus.setCreateTime(createTime);
            cus.setCreateBy(createBy);
            cus.setContactSummary(c.getContactSummary());
            //添加客户
            int count1 = customerDao.save(cus);
            if (count1!=1){
                flag = false;
            }
        }

        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAddress(c.getAddress());
        //添加联系人
        int count2 = contactsDao.save(con);
        if (count2!=1){
            flag = false;
        }

        //线索备注转换到客户和联系人
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList){
            //取出备注信息（转换）
            String noteContext = clueRemark.getNoteContent();
            //创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContext);
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3!=1){
                flag = false;
            }

            //创建联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(cus.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContext);
            int count4 = contactsRemarkDao.save(customerRemark);
            if (count4!=1){
                flag = false;
            }
        }

        //查询关联的市场活动
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            //取出市场活动的id
            String activityId = clueActivityRelation.getActivityId();
            //创建联系人和市场活动关系对象
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());
            //添加联系人和市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5!=1){
                flag = false;
            }
        }

        //创建交易
        if(t!=null){
            t.setSource(c.getSource());
            t.setOwner(c.getOwner());
            t.setDescription(c.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(c.getContactSummary());
            t.setContactsId(con.getId());
            //添加交易
            int count6 = tranDao.save(t);
            if (count6!=1){
                flag = false;
            }
            //生成交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            //添加交易历史
            int count7 = tranHistoryDao.save(tranHistory);
            if (count7!=1){
                flag = false;
            }
        }

        //删除线索备注
        for (ClueRemark clueRemark : clueRemarkList){
            int count8 = clueRemarkDao.delete(clueRemark);
            if (count8!=1){
                flag = false;
            }
        }

        //删除线索和市场活动
        for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if (count9!=1){
                flag = false;
            }
        }

        //删除线索
        int count10 = clueDao.delete(clueId);
        if (count10!=1){
            flag = false;
        }




        return flag;
    }
}
