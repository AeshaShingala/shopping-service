package com.simformsolutions.shop.utility;

import javax.servlet.http.Cookie;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.simformsolutions.shop.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class CookieUtil {

    public static Cookie cookieMaker(String email, String password, AuthenticationManager authenticationManager,
                                     JwtUtil jwtUtil, CustomUserDetailsService service, HttpServletRequest httpServletRequest) {
        Cookie cookie = null;
        try {
            UserDetails userDetails = service.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    email,password,userDetails.getAuthorities());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception ex) {
            System.out.println(ex);
            return cookie;
        }
        cookie = new Cookie("token", jwtUtil.generateToken(email));
        cookie.setPath("/");
        cookie.setMaxAge(60*60*10);
        return cookie;
    }
}
