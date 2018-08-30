package com.example.shirotest;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import javax.sql.DataSource;

public class JdbcRealmTest
{
  private  DruidDataSource dataSource=new DruidDataSource();
    {
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/test1");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }
    @Test
    public void TestAuth()
    {
        //jdbcrealm需要访问数据库
        JdbcRealm jdbcRealm=new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true);//注意默认情况下是不会去查询我们的权限数据的，这里需要设置为true
        jdbcRealm.setAuthenticationQuery("select * from user where name=? limit 1");//提供我们自己的sql
        jdbcRealm.setUserRolesQuery("select role_name from user_roles where username = ?");
        jdbcRealm.setPermissionsQuery("select permission from roles_permissions where role_name = ?");
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager =new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);
        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject =SecurityUtils.getSubject(); //注意这里的subject引入的包是shiro的
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zhangsan","112312");
        subject.login(usernamePasswordToken);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());//shiro提供的是否认证的方法
        //    subject.checkPermission("user:dele2te");//Subject does not have permission [user:dele2te]
        subject.logout();
        System.out.println("isAuthenticated:"+subject.isAuthenticated());//shiro提供的是否认证的方法
    }
}
