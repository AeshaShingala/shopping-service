package com.simformsolutions.shop.service;

import com.simformsolutions.shop.dto.ProductDetails;
import com.simformsolutions.shop.dto.UserDetails;
import com.simformsolutions.shop.entity.Category;
import com.simformsolutions.shop.entity.Product;
import com.simformsolutions.shop.entity.Role;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.SellerNotFoundException;
import com.simformsolutions.shop.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class SellerService {

    ModelMapper modelMapper;
    ProductRepository productRepository;
    RoleRepository roleRepository;
    ColourRepository colourRepository;
    CategoryRepository categoryRepository;
    ProductService productService;
    @Autowired
    UserRepository userRepository;

    public SellerService(ModelMapper modelMapper, ProductRepository productRepository, RoleRepository roleRepository, ColourRepository colourRepository, CategoryRepository categoryRepository, ProductService productService) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.roleRepository = roleRepository;
        this.colourRepository = colourRepository;
        this.categoryRepository = categoryRepository;
        this.productService = productService;
    }

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/productImages";

    public User userDetailsToUser(UserDetails userDetails) {
        return modelMapper.map(userDetails, User.class);
    }

    public User saveSeller(UserDetails userDetails) {
        Role role = roleRepository.findByName("seller");
        User user = userDetailsToUser(userDetails);
        user.getRoles().add(role);
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
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new SellerNotFoundException(email + "");
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

    public void saveSellingProduct(int sellerId, List<ProductDetails> productDetailsList) throws CategoryNotFoundException, IOException {
        User user = findSellerById(sellerId);
        for (ProductDetails productDetails : productDetailsList) {
            Category category = findCategoryById(productDetails.getCategoryId());
            Product product = new Product();

            product.setColours(productDetails.getColour().stream().map(colourRepository::findByName).toList());
            product.setName(productDetails.getName());
            product.setPrice(productDetails.getPrice());
            product.setDescription(productDetails.getDescription());
            product.setSizes(productDetails.getSize());

            String fileName = productDetails.getImage().getOriginalFilename();
            product.setImage(fileName);
            Path path = Paths.get(uploadDirectory, fileName);
            Files.write(path, productDetails.getImage().getBytes());

            category.setProduct(product);
            user.getProducts().add(categoryRepository.save(category).getProducts().get((category.getProducts().size() - 1)));
        }
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
}
