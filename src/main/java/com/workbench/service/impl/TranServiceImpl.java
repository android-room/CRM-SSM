package com.workbench.service.impl;

import com.utils.DateTimeUtil;
import com.utils.UUIDUtil;
import com.vo.PaginationVo;
import com.workbench.dao.TranDao;
import com.workbench.dao.TranHistoryDao;
import com.workbench.domain.Tran;
import com.workbench.domain.TranHistory;
import com.workbench.service.TranService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {
    @Resource
    private TranDao tranDao;
    @Resource
    private TranHistoryDao tranHistoryDao;

    @Override
    public PaginationVo pageList(Map<String, Object> map) {
        int total = tranDao.getTotalByCondition(map);
        List<Tran> dataList = tranDao.getClueListByCondition(map);

        PaginationVo<Tran> vo = new PaginationVo<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public Tran detail(String id) {
        return tranDao.detail(id);
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;
        int count = tranDao.changeStage(t);
        if (count != 1){
            flag = false;
        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        int count1 = tranHistoryDao.save(th);
        if (count1 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public List<Map> get() {
        return tranDao.get();
    }
}
