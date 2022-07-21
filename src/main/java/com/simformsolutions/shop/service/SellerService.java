package com.simformsolutions.shop.service;

import com.simformsolutions.shop.entity.*;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.exception.SellerNotFoundException;
import com.simformsolutions.shop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    ProductRepository productRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    ColourRepository colourRepository;
    CategoryRepository categoryRepository;

    public SellerService(UserRepository userRepository, RoleRepository roleRepository, ColourRepository colourRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.colourRepository = colourRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/productImages";

    public User saveSeller(User user) {
        Role role = roleRepository.findByName("seller");
        user.setRole(role);
        return userRepository.save(user);
    }

    public User findSellerById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new SellerNotFoundException(id + "");
    }

    public User findSellerByEmail(String email) throws SellerNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new SellerNotFoundException(email + "");
    }

    public User setRole(User user) {
        user.setHasRole("seller");
        return userRepository.save(user);
    }

    public User updateSeller(User user) throws SellerNotFoundException {
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        if (optionalUser.isPresent()) {
            User currentUser = optionalUser.get();
            currentUser.setName(user.getName());
            currentUser.setPassword(user.getPassword());
            currentUser.setEmail(user.getEmail());
            currentUser.setContact(user.getContact());
            currentUser.setAddress(user.getAddress());
            return userRepository.save(currentUser);
        }
        throw new SellerNotFoundException(user.getUserId() + "");
    }

    public void saveSellingProduct(int sellerId, String name, String description, BigDecimal price, int categoryId,
                                   MultipartFile image, List<Size> sizes, List<String> colours) throws IOException, CategoryNotFoundException {
        User user = findSellerById(sellerId);
        Category category = findCategoryById(categoryId);
        Product product = new Product();

        product.setColours(colours.stream().map(colourRepository::findByName).toList());
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setSizes(sizes);

        String fileName = image.getOriginalFilename();
        product.setImage(fileName);
        Path path = Paths.get(uploadDirectory, fileName);
        Files.write(path, image.getBytes());

        category.setProduct(product);
        user.getProducts().add(categoryRepository.save(category).getProducts().get((category.getProducts().size() - 1)));
        userRepository.save(user);
    }

    public List<Product> findAllProductsBySellerId(int sellerId) {
        Optional<User> user = userRepository.findById(sellerId);
        if (user.isPresent()) {
            return user.get().getProducts();
        }
        throw new SellerNotFoundException(sellerId + "");
    }

    public Category findCategoryById(int id) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent())
            return category.get();
        throw new CategoryNotFoundException(id + "");
    }


    //work here
    public void deleteProductById(int productId) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setColours(new ArrayList<>());
            productRepository.deleteById(productId);
        }
        throw new ProductNotFoundException(productId + "");
    }
}