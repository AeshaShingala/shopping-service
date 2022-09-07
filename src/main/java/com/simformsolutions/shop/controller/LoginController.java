package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.dto.AuthenticationRequest;
import com.simformsolutions.shop.dto.UserDetail;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.repository.CategoryRepository;
import com.simformsolutions.shop.service.*;
import com.simformsolutions.shop.utility.CookieUtil;
import com.simformsolutions.shop.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    @Autowired
    ProductService productService;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/")
    public ModelAndView dashboard() {
        return new ModelAndView("dashboard")
                .addObject("listOfProducts", productService.findAllProducts())
                .addObject("listOfCategories", categoryRepository.findAll());
    }

    @GetMapping("/signup/{role}")
    public String registerSuperAdmin() {
        return "register";
    }

    @PostMapping("/signup/{role}")
    public String addSuperAdmin(@PathVariable("role") String role, UserDetail userDetail) {
        User user;
        switch (role) {
            case "super-admin" -> {
                user = superAdminService.saveSuperAdmin(userDetail);
                buyerService.createWishlistAndCart(user);
            }
            case "admin" -> {
                user = adminService.saveAdmin(userDetail);
                buyerService.createWishlistAndCart(user);
            }
            case "buyer" -> {
                user = buyerService.saveBuyer(userDetail);
                buyerService.createWishlistAndCart(user);
            }
            case "seller" -> sellerService.saveSeller(userDetail);
            default -> {
            }
        }
        return "redirect:/";
    }

    @GetMapping("/login/{role}")
    public ModelAndView saveSuperAdmin(@PathVariable("role") String role) {
        return new ModelAndView("login").addObject("role", role)
                .addObject("invalid", 0);
    }

    @PostMapping("/login/{role}")
    public void getSuperAdminPrincipal(@PathVariable("role") String role, @ModelAttribute AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        Cookie cookie = CookieUtil.cookieMaker(authenticationRequest.getEmail(), authenticationRequest.getPassword(), authenticationManager, jwtUtil, customUserDetailsService);
        if (cookie == null) {
            response.sendRedirect("/login/" + role + "/invalid");
        } else {
            response.addCookie(cookie);
            switch (role) {
                case "super-admin" -> response.sendRedirect("/super-admin/home/1/userId");
                case "admin" -> response.sendRedirect("/admin/home/1/userId");
                case "buyer" -> response.sendRedirect("/buyer/home");
                case "seller" -> response.sendRedirect("/seller/home");
                default -> response.sendRedirect("/");
            }
        }
    }

    @RequestMapping("/login/{role}/invalid")
    public ModelAndView invalidLogin(@PathVariable("role") String role) {
        return new ModelAndView("login").addObject("role", role)
                .addObject("invalid", 1);
    }
}