package net.javaguides.mssql.Securirty;

import lombok.AllArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
public class JWTConfiguration  extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
        private final  TokenProvider tokenProvider;
        @Override
        public  void  configure(HttpSecurity httpBuilder) throws Exception{
        JWTFilter jwtFilter = new JWTFilter(tokenProvider);
        httpBuilder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        }

}
