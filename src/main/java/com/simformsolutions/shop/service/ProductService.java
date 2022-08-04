package com.simformsolutions.shop.service;

import com.simformsolutions.shop.dto.ProductDetails;
import com.simformsolutions.shop.entity.Category;
import com.simformsolutions.shop.entity.Product;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.repository.CategoryRepository;
import com.simformsolutions.shop.repository.ColourRepository;
import com.simformsolutions.shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ColourRepository colourRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/productImages";

    public Product findProductById(int productId) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new ProductNotFoundException(productId + "");
    }

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findProductsByCategory(int categoryId) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return category.get().getProducts();
        }
        throw new CategoryNotFoundException(categoryId + "");
    }

    public void updateProduct(int productId, ProductDetails productDetails) throws ProductNotFoundException, IOException {
        Product product = findProductById(productId);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setSizes(productDetails.getSize());
        product.setColours(productDetails.getColour().stream().map(colourRepository::findByName).toList());

        String fileName = productDetails.getImage().getOriginalFilename();
        product.setImage(fileName);
        Path path = Paths.get(uploadDirectory, fileName);
        Files.write(path, productDetails.getImage().getBytes());

        productRepository.save(product);
    }

    public void deleteProductById(int productId) throws ProductNotFoundException {
        Product product = findProductById(productId);
        productRepository.delete(product);
    }
}
