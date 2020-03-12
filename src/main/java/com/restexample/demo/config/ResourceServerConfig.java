package com.restexample.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
@Order(1)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "demo_rest_api";

//    private static final String RESOURCE_ID = "resource-server-ui";
    private static final String SECURED_FULL_SCOPE = "#oauth2.hasScope('full-access')";
    private static final String SECURED_PATTERN = "/api/**";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /*http.
                anonymous().disable()
                .requestMatchers().antMatchers("/user/**")
                .and().authorizeRequests()
                .antMatchers("/user/**").access("hasRole('ADMIN')")
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());*/

        /*http
                .requestMatchers()
                .antMatchers("/api/**").and()
                .authorizeRequests()
                .anyRequest().authenticated();*/


        http.csrf().ignoringAntMatchers(SECURED_PATTERN).and().requestMatchers()
                .antMatchers(SECURED_PATTERN)
                .and().csrf().disable()
                .authorizeRequests()
                .anyRequest()//.permitAll();
                .access(SECURED_FULL_SCOPE);
    }
}
