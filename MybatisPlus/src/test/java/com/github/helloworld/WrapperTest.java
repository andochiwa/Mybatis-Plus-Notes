package com.github.helloworld;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.helloworld.bean.User;
import com.github.helloworld.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HAN
 * @version 1.0
 * @create 03-13-5:25
 */
@SpringBootTest
public class WrapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void test() {
        // 查询name不为空，且email不为空，且age大于12的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.isNotNull("name")
                .isNotNull("email")
                .ge("age", 12);

        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void test2() {
        // 查询名字等于ta2
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "ta2");
        System.out.println(userMapper.selectOne(wrapper));
    }

    @Test
    void test3() {
        // 查询20-30岁之间的用户数
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age", 20, 30);
        System.out.println(userMapper.selectCount(wrapper));
    }

    @Test
    void test4() {
        // 模糊查询，名字中不包含e的且后缀为si的
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // likeRight和likeLeft分别表示%在右边和左边
        wrapper.notLike("name", "e").likeLeft("name", "si");
        System.out.println(userMapper.selectList(wrapper));
    }

}
