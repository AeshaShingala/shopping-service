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
import org.springframework.web.bind.annotation.ResponseBody;

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
        return "buyerLogin";
    }

    @PostMapping("/login/buyer/principal")
    public String getBuyerPrincipal(Authentication authentication) {
        return "redirect:/buyer/home";
//        return "redirect:/buyer/" + buyerService.findBuyerByEmail(authentication.getName()).getUserId();
    }

    @GetMapping("/login/seller")
    public String saveSeller() {
        return "sellerLogin";
    }

    @PostMapping("/login/seller/principal")
    public String getSellerPrincipal(Authentication authentication) {
        return "redirect:/seller/home";
//        return "redirect:/seller/" + sellerService.findSellerByEmail(authentication.getName()).getUserId();
    }

  /*  @GetMapping("/admin/login")
    public String saveSeller() {
        return "sellerLogin";
    }

    @PostMapping("/admin/login")
    public String getSellerPrincipal(Authentication authentication) {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        return "redirect:/admin/home";
//        return "redirect:/seller/" + sellerService.findSellerByEmail(authentication.getName()).getUserId();
    }

    @GetMapping("/admin/home")
    @ResponseBody
    public String home() {
        return "sellerLogin";
    }*/

}
