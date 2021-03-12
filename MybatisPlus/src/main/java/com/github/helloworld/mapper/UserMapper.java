package com.github.helloworld.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.helloworld.bean.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author HAN
 * @version 1.0
 * @create 03-13-2:31
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
