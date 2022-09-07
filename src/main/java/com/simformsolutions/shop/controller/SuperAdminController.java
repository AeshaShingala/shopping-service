package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.repository.UserRepository;
import com.simformsolutions.shop.service.SellerService;
import com.simformsolutions.shop.service.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/super-admin")
@Controller
public class SuperAdminController {

    @Autowired
    SuperAdminService superAdminService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    SellerService sellerService;


    @GetMapping("/home/{pageNo}/{sortBy}")
    public ModelAndView dashboard(@PathVariable("pageNo") int pageNo, @PathVariable("sortBy") String sortBy, Authentication authentication) {
        User user = superAdminService.findSuperAdminByEmail(authentication.getName());
        Page<User> sellers = superAdminService.getAllSellers(pageNo, 2, sortBy);
        return new ModelAndView("superAdminDashboard")
                .addObject("superadmin", user)
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
        mv.addObject("user", superAdminService.findSuperAdminById(userId));
        mv.addObject("hasRole", "super-admin");
        return mv;
    }

    @GetMapping("/profile/edit")
    public ModelAndView editSuperAdmin(@RequestParam("userId") int userId) {
        ModelAndView mv = new ModelAndView("editProfile");
        mv.addObject("user", superAdminService.findSuperAdminById(userId));
        mv.addObject("hasRole", "super-admin");
        return mv;
    }

    @PostMapping("/profile/edit")
    public ModelAndView editAdminDetails(User user) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", superAdminService.updateUser(user));
        mv.addObject("hasRole", "super-admin");
        return mv;
    }

    @GetMapping("/delete/{id}")
    public String removeSeller(@PathVariable("id") int userId) {
        superAdminService.deleteSeller(userId);
        return "redirect:/super-admin/home/1/userId";
    }

}
