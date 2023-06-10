package net.javaguides.mssql.Securirty;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Optional;

public class SecutityUltis {

    public static Optional<String> getCurrentAccountLogin(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return Optional.ofNullable(extractPrincicpal(authentication));
    }
    public  static String  extractPrincicpal(Authentication authentication){
        if(Objects.isNull(authentication)){
            return null;
        }
        try {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                return String.valueOf(userDetails.getUsername());
            }
                if (authentication.getPrincipal() instanceof  Long){
                    return  String.valueOf(authentication.getPrincipal().toString());
                }
            }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
        }
    }

