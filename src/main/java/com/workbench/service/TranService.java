package com.workbench.service;

import com.vo.PaginationVo;
import com.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {
    PaginationVo pageList(Map<String, Object> map);

    Tran detail(String id);

    boolean changeStage(Tran t);

    List<Map> get();
}
