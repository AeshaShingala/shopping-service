package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.dto.AuthenticationRequest;
import com.simformsolutions.shop.dto.UserDetail;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.service.*;
import com.simformsolutions.shop.utility.CookieUtil;
import com.simformsolutions.shop.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    SuperAdminService superAdminService;
    @Autowired
    AdminService adminService;
    @Autowired
    SellerService sellerService;
    @Autowired
    BuyerService buyerService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomUserDetailsService customUserDetailsService;


    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/signup/sa")
    public String registerSuperAdmin() {
        return "register";
    }

    @PostMapping("/signup/sa")
    public String addSuperAdmin(UserDetail userDetail) {
        User user = superAdminService.saveSuperAdmin(userDetail);
        buyerService.createWishlistAndCart(user);
        return "redirect:/";
    }

    @GetMapping("/signup/admin")
    public String registerAdmin() {
        return "register";
    }

    @PostMapping("/signup/admin")
    public String addAdmin(UserDetail userDetail) {
        User user = adminService.saveAdmin(userDetail);
        buyerService.createWishlistAndCart(user);
        return "redirect:/";
    }

    @GetMapping("/signup/buyer")
    public String registerBuyer() {
        return "register";
    }

    @PostMapping("/signup/buyer")
    public String addBuyer(UserDetail userDetail) {
        User user = buyerService.saveBuyer(userDetail);
        buyerService.createWishlistAndCart(user);
        return "redirect:/";
    }

    @GetMapping("/signup/seller")
    public String registerSeller() {
        return "register";
    }

    @PostMapping("/signup/seller")
    public String addSeller(UserDetail userDetail) {
        sellerService.saveSeller(userDetail);
        return "redirect:/";
    }

    @GetMapping("/login/sa")
    public String saveSuperAdmin() {
        return "login";
    }

    @PostMapping("/login/sa")
    public void getSuperAdminPrincipal(@ModelAttribute AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        Cookie cookie = CookieUtil.cookieMaker(authenticationRequest.getEmail(), authenticationRequest.getPassword(), authenticationManager, jwtUtil, customUserDetailsService);
        if (cookie == null) {
            response.sendRedirect("/");
        } else {
            response.addCookie(cookie);
            response.sendRedirect("/super-admin/home");
        }
    }

    @GetMapping("/login/admin")
    public String saverAdmin() {
        return "login";
    }

    @PostMapping("/login/admin")
    public void getAdminPrincipal(@ModelAttribute AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        Cookie cookie = CookieUtil.cookieMaker(authenticationRequest.getEmail(), authenticationRequest.getPassword(), authenticationManager, jwtUtil, customUserDetailsService);
        if (cookie == null) {
            response.sendRedirect("/");
        } else {
            response.addCookie(cookie);
            response.sendRedirect("/admin/home");
        }
    }

    @GetMapping("/login/buyer")
    public String saveBuyer() {
        return "login";
    }

    @PostMapping("/login/buyer")
    public void getBuyerPrincipal(@ModelAttribute AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        Cookie cookie = CookieUtil.cookieMaker(authenticationRequest.getEmail(), authenticationRequest.getPassword(), authenticationManager, jwtUtil, customUserDetailsService);
        if (cookie == null) {
            response.sendRedirect("/");
        } else {
            response.addCookie(cookie);
            response.sendRedirect("/buyer/home");
        }
    }

    @GetMapping("/login/seller")
    public String saveSeller() {
        return "login";
    }

    @PostMapping("/login/seller")
    public void getSellerPrincipal(@ModelAttribute AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        Cookie cookie = CookieUtil.cookieMaker(authenticationRequest.getEmail(), authenticationRequest.getPassword(), authenticationManager, jwtUtil, customUserDetailsService);
        if (cookie == null) {
            response.sendRedirect("/");
        } else {
            response.addCookie(cookie);
            response.sendRedirect("/seller/home");
        }
    }
}