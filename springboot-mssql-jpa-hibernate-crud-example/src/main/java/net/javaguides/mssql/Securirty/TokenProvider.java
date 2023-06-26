package net.javaguides.mssql.Securirty;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.javaguides.mssql.Enum.Token_Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.Authenticator;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    @Value("${secret.key}")
    private String secretKey;

    @Value("${access.token.expired}")
    private Integer accessTokenExpireTime;

    @Value("${refresh.token.expired}")
    private Integer refreshTokenExpireTime;


    public String createAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Map<String, Object> claims = new HashMap<>();
        claims.put(SecurityConstant.TOKEN_TYPE, (String)Token_Type.ACCESS_TOKEN.toString());
        claims.put(SecurityConstant.AUTHORITIES_KEY,authorities);
        Map<String, Object> header = new HashMap<>();
        header.put("typ","JWT");
        LocalDateTime exp = LocalDateTime.now().plusMinutes(accessTokenExpireTime);
        Date expToken = Date.from(exp.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(authentication.getName()) // Account
                .addClaims(claims)
                .setExpiration(expToken)
                .setHeader(header)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String  createRefreshToken(Authentication authentication){
        Map<String,Object> claims = new HashMap<>();
        claims.put(SecurityConstant.TOKEN_TYPE,Token_Type.REFRESH_TOKEN);

        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(refreshTokenExpireTime);
        Date expireDdate = Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims)
                .setExpiration(expireDdate)
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        if (!StringUtils.hasText(accessToken)) {
            return null;
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey)
                    .parseClaimsJws(accessToken).getBody();
            List<GrantedAuthority> authorities = Arrays.stream(claims.get("authorities")
                            .toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            User principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
        } catch (Exception e) {
        }
        return null;
    }


}
