package net.javaguides.mssql.Securirty;


import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import net.javaguides.mssql.Enum.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import lombok.*;
import org.springframework.security.config.annotation.web.AbstractRequestMatcherRegistry;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration  {
    private TokenProvider tokenProvider;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.web.filter.CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        System.out.println("CorsFilter");

        return new org.springframework.web.filter.CorsFilter(source);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf()
                .disable()
                .addFilterBefore(corsFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/employees/**")).hasAnyAuthority(Role.ROLE_EMPLOYEE.name(),Role.ROLE_MANAGER.name())
                .requestMatchers(new AntPathRequestMatcher("/api/v1/manager/**")).hasAnyAuthority(Role.ROLE_MANAGER.name())
                .requestMatchers(new AntPathRequestMatcher("/api/v1/login")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/api/v1/add-employee")).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .apply(new JWTConfiguration(tokenProvider));

        return  httpSecurity.build();


    }
}
