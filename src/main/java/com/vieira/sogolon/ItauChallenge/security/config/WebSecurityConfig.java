package com.vieira.sogolon.ItauChallenge.security.config;

import com.vieira.sogolon.ItauChallenge.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable();
        http
                .sessionManagement()
                .sessionCreationPolicy(STATELESS);
        http
                .authorizeRequests()
                .antMatchers( "/registration/**")
                .permitAll();
        http
                .authorizeRequests()
                .antMatchers(GET,  "/movies/**")
                .hasAnyAuthority("READER", "BASIC", "ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST,  "/api/critic/rate")
                .hasAnyAuthority("READER", "BASIC", "ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/critic/comment")
                .hasAnyAuthority("BASIC", "ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/critic/comment/response")
                .hasAnyAuthority("BASIC", "ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/critic/comment/tag")
                .hasAnyAuthority("ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/critic/comment/like")
                .hasAnyAuthority("ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/critic/comment/dislike")
                .hasAnyAuthority("ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/critic/comment/response/like")
                .hasAnyAuthority("ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/critic/comment/response/dislike")
                .hasAnyAuthority("ADVANCED", "MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(DELETE, "/api/moderator/comment")
                .hasAnyAuthority("MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(DELETE, "/api/moderator/comment/response")
                .hasAnyAuthority("MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(DELETE, "/api/moderator/comment/tag")
                .hasAnyAuthority("MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/moderator/comment/repeated")
                .hasAnyAuthority("MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(POST, "/api/moderator/comment/response/repeated")
                .hasAnyAuthority("MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(PATCH, "/api/moderator/user/**")
                .hasAnyAuthority("MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(GET, "/api/user/**")
                .hasAnyAuthority("MODERATOR");
        http
                .authorizeRequests()
                .antMatchers(GET, "/api/users")
                .hasAnyAuthority("MODERATOR");
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated();
        http
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*");
            }
        };
    }

}
