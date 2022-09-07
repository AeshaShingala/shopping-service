package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.repository.UserRepository;
import com.simformsolutions.shop.service.AdminService;
import com.simformsolutions.shop.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/home/{pageNo}/{sortBy}")
    public ModelAndView dashboard(@PathVariable("pageNo") int pageNo, @PathVariable("sortBy") String sortBy, Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).get();
        Page<User> sellers = adminService.getAllSellers(pageNo, 4, sortBy);
        return new ModelAndView("adminDashboard")
                .addObject("admin", user)
                .addObject("listOfSellers", sellers.getContent())
                .addObject("currentPage", pageNo)
                .addObject("totalPages", sellers.getTotalPages())
                .addObject("totalItems", sellers.getTotalElements())
                .addObject("sort", sortBy);
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
