package com.example.nya_shopping.config;


import com.example.nya_shopping.controller.error.CustomAccessDeniedHandler;
import com.example.nya_shopping.controller.error.CustomAuthenticationEntryPoint;
import com.example.nya_shopping.controller.error.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .formLogin(login -> login
                        .loginPage("/login")
                        .failureHandler(customAuthenticationFailureHandler)
                        .defaultSuccessUrl("/", true)
                        .usernameParameter("account")
                        .passwordParameter("password")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .clearAuthentication(true)
                )
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/", "/login", "/user/new", "/user/add", "/search", "/restaurant/**").permitAll()
                                .requestMatchers("/webjars/**", "/css/**", "/js/**", "/storage/**").permitAll()
                                .requestMatchers("/mypage/**", "/reservation/**", "/user/edit/**",  "/withdraw/**").hasRole("USER")
                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.POST, "/**").authenticated()
                                .anyRequest().authenticated()
                )

                .exceptionHandling(e -> e
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );

        return http.build();
    }

}
