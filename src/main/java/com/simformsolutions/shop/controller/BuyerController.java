package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.entity.CartProduct;
import com.simformsolutions.shop.entity.Purchase;
import com.simformsolutions.shop.entity.Size;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.repository.CategoryRepository;
import com.simformsolutions.shop.repository.ColourRepository;
import com.simformsolutions.shop.service.BuyerService;
import com.simformsolutions.shop.service.ProductService;
import com.simformsolutions.shop.service.PurchaseService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
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
    @Autowired
    PurchaseService purchaseService;

    @GetMapping("/home")
    public String falseDashboard(Authentication authentication ){
        return "redirect:/buyer/" + buyerService.findBuyerByEmail(authentication.getName()).getUserId();
    }

    @GetMapping("/{id}")
    public ModelAndView dashboard(@PathVariable("id") int buyerId) {
        return new ModelAndView("buyerDashboard")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("listOfProducts", productService.findAllProducts())
                .addObject("listOfCategories", categoryRepository.findAll())
                .addObject("wishlistSize", buyerService.findBuyerById(buyerId).getWishlist().getWishlistProducts().size())
                .addObject("cartSize", buyerService.findBuyerById(buyerId).getCart().getCartProducts().size());
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
                .addObject("listOfCategories", categoryRepository.findAll())
                .addObject("wishlistSize", buyerService.findBuyerById(buyerId).getWishlist().getWishlistProducts().size())
                .addObject("cartSize", buyerService.findBuyerById(buyerId).getCart().getCartProducts().size());
    }

    @GetMapping("/product/view/{id}/{bid}")
    public ModelAndView showProductDetails(@PathVariable("id") int productId, @PathVariable("bid") int buyerId) throws ProductNotFoundException {
        return new ModelAndView("showBuyerProductDetails")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("product", productService.findProductById(productId))
                .addObject("listOfSizes", productService.findProductById(productId).getSizes())
                .addObject("listOfColours", productService.findProductById(productId).getColours())
                .addObject("wishlistSize", buyerService.findBuyerById(buyerId).getWishlist().getWishlistProducts().size())
                .addObject("cartSize", buyerService.findBuyerById(buyerId).getCart().getCartProducts().size());
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
                .addObject("listOfProducts", buyerService.findProductsInWishlist(buyerId))
                .addObject("wishlistSize", buyerService.findBuyerById(buyerId).getWishlist().getWishlistProducts().size())
                .addObject("cartSize", buyerService.findBuyerById(buyerId).getCart().getCartProducts().size());
    }

    @GetMapping("/wishlist/remove/{id}/{bid}")
    public String removeProductFromWishlist(@PathVariable("id") int productId, @PathVariable("bid") int buyerId) throws ProductNotFoundException {
        buyerService.removeProductFromWishlist(buyerId, productId);
        return "redirect:/buyer/wishlist/" + buyerId;
    }

    @GetMapping("/cart/{bid}")
    public ModelAndView showCart(@PathVariable("bid") int buyerId) {
        return new ModelAndView("cart")
                .addObject("listOfCartProducts", buyerService.findAllProductsInCart(buyerId))
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("wishlistSize", buyerService.findBuyerById(buyerId).getWishlist().getWishlistProducts().size())
                .addObject("cartSize", buyerService.findBuyerById(buyerId).getCart().getCartProducts().size());
    }

    @PostMapping("/product/cart")
    public String addProductToCart(@RequestParam("userId") int buyerId, @RequestParam("productId") int productId, @RequestParam("sizes") String size, @RequestParam("colour") String colour, @RequestParam("quantity") String quantity) throws ProductNotFoundException {
        buyerService.saveProductToCart(buyerId, productService.findProductById(productId), size, colour, Integer.parseInt(quantity));
        return "redirect:/buyer/cart/" + buyerId;
    }

    @GetMapping("/cart/remove/{id}/{bid}")
    public String removeProductFromCart(@PathVariable("id") int cartProductId, @PathVariable("bid") int buyerId) {
        buyerService.removeProductFromCart(cartProductId);
        return "redirect:/buyer/cart/" + buyerId;
    }

    @PostMapping("/product/quantity")
    @ResponseBody
    public String changeQuantity(CartProduct cartProduct) {
        buyerService.updateQuantity(cartProduct.getCartProductId(), cartProduct.getQuantity());
        return "success";
    }

    @GetMapping("/order/{bid}")
    public ModelAndView checkout(@PathVariable("bid") int buyerId, @RequestParam("subtotal") String subtotal, @RequestParam("shipping") String shipping, @RequestParam("total") String total) {
        return new ModelAndView("checkout")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("listOfProducts", buyerService.findAllProductsInCart(buyerId))
                .addObject("subtotal", subtotal).addObject("shipping", shipping)
                .addObject("total", total)
                .addObject("wishlistSize", buyerService.findBuyerById(buyerId).getWishlist().getWishlistProducts().size())
                .addObject("cartSize", buyerService.findBuyerById(buyerId).getCart().getCartProducts().size());
    }

    @PostMapping("/order/{bid}")
    @ResponseBody
    public String placeOrder(@PathVariable("bid") int buyerId, @RequestParam("total") String amount, @RequestParam("address") String shippingAddress) throws JRException {
        String link = buyerService.updateCart(buyerId, shippingAddress, BigDecimal.valueOf(Long.parseLong(amount.substring(1))));
        System.out.println(link);
        return link;
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("id") String invoiceName) throws Exception {
        Purchase purchase = purchaseService.findPurchaseByInvoiceName(invoiceName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + invoiceName + ".pdf" + "\"")
                .body(purchase.getInvoice());
    }

    @GetMapping("/history/{bid}")
    public ModelAndView showOrderHistory(@PathVariable("bid") int buyerId) {
        return new ModelAndView("orderHistory")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("purchases", buyerService.findOrderHistory(buyerId));
    }

    @GetMapping("/invoice/{bid}/{purchaseId}")
    public ModelAndView showInvoice(@PathVariable("bid") int buyerId, @PathVariable("purchaseId") int purchaseId) {
        return new ModelAndView("showOrder")
                .addObject("user", buyerService.findBuyerById(buyerId))
                .addObject("listOfCartProducts", buyerService.showOrder(purchaseId));
    }
}