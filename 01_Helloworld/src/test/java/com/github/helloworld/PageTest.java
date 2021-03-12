package com.github.helloworld;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.helloworld.bean.User;
import com.github.helloworld.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author HAN
 * @version 1.0
 * @create 03-13-4:51
 */
@SpringBootTest
public class PageTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void testPage() {
        // 参数1：当前页，参数2：页面大小（相当于limit (2, 5) 实际为limit 5 offset 5）
        Page<User> page = new Page<>(2, 5);
        userMapper.selectPage(page, null);

        page.getRecords().forEach(System.out::println);
    }

}
