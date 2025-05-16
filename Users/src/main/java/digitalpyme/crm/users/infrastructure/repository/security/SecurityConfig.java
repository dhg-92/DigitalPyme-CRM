package digitalpyme.crm.users.infrastructure.repository.security;

import digitalpyme.crm.users.domain.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final UserService userService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserService userService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userService = userService;
    }

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/users/login").permitAll()
                    .requestMatchers("/users/restorePassword").permitAll()
                    .requestMatchers("/users/setPassword").permitAll()
                    .requestMatchers("/users/info").authenticated()
                    .requestMatchers("/users/**").hasAuthority("ROLE_ADMIN")
                    .anyRequest().permitAll()
                ).exceptionHandling(exception -> exception
                        .accessDeniedHandler(new ExceptionHandler.CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new ExceptionHandler.CustomAuthenticationEntryPoint())
                ).csrf(csrf -> csrf.disable());;

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService::loadUserByEmail);
        return authenticationManagerBuilder.build();
    }
}
