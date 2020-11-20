package com.ggboy.exam.config;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface GeneralMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
