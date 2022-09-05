package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.dto.ProductDetails;
import com.simformsolutions.shop.dto.ProductsDetails;
import com.simformsolutions.shop.entity.Colour;
import com.simformsolutions.shop.entity.Product;
import com.simformsolutions.shop.entity.Size;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.repository.CategoryRepository;
import com.simformsolutions.shop.repository.ColourRepository;
import com.simformsolutions.shop.service.ProductService;
import com.simformsolutions.shop.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
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

    @Autowired
    ProductService productService;

    @GetMapping("/home")
    public ModelAndView showDashboard(Authentication authentication) {
        User user = sellerService.findSellerByEmail(authentication.getName());
        ModelAndView mv = new ModelAndView("sellerDashboard");
        mv.addObject("seller", user);
        mv.addObject("listOfProducts", sellerService.findAllSellerProducts(user));
        return mv;
    }

    @GetMapping("/profile/show/{id}")
    public ModelAndView showProfile(@PathVariable("id") int sellerId) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", sellerService.findSellerById(sellerId));
        mv.addObject("hasRole", "seller");
        return mv;
    }

    @GetMapping("/profile/edit")
    public ModelAndView editSeller(@RequestParam("userId") int sellerId) {
        ModelAndView mv = new ModelAndView("editProfile");
        mv.addObject("user", sellerService.findSellerById(sellerId));
        mv.addObject("hasRole", "seller");
        return mv;
    }

    @PostMapping("/profile/edit")
    public ModelAndView editSellerDetails(User seller) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", sellerService.updateSeller(seller));
        mv.addObject("hasRole", "seller");
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
        return "redirect:/seller/home";
    }

    @GetMapping("/product/show/{id}/{sid}")
    public ModelAndView showProduct(@PathVariable("id") int productId, @PathVariable("sid") int sellerId) throws ProductNotFoundException {
        Product product = productService.findProductById(productId);
        User user = sellerService.findSellerById(sellerId);
        return new ModelAndView("showSellerProductDetails")
                .addObject("product", product)
                .addObject("user", user)
                .addObject("listOfColours", product.getColours())
                .addObject("listOfSizes", product.getSizes());
    }

    @GetMapping("/product/delete/{id}/{sid}")
    public String removeProduct(@PathVariable("id") int productId, @PathVariable("sid") int sellerId) throws ProductNotFoundException {
        productService.deleteProductById(productId);
        return "redirect:/seller/home";
    }

    @GetMapping("/product/edit/{id}/{sid}")
    public ModelAndView editProduct(@PathVariable("id") int productId, @PathVariable("sid") int sellerId) throws ProductNotFoundException {

        Product product = productService.findProductById(productId);
        List<Colour> colours = colourRepository.findAll();
        colours.removeAll(product.getColours());
        List<Size> sizes = new java.util.ArrayList<>(Arrays.stream(Size.values()).toList());
        sizes.removeAll(product.getSizes());

        return new ModelAndView("editProduct")
                .addObject("product", product)
                .addObject("user", sellerService.findSellerById(sellerId))
                .addObject("listOfRemainingColour", colours)
                .addObject("listOfSizes", sizes);
    }

    @PostMapping("/product/edit/{id}/{sid}")
    public String editProduct(@ModelAttribute("ProductDetails") ProductDetails productDetails, @PathVariable("id") int productId, @PathVariable("sid") int sellerId) throws ProductNotFoundException, IOException {
        productService.updateProduct(productId, productDetails);
        return "redirect:/seller/home";
    }
}
