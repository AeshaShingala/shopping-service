package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.repository.UserRepository;
import com.simformsolutions.shop.service.AdminService;
import com.simformsolutions.shop.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/admin")
@Controller
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    SellerService sellerService;

    @GetMapping("/home")
    public ModelAndView dashboard(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).get();
        return new ModelAndView("adminDashboard")
                .addObject("admin", user)
                .addObject("listOfSellers", adminService.getAllSellers());
    }

    @PostMapping("/status")
    @ResponseBody
    public String disableUser(@RequestParam("userId") int id) {
        User user = sellerService.findSellerById(id);
        user.setEnable(!user.isEnable());
        userRepository.save(user);
        return "success";
    }

    @GetMapping("/profile/show/{id}")
    public ModelAndView showProfile(@PathVariable("id") int userId) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", adminService.findAdminById(userId));
        mv.addObject("hasRole", "admin");
        return mv;
    }

    @GetMapping("/profile/edit")
    public ModelAndView editAdmin(@RequestParam("userId") int userId) {
        ModelAndView mv = new ModelAndView("editProfile");
        mv.addObject("user", adminService.findAdminById(userId));
        mv.addObject("hasRole", "admin");
        return mv;
    }

    @PostMapping("/profile/edit")
    public ModelAndView editAdminDetails(User user) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", adminService.updateUser(user));
        mv.addObject("hasRole", "admin");
        return mv;
    }
}
