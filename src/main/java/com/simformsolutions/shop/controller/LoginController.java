package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.dto.UserDetail;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.service.BuyerService;
import com.simformsolutions.shop.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    BuyerService buyerService;

    @Autowired
    SellerService sellerService;

    @GetMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/signup/buyer")
    public String registerBuyer() {
        return "buyerRegister";
    }

    @PostMapping("/signup/buyer")
    public String addBuyer(UserDetail userDetail) {
        User user = buyerService.saveBuyer(userDetail);
        buyerService.createWishlistAndCart(user);
        return "redirect:/buyer/" + user.getUserId();
    }

    @GetMapping("/signup/seller")
    public String registerSeller() {
        return "sellerRegister";
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
    public String getBuyerPrincipal(Authentication authentication) {
        String principal = authentication.getName();
        System.out.println(principal);
        return "redirect:/buyer/" + buyerService.findBuyerByEmail(principal).getUserId();
    }

    @GetMapping("/login/seller")
    public String saveSeller() {
        return "login";
    }

    @PostMapping("/login/seller")
    public String getSellerPrincipal(Authentication authentication) {
        String principal = authentication.getName();
        return "redirect:/seller/" + sellerService.findSellerByEmail(principal).getUserId();
    }
}
