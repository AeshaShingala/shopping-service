package com.simformsolutions.shop.utility;

import com.simformsolutions.shop.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.Cookie;

public class CookieUtil {

    public static Cookie cookieMaker(String email, String password, AuthenticationManager authenticationManager,
                                     JwtUtil jwtUtil, CustomUserDetailsService service) {
        try {
            UserDetails userDetails = service.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password, userDetails.getAuthorities());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception ex) {
            return null;
        }
        Cookie cookie = new Cookie("token", jwtUtil.generateToken(email));
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 10);
        return cookie;
    }
}
