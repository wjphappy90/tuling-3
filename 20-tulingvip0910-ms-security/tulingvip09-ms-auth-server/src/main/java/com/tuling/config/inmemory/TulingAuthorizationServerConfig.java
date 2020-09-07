package com.tuling.config.inmemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

/**
 * 授权中心的配置
 * Created by smlz on 2019/12/20.
 */
@Configuration
@EnableAuthorizationServer
public class TulingAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 方法实现说明:认证服务器 需要知道 是哪个用户来访问资源服务器，所以该方法是用来
     * 验证用户信息的
     * @author:smlz
     * @param endpoints 认证服务器的 识别用户信息的配置
     * @return:
     * @exception:
     * @date:2020/3/7 16:09
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //这这里，认证服务器委托一个AuthenticationManager 来验证我们的用户信息
        //这里的authenticationManager 怎么来的 ？
        endpoints.authenticationManager(authenticationManager);

    }

    /**
     * 方法实现说明:作为授权服务器,必须知道有哪些第三方app 来向我们授权服务器
     * 索取令牌,所以这个方法就是配置 哪些第三方app可以来获取我们的令牌的.
     * @author:smlz
     * @param clients 第三方详情信息
     * @return:
     * @exception:
     * @date:2020/3/7 16:02
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        /**
         * 配置解析 授权服务器指定客户端(第三方应用)能访问授权服务器
         * 为第三方应用颁发客户端 id为  ,密码为smlz
         * 支持的授权类型为 密码模式(有四种模式,后面说)
         * 颁发的令牌的有效期为1小时
         * 通过该令牌可以访问 哪些资源服务器(order-service) 可以配置多个
         * 访问资源服务器的read write权限
         */

        clients.inMemory()
                    .withClient("portal_app")
                    .secret(passwordEncoder.encode("portal_app"))
                    .authorizedGrantTypes("password","authorization_code")
                    .scopes("read")
                    .accessTokenValiditySeconds(3600)
                    .resourceIds("order-service","product-service")
                    .redirectUris("http://www.baidu.com")
                .and()
                    .withClient("order_app")
                    .secret(passwordEncoder.encode("smlz"))
                    .accessTokenValiditySeconds(1800)
                    .scopes("read")
                    .authorizedGrantTypes("password")
                    .resourceIds("order-service")
                .and()
                    .withClient("product_app")
                    .secret(passwordEncoder.encode("smlz"))
                    .accessTokenValiditySeconds(1800)
                    .scopes("read")
                    .authorizedGrantTypes("password")
                    .resourceIds("product-service");
    }


    /**
     * 表示的资源服务器 校验token的时候需要干什么(这里表示需要带入自己appid,和app secret)来进行验证
     * @param security
     * @throws Exception
     */
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //获取tokenkey需要登陆
        security.checkTokenAccess("isAuthenticated()");
    }

}
