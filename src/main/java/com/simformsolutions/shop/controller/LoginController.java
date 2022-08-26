package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.dto.UserDetail;
import com.simformsolutions.shop.entity.Role;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.service.BuyerService;
import com.simformsolutions.shop.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @GetMapping("/login")
    public String loginUser() {
        return "login";
    }

    @PostMapping("/principal")
    public ModelAndView getUserPrincipal() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = buyerService.findBuyerByEmail(principal);
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        System.out.println(user);
        if (roles.size() > 1)
            return new ModelAndView("selectRole").addObject("user", user);
        else
            return new ModelAndView("redirect:/" + roles.get(0) + "/" + user.getUserId());
    }
}
