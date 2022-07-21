package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.entity.Size;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.repository.CategoryRepository;
import com.simformsolutions.shop.repository.ColourRepository;
import com.simformsolutions.shop.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/seller")
@Controller
public class SellerController {

    @Autowired
    SellerService sellerService;

    @Autowired
    ColourRepository colourRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public ModelAndView showDashboard(@PathVariable("id") int sellerId) {
        ModelAndView mv = new ModelAndView("sellerDashboard").addObject("seller", sellerService.findSellerById(sellerId));
        mv.addObject("listOfProducts", sellerService.findAllProductsBySellerId(sellerId));
        return mv;
    }

    @GetMapping("/signup")
    public String registerSeller() {
        return "register";
    }

    @PostMapping("/signup")
    public String addSeller(User seller) {
        User user = sellerService.saveSeller(seller);
        return "redirect:/seller/" + user.getUserId();
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String loginSeller(@RequestParam("email") String email, @RequestParam("password") String password) {
        User seller = sellerService.setRole(sellerService.findSellerByEmail(email));
        return "redirect:/seller/" + seller.getUserId();
    }

    @GetMapping("/profile/show/{id}")
    public ModelAndView showProfile(@PathVariable("id") int sellerId) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", sellerService.findSellerById(sellerId));
        return mv;
    }

    @GetMapping("/profile/edit")
    public ModelAndView editSeller(@RequestParam("userId") int sellerId) {
        ModelAndView mv = new ModelAndView("editProfile");
        mv.addObject("user", sellerService.findSellerById(sellerId));
        return mv;
    }

    @PostMapping("/profile/edit")
    public ModelAndView editSellerDetails(User seller) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", sellerService.updateSeller(seller));
        return mv;
    }

    @GetMapping("/product/add/{id}")
    public ModelAndView addProduct(@PathVariable("id") int sellerId) {
        ModelAndView mv = new ModelAndView("addProduct");
        mv.addObject("seller", sellerService.findSellerById(sellerId));
        mv.addObject("listOfSizes", Arrays.asList(Size.values()));
        mv.addObject("listOfColour", colourRepository.findAll());
        mv.addObject("listOfCategories", categoryRepository.findAll());
        return mv;
    }

    //change request params here
    @PostMapping("/product/add/{id}")
    public String addProduct(@PathVariable("id") int sellerId, @RequestParam("name") String name, @RequestParam("description") String description,
                             @RequestParam("price") BigDecimal price, @RequestParam("category") int categoryId, @RequestParam("image") MultipartFile image,
                             @RequestParam("size") List<Size> size, @RequestParam("colour") List<String> colour) throws IOException, CategoryNotFoundException {
        sellerService.saveSellingProduct(sellerId, name, description, price, categoryId, image, size, colour);
        return "redirect:/seller/" + sellerId;
    }

    @GetMapping("/delete/{id}")
    public String removeProduct(@PathVariable("id") int productId) throws ProductNotFoundException {
        sellerService.deleteProductById(productId);
        return "redirect:/seller/2";
    }
}