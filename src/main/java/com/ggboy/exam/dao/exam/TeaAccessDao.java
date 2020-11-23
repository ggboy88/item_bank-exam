package com.ggboy.exam.dao.exam;

import com.ggboy.exam.beans.exam.StuApplyResponse;
import com.ggboy.exam.beans.exam.TeaAccess;
import com.ggboy.exam.config.GeneralMapper;

import java.util.List;

public interface TeaAccessDao extends GeneralMapper<TeaAccess> {

    void refuseApply(String accessId);

    void accessApply(String accessId);
}
