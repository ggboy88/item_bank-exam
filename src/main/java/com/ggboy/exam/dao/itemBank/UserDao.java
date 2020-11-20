package com.ggboy.exam.dao.itemBank;

import com.ggboy.exam.beans.itemBank.User;
import com.ggboy.exam.config.GeneralMapper;

public interface UserDao extends GeneralMapper<User> {

    String selectUserNameById(Integer id);

}
