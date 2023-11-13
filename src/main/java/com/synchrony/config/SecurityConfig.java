package com.synchrony.config;

import com.synchrony.filter.UserAuthenticationFilter;
import com.synchrony.security.SynchronyUserAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SynchronyUserAuthenticationProvider userAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new SynchronyUserAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
        // For H2 Console, set X-Frame-Options to DENY
        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public Filter authenticationFilter(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return new UserAuthenticationFilter(authenticationManagerBean(authenticationConfiguration));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}