# Mybatis-plus Study notes

# 配置日志

我们写的所有的sql已经由Mybatis-plus管理，是不可见的，我们希望知道它是如何执行的，所以必须看日志

```yaml
# 配置日志
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

配置完毕后，就可以查看日志了



## CRUD扩展

* **主键生成策略**
  * 雪花算法
    * nowflake是Twitter开源的分布式ID生成算法，结果是一个long型的ID。其核心思想是：使用41bit作为毫秒数，10bit作为机器的ID（5个bit是数据中心，5个bit的机器ID），12bit作为毫秒内的流水号（意味着每个节点在每毫秒可以产生 4096 个 ID），最后还有一个符号位，永远是0。snowflake算法可以根据自身项目的需要进行一定的修改。比如估算未来的数据中心个数，每个数据中心的机器数以及统一毫秒可以能的并发数来调整在算法中所需要的bit数
  * 使用@TableId注解设置id生成策略，具体设置可以看IdType类



# 自动填充

创建时间，修改时间。这些操作都是自动化完成，我们不希望手动更新

阿里巴巴开发手册：所有数据库表：gmt_create, gmt_modified几乎所有的表都要配置上，而且需要自动化

> 方式一：数据库级别（实际工程中不允许修改数据库）

1. 在表中新增字段 create_time, update_time，并且**设置默认值以及更新约束**
2. 再次测试插入方法

> 方式二：代码级别

1. 删除数据库的默认值

2. 实体类字段属性上增加注解

   ```java
   @TableField(fill = FieldFill.INSERT)
   private LocalDateTime createTime;
   @TableField(fill = FieldFill.INSERT_UPDATE)
   private LocalDateTime updateTime;
   ```

3. 编写处理器来处理注解即可（不要忘记把处理器加入到ioc容器中）

   ```java
   @Component
   public class MetaObjectHandler implements com.baomidou.mybatisplus.core.handlers.MetaObjectHandler {
       // 插入时的填充策略
       @Override
       public void insertFill(MetaObject metaObject) {
           this.strictInsertFill(metaObject, "createTime", 
                                 LocalDateTime.class, LocalDateTime.now());
           this.strictInsertFill(metaObject, "updateTime", 
                                 LocalDateTime.class, LocalDateTime.now());
       }
   
       // 更新时的填充策略
       @Override
       public void updateFill(MetaObject metaObject) {
           this.strictInsertFill(metaObject, "updateTime", 
                                 LocalDateTime.class, LocalDateTime.now());
       }
   }
   ```



# 乐观锁

> 乐观锁，顾名思义十分乐观，认为总是不会出问题，所以不会上锁，在最后更新时检测是否有被其他线程更新过
>
> 悲观锁，十分悲观，上锁

实现方式

1. 取出记录时，获取version

2. 更新时，带上这个version

3. 执行更新时

   ```sql
   set version = new_version where version = old_version
   ```

4. 如果发现version不对，则更新失败



mybatis-plus自带乐观锁插件，使用方法

1. 给数据库增加version字段

2. 给实体类增加对应字段

   ```java
   @Version // 乐观锁version注解
   private Integer version;
   ```

3. 注册组件

   ```java
   @Configuration
   public class MybatisPlusConfig {
       // 注册乐观锁插件
       @Bean
       public MybatisPlusInterceptor mybatisPlusInterceptor() {
           MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
           interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
           return interceptor;
       }
   }
   ```

   

# 分页查询

分页在网站经常使用，可以有以下几种方式

1. 原始的limit进行分页
2. pageHelper第三方插件
3. mybatis-plus内置了分页插件

```java
interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
```

```java
// 参数1：当前页，参数2：页面大小（相当于limit (2, 5) 实际为limit 5 offset 5）
Page<User> page = new Page<>(2, 5);
userMapper.selectPage(page, null);
```

# 逻辑删除

> 物理删除：从数据库直接移除
>
> 逻辑移除：在数据库中没有移除，而是添加一个字段记录他被移除

使用方法

1. 在数据库中添加字段deleted

2. 在实体类中添加属性以及注解 即可

   ```java
   @TableLogic
   private Integer deleted;
   ```



# 条件构造器(*)

写一些复杂的sql就可以用它来替代

编写wrapper接口的实现类来进行复杂查询

```java
void test() {
    // 查询name不为空，且email不为空，且age大于12的用户
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.isNotNull("name")
            .isNotNull("email")
            .ge("age", 12);

    userMapper.selectList(wrapper).forEach(System.out::println);
}
```

```java
void test4() {
    // 模糊查询，名字中不包含e的且后缀为si的
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    // likeRight和likeLeft分别表示%在右边和左边
    wrapper.notLike("name", "e").likeLeft("name", "si");
    System.out.println(userMapper.selectList(wrapper));
}
```