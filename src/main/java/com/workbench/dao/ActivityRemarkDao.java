package com.workbench.dao;

import com.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityRemarkDao {
    int getCountByAids(String[] id);

    int deleteByAids(String[] id);

    List<ActivityRemark> getRemarkList(String id);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemarkBtn(ActivityRemark activityRemark);
}
