package com.workbench.service;

import com.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityRemarkService {

    List<ActivityRemark> getRemarkList(String id);

    boolean deleteRemark(String id);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean updateRemarkBtn(ActivityRemark activityRemark);
}
