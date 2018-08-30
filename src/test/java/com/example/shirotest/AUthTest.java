package com.example.shirotest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;


public class AUthTest {
   private SimpleAccountRealm simpleAccountRealm=new SimpleAccountRealm();
    @Before
    public void add()
    {
        simpleAccountRealm.addAccount("zhangsan","123456","admin","user");//再添加用户的时候我们给用户赋予一个adminq权限
    }

    @Test
    public void TestAuth()
    {
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager =new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);//将simpleAccountRealm设置到我们的环境当中来
        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject =SecurityUtils.getSubject(); //注意这里的subject引入的包是shiro的
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zhangsan","123456");
        subject.login(usernamePasswordToken);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());//shiro提供的是否认证的方法
        //需要在认证完成之后检查权限
      subject.checkRole("admin");
      subject.checkRoles("admin","user");
        subject.logout();
        System.out.println("isAuthenticated:"+subject.isAuthenticated());//shiro提供的是否认证的方法
    }
}