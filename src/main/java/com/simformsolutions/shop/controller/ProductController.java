package com.simformsolutions.shop.controller;

import com.simformsolutions.shop.entity.Product;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class ProductController {
/*
    @PutMapping("/editp")
    public Product editProduct(Product product, int sellerId) throws ProductNotFoundException {
        return sellerService.updateProduct(product, sellerId);
    }

    @DeleteMapping("/deletep")
    public void deleteProduct(int productId, int sellerId) {
        sellerService.deleteProduct(productId, sellerId);
    }*/
}
