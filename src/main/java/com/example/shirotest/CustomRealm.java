package com.example.shirotest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.*;

public class CustomRealm extends AuthorizingRealm {

 private    Map<String,String> userMap=new HashMap<>(16) ;
    {
        userMap.put("zhangsan","e8b6ba86bafab35cfdf900a5402ba046");
        super.setName("CustomRealm");//设置我们访问的realm名称（这里取什么名称无所谓）
    }
    //这个是用来做授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username=(String)principals.getPrimaryPrincipal();//从我们的认证信息中获得用户名
        System.out.println(username);
        //从数据库和缓存中获取角色数据
        Set<String> roles=GetRolesByUserName(username);
        //通过用户名来获得权限数据
        Set<String> premission=GetPremissionsByUserName(username);
        //将我们去到的角色数据和权限数据返回
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(premission);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }
    //这个是用来做认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.从主体传过来的认证信息中获得用户名
        String userName = (String) token.getPrincipal();
        //2.通过用户名去到数据库中获取凭证
        String password=GetPasswordByUserName(userName);
        if(password==null) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo("zhangsan",password,getName());//最后一个参数是Realm
        //加盐
        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("zhang"));
        return simpleAuthenticationInfo;
    }

    /**
     * 这里是模拟数据库访问
     * @param userName 当前登录用户名
     * @return  当前用户所对应的密码
     */
    private String GetPasswordByUserName(String userName)
    {
        return userMap.get(userName);
    }

    /**
     * 获得角色数据
     * @param username 用户
     * @return  密码
     */
    private Set<String> GetRolesByUserName(String username) {
        Set<String> sets=new HashSet<>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }

    /**
     * 模拟数据库
     * @param username 用户
     * @return   密码
     */
    private Set<String> GetPremissionsByUserName(String username)
    {
        Set<String> sets=new HashSet<>();
        sets.add("user:delete");
        sets.add("user:update");
        return sets;
    }

    public static void main(String[] args)
    {
        Md5Hash md5Hash=new Md5Hash("123456","zhang",2);
        System.out.println(md5Hash.toString());
    }
}
