package com.tuling.config.indb;

import com.tuling.config.TulingUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
* @vlog: 高于生活，源于生活
* @desc: 类的描述:安全配置
* @author: smlz
* @createDate: 2020/1/15 20:32
* @version: 1.0
*/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private TulingUserDetailService userDetailsService;

    /**
     * 方法实现说明:用于构建用户认证组件,需要传递userDetailsService和密码加密器
     * @author:smlz
     * @param auth
     * @return:
     * @exception:
     * @date:2019/12/25 14:31
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * 方法实现说明:安全配置
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/20 20:34
     */
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and()
                .authorizeRequests()
                .antMatchers("/user/getCurrentUser").permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable().cors();
    }

    /**
     * 方法实现说明:密码加密器
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/20 20:34
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 方法实现说明:用户密码认证模式的组件
     * @author:smlz
     * @return:
     * @exception:
     * @date:2020/1/20 20:33
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("smlz"));
    }

}

