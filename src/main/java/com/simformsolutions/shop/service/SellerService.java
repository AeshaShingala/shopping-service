package com.simformsolutions.shop.service;

import com.simformsolutions.shop.dto.ProductDetails;
import com.simformsolutions.shop.dto.UserDetail;
import com.simformsolutions.shop.entity.Category;
import com.simformsolutions.shop.entity.Product;
import com.simformsolutions.shop.entity.Role;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.UserNotFoundException;
import com.simformsolutions.shop.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.simformsolutions.shop.constants.ReportConstants.PRODUCT_FILEPATH;

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

    public User userDetailsToUser(UserDetail userDetail) {
        return modelMapper.map(userDetail, User.class);
    }

    public void saveSeller(UserDetail userDetail) {
        Optional<User> optionalUser = userRepository.findByEmail(userDetail.getEmail());
        Role role = roleRepository.findByName("seller");

        if (optionalUser.isEmpty()) {
            userDetail.setPassword(new BCryptPasswordEncoder().encode(userDetail.getPassword()));
            User user = userDetailsToUser(userDetail);
            user.getRoles().add(role);
            userRepository.save(user);
        } else {
            if (!(optionalUser.get().getRoles().contains(role)))
                optionalUser.get().getRoles().add(role);
            userRepository.save(optionalUser.get());
        }
    }

    public User findSellerById(int id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException(id + "");
    }

    public User findSellerByEmail(String email) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UserNotFoundException(email + "");
    }

    public User updateSeller(User user) throws UserNotFoundException {
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
        throw new UserNotFoundException(user.getUserId() + "");
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
            Path path = Paths.get(PRODUCT_FILEPATH, fileName);
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
        throw new UserNotFoundException(sellerId + "");
    }

    public Category findCategoryById(int id) throws CategoryNotFoundException {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent())
            return category.get();
        throw new CategoryNotFoundException(id + "");
    }
}
