package com.example.shirotest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class IniRealmTest
{
    @Test
    public void TestAuth()
    {
        IniRealm iniRealm =new IniRealm("classpath:user.ini");
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager =new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);
        //2.主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject =SecurityUtils.getSubject(); //注意这里的subject引入的包是shiro的
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zhangsan","123456");
        subject.login(usernamePasswordToken);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());//shiro提供的是否认证的方法
        subject.checkRole("admin");
        subject.checkPermissions("user:update","user:dele2te");//Subject does not have permission [user:dele2te]
    //    subject.checkPermission("user:dele2te");//Subject does not have permission [user:dele2te]
        subject.logout();
        System.out.println("isAuthenticated:"+subject.isAuthenticated());//shiro提供的是否认证的方法
    }
}
