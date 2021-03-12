package com.github.helloworld;

import com.github.helloworld.bean.User;
import com.github.helloworld.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HAN
 * @version 1.0
 * @create 03-13-4:59
 */
@SpringBootTest
public class OptimisticLockerTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void optimisticLockSuccess() {
        User user = userMapper.selectById(3);
        user.setName("wangwu");
        user.setEmail("wangwu@wang.com");
        userMapper.updateById(user);
    }

    @Test
    void optimisticLockFail() {
        User user1 = userMapper.selectById(2);
        user1.setName("ta");
        user1.setEmail("ta@wang.com");

        // 模拟另外一个线程进行插入操作
        User user2 = userMapper.selectById(2);
        user2.setName("ta2");
        user2.setEmail("ta2@wang.com");
        userMapper.updateById(user2);

        // 可以使用自旋锁来提交
        userMapper.updateById(user1);
    }

}
