package com.micheal.week8jwt.config;

import com.micheal.week8jwt.Util.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.micheal.week8jwt.serviceimpl.UserServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private UserServiceImpl userService;
    private JwtAuthenticationFilter authentication;

    @Autowired
    public WebSecurityConfig(@Lazy UserServiceImpl userService, JwtAuthenticationFilter authentication) {
        this.userService = userService;
        this.authentication = authentication;
    }

    @Bean//bcryptPasswordEncoder is enabled for spring security hashing/salting of user's password information
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //AuthenticationProvider(DAOAuthenticationProvider) is enabled to function as the "bouncer" in our application. Checking
    //password and User information credibility.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(username -> userService.loadUserByUsername(username));
        return daoAuthenticationProvider;
    }

    @Bean//Creating our authorisation security for providing the right authorisation process
    // from before "logging in" till after "logging out"
    public SecurityFilterChain httpSecurity (HttpSecurity httpSecurity) throws Exception {
         return httpSecurity
                 .csrf(AbstractHttpConfigurer::disable)
                 .authorizeHttpRequests(httpRequests->
                         httpRequests
                                 .requestMatchers("/api/v1/sign-up", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api/v1/google/**", "/v3/api-docs.yaml",
                                         "/api/v1/login","/api/save-post","/api/comment/create-comment/{postId}","/api/like/{postId}","/api/unlike/{postId}","api/get-comment/{content}").permitAll()
                                 .requestMatchers("/api/v1/index","/api/like/{postId}","/api/unlike-comment/{id}",
                                         "/api/delete-comment/{id}","/api/all-comment",
                                         "/api/find-comment-postId/{postId}","/api/edit-comment/{commentId}","/api/save-post","/api/all-post",
                                         "/api/search-post/{title}","/api/delete-post/{id}","/api/edit-post/{id}").authenticated())
                 .sessionManagement(sessionManagement->
                         sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                 .authenticationProvider(authenticationProvider())
                 .addFilterBefore(authentication, UsernamePasswordAuthenticationFilter.class)
                 .build();
    }

}
