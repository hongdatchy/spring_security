/**
 * Copyright(C) 2022 Phạm Hồng Đạt
 * 02/10/2022
 */
package com.hongdatchy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 *
 *
 * @author hongdatchy
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
//                .antMatchers("/api/v1/ad/*").hasRole("Admin")
                .antMatchers(HttpMethod.GET,"/api/v1/ad/*").hasRole("Admin")
                .antMatchers(HttpMethod.POST,"/api/v1/ad/*").hasRole("Admin")
                .antMatchers(HttpMethod.PUT,"/api/v1/ad/*").hasRole("Admin")
                .antMatchers(HttpMethod.DELETE,"/api/v1/ad/*").hasAuthority("Admin-vip")
                .antMatchers("/api/v1/us/*").hasRole("User")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

//    @Override
    @Bean
    protected UserDetailsService userDetailsService(){
        UserDetails admin = User.builder()
                .username("hongdatchy")
                .password(passwordEncoder.encode("hongdat10"))
                .roles("Admin", "User")
                .build();
        UserDetails user = User.builder()
                .username("thuy")
                .password(passwordEncoder.encode("cute"))
                .roles("User")
                .build();
        UserDetails admin_vip = User.builder()
                .username("admin-vip")
                .password(passwordEncoder.encode("1"))
//                .roles("Admin", "User")
                .authorities("Admin-vip")
                .build();

        return new InMemoryUserDetailsManager(admin, user, admin_vip);

    }
}
