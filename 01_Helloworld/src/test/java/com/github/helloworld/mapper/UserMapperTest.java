package com.github.helloworld.mapper;

import com.github.helloworld.bean.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author HAN
 * @version 1.0
 * @create 03-13-2:34
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void test() {
        // 参数是个wrapper，叫条件构造器，这里先不用
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    void insert() {
        User user = new User(null, "wangwu", 20, "zhangsan@g.com", null, null);
        userMapper.insert(user);
        System.out.println(user);
    }

    @Test
    void update() {
        User user = new User(4L, "lisi", 18, "lisi@g.com", null, null);
        userMapper.updateById(user);
    }

}