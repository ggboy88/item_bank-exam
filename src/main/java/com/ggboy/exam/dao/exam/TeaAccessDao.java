package com.ggboy.exam.dao.exam;

import com.ggboy.exam.beans.exam.TeaAccess;
import com.ggboy.exam.config.GeneralMapper;

public interface TeaAccessDao extends GeneralMapper<TeaAccess> {

    void refuseApply(String accessId);

    void accessApply(String accessId);

}
