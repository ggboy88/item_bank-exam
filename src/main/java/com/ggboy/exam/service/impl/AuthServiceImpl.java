package com.ggboy.exam.service.impl;

import com.ggboy.exam.beans.exam.StuInfo;
import com.ggboy.exam.beans.exam.StuRegisterParam;
import com.ggboy.exam.beans.exam.StuTeaCourseLink;
import com.ggboy.exam.beans.itemBank.User;
import com.ggboy.exam.common.ResultEnum;
import com.ggboy.exam.common.ResultResponse;
import com.ggboy.exam.dao.exam.StuDao;
import com.ggboy.exam.dao.exam.StuTeaCourseLinkDao;
import com.ggboy.exam.dao.itemBank.UserDao;
import com.ggboy.exam.service.AuthService;
import com.ggboy.exam.utils.AuthUtils;
import com.ggboy.exam.utils.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserDao userDao;

    @Resource
    private AuthUtils authUtils;

    @Resource
    private StuDao stuDao;

    @Resource
    private StuTeaCourseLinkDao stuTeaCourseLinkDao;

    @Override
    public ResultResponse teaLogin(String username, String password) {

        Example example = Example.builder(User.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("username", username)
                        .andEqualTo("password",password))
                .build();

        User user = userDao.selectOneByExample(example);

        if (user == null || user.getFlag() != 1){
            return ResultResponse.fail(ResultEnum.LOGIN_FAIL);
        }
        String token = authUtils.getAuthToken(String.valueOf(user.getId()),username, password);
        Map<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("key",user.getId());
        return ResultResponse.success(map);
    }

    @Override
    public ResultResponse stuLogin(String username, String password) {

        Example example = Example.builder(StuInfo.class)
                .andWhere(Sqls.custom().andEqualTo("username", username)).build();

        StuInfo stuInfo = stuDao.selectOneByExample(example);
        String s;
        if (stuInfo != null){
             s = MD5Util.MD5(password, stuInfo.getSalt());
        }else {
            return ResultResponse.fail("用户名或密码错误");
        }

        assert s != null;
        if (!s.equals(stuInfo.getPassword())){
            return ResultResponse.fail("密码错误");
        }
        if (stuInfo.getStatus() != 1){
            return ResultResponse.fail(ResultEnum.LOGIN_FAIL);
        }
        String authToken = authUtils.getAuthToken(stuInfo.getId(),username, password);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",authToken);
        return ResultResponse.success(map);
    }

    @Override
    @Transactional
    public ResultResponse stuRegister(StuRegisterParam stuRegisterParam) {
        StuInfo stuInfo = stuRegisterParam.getStuInfo();

        //检测手机号是否重复
        StuInfo stuInfo2 = new StuInfo();
        stuInfo2.setId(null);
        stuInfo2.setPhone(stuInfo.getPhone());
        StuInfo stuInfo1 = stuDao.selectOne(stuInfo2);
        if (stuInfo1 != null){
            return ResultResponse.fail(ResultEnum.TELEPHONE_REPEAT);
        }

        //检测用户名是否重复
        stuInfo2.setPhone(null);
        stuInfo2.setUsername(stuInfo.getUsername());
        StuInfo stuInfo3 = stuDao.selectOne(stuInfo2);
        if (stuInfo3 != null){
            return ResultResponse.fail(ResultEnum.USERNAME_REPEAT);
        }

        //对密码进行加密，再加密加盐
        String salt = MD5Util.randomNum();
        String s = MD5Util.MD5(MD5Util.MD5(stuInfo.getPassword()),salt);
        stuInfo.setPassword(s);
        stuInfo.setSalt(salt);
        try {
            stuDao.insert(stuInfo);
            stuTeaCourseLinkDao.insert(
                    new StuTeaCourseLink(stuInfo.getId(),
                            stuRegisterParam.getCourseId(),
                            stuRegisterParam.getTeaId()));
        }catch (Exception e){
            e.printStackTrace();
            return ResultResponse.fail(ResultEnum.SYSTEM_ERROR);
        }
        return ResultResponse.success();
    }

}
