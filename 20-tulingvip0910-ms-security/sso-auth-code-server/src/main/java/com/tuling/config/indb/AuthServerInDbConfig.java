package com.tuling.config.indb;

import com.tuling.config.TulingUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * Created by smlz on 2019/12/25.
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerInDbConfig extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TulingUserDetailService userDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 方法实现说明: 认证服务器颁发的token的存储方式  内存|db|redis|jwt
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:17
     */
    @Bean
    public TokenStore tokenStore(){
        //return new JdbcTokenStore(dataSource);
        return new RedisTokenStore(redisConnectionFactory);
    }



    /**
     * 方法实现说明:认证服务器能够给哪些 客户端颁发token  我们需要把客户端的配置 存储到
     * 数据库中 可以基于内存存储和db存储
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:18
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }

    /**
     * 方法实现说明:用于查找我们第三方客户端的组件 主要用于查找 数据库表 oauth_client_details
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:19
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 方法实现说明:授权服务器的配置的配置
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:21
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.tokenStore(tokenStore()) //授权服务器颁发的token 怎么存储的
                .userDetailsService(userDetailsService) //用户来获取token的时候需要 进行账号密码
                .authenticationManager(authenticationManager);
        //用户通过accessToken来获取用户信息的
        //endpoints.tokenServices(defaultTokenServices());

    }

    /**
     * 认证服务器和资源服务器分离(就是把用户微服务作为资源服务器的话 看下面这篇文章)
     * https://stackoverflow.com/questions/35056169/how-to-get-custom-user-info-from-oauth2-authorization-server-user-endpoint/35092561
     * @return
     */
/*    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(clientDetails());
        tokenServices.setAccessTokenValiditySeconds(60*60*12); // token有效期自定义设置，默认12小时
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);//默认30天，这里修改
        return tokenServices;
    }*/

    /**
     * 方法实现说明:授权服务器安全配置
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/15 20:23
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //第三方客户端校验token需要带入 clientId 和clientSecret来校验
        security .checkTokenAccess("isAuthenticated()");
        security.allowFormAuthenticationForClients();
    }

}
