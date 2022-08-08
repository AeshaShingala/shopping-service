package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.dto.UserDetails;
import com.simformsolutions.shop.entity.*;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.repository.CartRepository;
import com.simformsolutions.shop.repository.CategoryRepository;
import com.simformsolutions.shop.repository.ColourRepository;
import com.simformsolutions.shop.repository.WishlistRepository;
import com.simformsolutions.shop.service.BuyerService;
import com.simformsolutions.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    BuyerService buyerService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ColourRepository colourRepository;
    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ModelAndView dashboard(@PathVariable("id") int buyerId) {
        return new ModelAndView("buyerDashboard")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("listOfProducts", productService.findAllProducts())
                .addObject("listOfCategories", categoryRepository.findAll());
    }

    @GetMapping("/signup")
    public String addProfile() {
        return "register";
    }

    @PostMapping("/signup")
    public String addBuyer(UserDetails userDetails) {
        User user = buyerService.saveBuyer(userDetails);
        buyerService.createWishlistAndCart(user);
        return "redirect:/buyer/" + user.getUserId();
    }

    @GetMapping("/login")
    public String saveBuyer() {
        return "login";
    }

    @PostMapping("/login")
    public String saveBuyer(@RequestParam("email") String email, @RequestParam("password") String password) {
        User user = buyerService.findBuyerByEmail(email);
        return "redirect:/buyer/" + user.getUserId();
    }

    @GetMapping("/profile/show/{id}")
    public ModelAndView showProfile(@PathVariable("id") int buyerId) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", buyerService.findBuyerById(buyerId));
        mv.addObject("hasRole", "buyer");
        return mv;
    }

    @GetMapping("/profile/edit")
    public ModelAndView editBuyer(@RequestParam("userId") int buyerId) {
        ModelAndView mv = new ModelAndView("editProfile");
        mv.addObject("user", buyerService.findBuyerById(buyerId));
        mv.addObject("hasRole", "buyer");
        return mv;
    }

    @PostMapping("/profile/edit")
    public ModelAndView editBuyerDetails(User buyer) {
        ModelAndView mv = new ModelAndView("showProfile");
        mv.addObject("user", buyerService.updateBuyer(buyer));
        mv.addObject("hasRole", "buyer");
        return mv;
    }

    @GetMapping("/products/{cid}/{bid}")
    public ModelAndView showProductsByCategory(@PathVariable("cid") int categoryId, @PathVariable("bid") int buyerId) throws CategoryNotFoundException {
        return new ModelAndView("showProductsByCategory")
                .addObject("listOfProducts", productService.findProductsByCategory(categoryId))
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("listOfColours", colourRepository.findAll())
                .addObject("listOfSizes", Arrays.stream(Size.values()).toList())
                .addObject("listOfCategories", categoryRepository.findAll());
    }

    @GetMapping("/product/view/{id}/{bid}")
    public ModelAndView showProductDetails(@PathVariable("id") int productId, @PathVariable("bid") int buyerId) throws ProductNotFoundException {
        return new ModelAndView("showBuyerProductDetails")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("product", productService.findProductById(productId))
                .addObject("listOfSizes", productService.findProductById(productId).getSizes())
                .addObject("listOfColours", productService.findProductById(productId).getColours());
    }

    @GetMapping("/product/wishlist/{id}/{bid}")
    public String addProductToWishlist(@PathVariable("id") int productId, @PathVariable("bid") int buyerId) throws ProductNotFoundException {
        buyerService.saveProductInWishlist(productService.findProductById(productId), buyerId);
        return "redirect:/buyer/" + buyerId;
    }

    @GetMapping("/wishlist/{bid}")
    public ModelAndView showWishlist(@PathVariable("bid") int buyerId) {
        return new ModelAndView("wishlist")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("listOfProducts", buyerService.findProductsInWishlist(buyerId));
    }

    @GetMapping("/wishlist/remove/{id}/{bid}")
    public ModelAndView deleteProductFromWishlist(@PathVariable("id") int productId, @PathVariable("bid") int buyerId) throws ProductNotFoundException {
        return new ModelAndView("wishlist")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("listOfProducts", buyerService.removeProductFromWishlist(buyerId, productId));
    }

    @GetMapping("/cart/{bid}")
    public ModelAndView showCart(@PathVariable("bid") int buyerId) {
        return new ModelAndView("cart")
                .addObject("listOfPurchaseProducts", buyerService.findAllProductsInCart(buyerId))
                .addObject("user", buyerService.findBuyerById(buyerId));
    }

    @PostMapping("/product/cart")
    public String addProductToCart(@RequestParam("userId") int buyerId, @RequestParam("productId") int productId, @RequestParam("sizes") String size,
                                   @RequestParam("colour") String colour, @RequestParam("quantity") String quantity) throws ProductNotFoundException {
        buyerService.saveProductToCart(buyerId, productService.findProductById(productId), size, colour, Integer.parseInt(quantity));
        return "redirect:/buyer/cart/" + buyerId;
    }


    @GetMapping("/cart/remove/{id}/{bid}")
    public String removeProductFromCart(@PathVariable("id") int purchaseProductId, @PathVariable("bid") int buyerId) {
        buyerService.removeProductFromCart(purchaseProductId);
        return "redirect:/buyer/cart/" + buyerId;
    }

    @PostMapping("/product/quantity")
    @ResponseBody
    public String changeQuantity(PurchaseProduct purchaseProduct) {
        buyerService.updateQuantity(purchaseProduct.getPurchaseProductId(), purchaseProduct.getQuantity());
        return "success";
    }

}
