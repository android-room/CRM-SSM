package com.settings.service.impl;

import com.settings.dao.DicTypeDao;
import com.settings.dao.DicValueDao;
import com.settings.domain.DicType;
import com.settings.domain.DicValue;
import com.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {
    @Resource
    private DicTypeDao dicTypeDao;
    @Resource
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<>();

        List<DicType> list = dicTypeDao.getTypeList();
        for (DicType dt:list){
            String code = dt.getCode();
            List<DicValue> dvList = dicValueDao.getListByCode(code);
            map.put(code,dvList);
        }

        return map;
    }
}
