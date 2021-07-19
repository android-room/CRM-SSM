package com.workbench.service;

import com.vo.PaginationVo;
import com.workbench.domain.Clue;
import com.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {
    boolean save(Clue clue);

    PaginationVo pageList(Map<String, Object> map);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bundBtn(String cid, String[] aid);

    boolean convert(String clueId, Tran t, String createBy);
}
