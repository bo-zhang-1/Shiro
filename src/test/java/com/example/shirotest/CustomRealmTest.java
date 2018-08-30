package com.example.shirotest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest
{
    @Test
    public void TestAuth()
    {
        CustomRealm customRealm=new CustomRealm();
        //1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager =new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);
        //2.主体提交认证请求
        HashedCredentialsMatcher hashedCredentialsMatcher=new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//传一个加密名称
        hashedCredentialsMatcher.setHashIterations(2);//设置加密次数,这边的值得和加密的值一致
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject =SecurityUtils.getSubject(); //注意这里的subject引入的包是shiro的
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken("zhangsan","123456");
        subject.login(usernamePasswordToken);
        System.out.println("isAuthenticated:"+subject.isAuthenticated());//shiro提供的是否认证的方法
        subject.checkPermission("user:delete");//Subject does not have permission [user:dele2te]
       subject.checkRole("admin");
        subject.logout();
        System.out.println("isAuthenticated:"+subject.isAuthenticated());//shiro提供的是否认证的方法
    }
}
