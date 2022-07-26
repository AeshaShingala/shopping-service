package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.dto.ProductDetails;
import com.simformsolutions.shop.dto.ProductsDetails;
import com.simformsolutions.shop.dto.UserDetails;
import com.simformsolutions.shop.entity.Colour;
import com.simformsolutions.shop.entity.Product;
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
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        ModelAndView mv = new ModelAndView("sellerDashboard");
        mv.addObject("seller", sellerService.findSellerById(sellerId));
        mv.addObject("listOfProducts", sellerService.findAllProductsBySellerId(sellerId));
        return mv;
    }

    @GetMapping("/signup")
    public String registerSeller() {
        return "register";
    }

    @PostMapping("/signup")
    public String addSeller(UserDetails userDetails) {
        User user = sellerService.saveSeller(userDetails);
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

    @PostMapping("/product/add/{id}")
    public String addProduct(@PathVariable("id") int sellerId, @ModelAttribute("ProductsDetails") ProductsDetails productDetails) throws IOException, CategoryNotFoundException {
        sellerService.saveSellingProduct(sellerId, productDetails.getProductDetailsList());
        return "redirect:/seller/" + sellerId;
    }

    @GetMapping("/product/show/{id}/{sid}")
    public ModelAndView showProduct(@PathVariable("id") int productId, @PathVariable("sid") int sellerId) throws ProductNotFoundException {
        Product product = sellerService.findProductById(productId);
        return new ModelAndView("showProductDetails").addObject("product", product).addObject("user", sellerService.findSellerById(sellerId)).addObject("listOfColours", product.getColours()).addObject("listOfSizes", product.getSizes());
    }

    @GetMapping("/product/delete/{id}/{sid}")
    public String removeProduct(@PathVariable("id") int productId, @PathVariable("sid") int sellerId) {
        sellerService.deleteProductById(productId);
        return "redirect:/seller/" + sellerId;
    }

    @GetMapping("/product/edit/{id}/{sid}")
    public ModelAndView editProduct(@PathVariable("id") int productId, @PathVariable("sid") int sellerId) throws ProductNotFoundException {
        ModelAndView mv = new ModelAndView("editProduct");
        Product product = sellerService.findProductById(productId);

        List<Colour> colours = colourRepository.findAll();
        colours.removeAll(product.getColours());
        //display only remaining sizes not all
        List<Size> sizes = Arrays.stream(Size.values()).toList();

        mv.addObject("product", product)
                .addObject("user", sellerService.findSellerById(sellerId))
                .addObject("listOfRemainingColour", colours)
                .addObject("listOfSizes", sizes);
        return mv;
    }

    @PostMapping("/product/edit/{id}/{sid}")
    public String editProduct(@PathVariable("id") int productId, @PathVariable("sid") int sellerId, @ModelAttribute("ProductDetails") ProductDetails productDetails) throws ProductNotFoundException, IOException {
        sellerService.updateProduct(productId, productDetails);
        return "redirect:/seller/" + sellerId;
    }
}