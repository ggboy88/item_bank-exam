package com.ggboy.exam.service;

import com.ggboy.exam.beans.exam.StuRegisterParam;
import com.ggboy.exam.common.ResultResponse;

public interface AuthService {

    ResultResponse teaLogin(String username,String password);

    ResultResponse stuLogin(String username,String password);

    ResultResponse stuRegister(StuRegisterParam stuRegisterParam);

}
