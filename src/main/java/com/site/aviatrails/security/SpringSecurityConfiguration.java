package com.site.aviatrails.security;

import com.site.aviatrails.security.filter.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SpringSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final WebSecurity webSecurity;

    public SpringSecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, WebSecurity webSecurity) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.webSecurity = webSecurity;
    }

    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(AUTH_WHITELIST));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/user/{userId}")
                                .access(((authentication, context) ->
                                        new AuthorizationDecision(webSecurity.canAccessUser(authentication.get(), context.getRequest()))))
                                .requestMatchers(HttpMethod.POST, "/user").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/user").hasAnyRole("ADMIN", "USER", "MODERATOR")
                                .requestMatchers(HttpMethod.DELETE, "/user/delete/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/flights").permitAll()
                                .requestMatchers(HttpMethod.GET, "/flights/{id}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/flights/search").permitAll()
                                .requestMatchers(HttpMethod.GET, "/booking/allTickets").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/booking/user/{id}")
                                .access(((authentication, context) ->
                                        new AuthorizationDecision(webSecurity.canAccessUser(authentication.get(), context.getRequest()))))
                                .requestMatchers(HttpMethod.POST, "/booking").hasAnyRole("ADMIN", "USER", "MODERATOR")
                                .requestMatchers(HttpMethod.POST, "/booking/pay/{id}").hasAnyRole("ADMIN", "USER", "MODERATOR")
                                .requestMatchers(HttpMethod.POST, "/booking/refund/{id}").access(((authentication, context) ->
                                        new AuthorizationDecision(webSecurity.canAccessUser(authentication.get(), context.getRequest()))))
                                .requestMatchers(HttpMethod.POST, "/authentication").permitAll()
                                .requestMatchers(HttpMethod.POST, "/registration").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
