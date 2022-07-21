package com.simformsolutions.shop.service;

import com.simformsolutions.shop.entity.Product;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

/*    //find product id
    public Product findProductById(int productId) throws ProductNotFoundException {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ProductNotFoundException(productId + "");
    }

    public Product updateProduct(Product product, int sellerId) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(product.getProductId());
        if (optionalProduct.isPresent()) {
            Product currentProduct = optionalProduct.get();
            currentProduct.setName(product.getName());
            currentProduct.setDescription((product.getDescription()));
            currentProduct.setPrice(product.getPrice());
            currentProduct.setImage(product.getImage());
            currentProduct.setAvailable(product.isAvailable());
            return productRepository.save(currentProduct);
        }
        throw new ProductNotFoundException(product.getProductId() + "");
    }

    public void deleteProduct(int productId, int sellerId) {
        Optional<User> user = userRepository.findById(sellerId);
        if (user.isPresent()) {
            user.get().setProducts(user.get().getProducts().stream().filter(product -> product.equals(productRepository.findById(productId))).toList());
        }
    }*/
}
