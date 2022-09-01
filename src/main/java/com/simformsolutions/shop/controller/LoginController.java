package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.dto.AuthRequest;
import com.simformsolutions.shop.dto.UserDetail;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.service.BuyerService;
import com.simformsolutions.shop.service.CustomUserDetailsService;
import com.simformsolutions.shop.service.SellerService;
import com.simformsolutions.shop.utility.CookieUtil;
import com.simformsolutions.shop.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    BuyerService buyerService;

    @Autowired
    SellerService sellerService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @GetMapping("/")
    public String dashboard()
    {
        return "dashboard";
    }

    @GetMapping("/signup/buyer")
    public String registerBuyer() {
        return "register";
    }

    @PostMapping("/signup/buyer")
    public String addBuyer(UserDetail userDetail) {
        User user = buyerService.saveBuyer(userDetail);
        buyerService.createWishlistAndCart(user);
        return "redirect:/buyer/" + user.getUserId();
    }

    @GetMapping("/signup/seller")
    public String registerSeller() {
        return "register";
    }

    @PostMapping("/signup/seller")
    public String addSeller(UserDetail userDetail) {
        User user = sellerService.saveSeller(userDetail);
        return "redirect:/seller/" + user.getUserId();
    }

    @GetMapping("/login/buyer")
    public String saveBuyer() {
        return "login";
    }

    @PostMapping("/login/buyer")
    public void getBuyerPrincipal(@ModelAttribute AuthRequest authRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = CookieUtil.cookieMaker(authRequest.getEmail(),authRequest.getPassword(),authenticationManager,jwtUtil,customUserDetailsService,request);
        if(cookie == null){
            response.sendRedirect("/");
        }	else {
            response.addCookie(cookie);
            System.out.println(cookie.getValue());
            response.sendRedirect("/buyer/"+ buyerService.findBuyerByEmail(authRequest.getEmail()).getUserId());
        }
    }

    @GetMapping("/login/seller")
    public String saveSeller() {
        return "login";
    }

    @PostMapping("/login/seller")
    public void getSellerPrincipal(@ModelAttribute AuthRequest authRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = CookieUtil.cookieMaker(authRequest.getEmail(),authRequest.getPassword(),authenticationManager,jwtUtil,customUserDetailsService,request);
        if(cookie == null){
            response.sendRedirect("/");
        }	else {
            response.addCookie(cookie);
            System.out.println(cookie.getValue());
            response.sendRedirect("/seller/"+ sellerService.findSellerByEmail(authRequest.getEmail()).getUserId());
        }
    }
}
