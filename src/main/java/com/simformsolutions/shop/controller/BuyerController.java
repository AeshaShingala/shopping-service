package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.service.BuyerService;
import com.simformsolutions.shop.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    BuyerService buyerService;

    @GetMapping("/")
    public String dashboard() {
        return "buyerDashboard";
    }

    @GetMapping("/signup")
    public String addProfile() {
        return "register";
    }

    @PostMapping("/signup")
    public String addSeller(User user) {
        buyerService.saveBuyer(user);
        return "buyerDashboard";
    }
}
