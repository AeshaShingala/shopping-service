package com.simformsolutions.shop.service;

import com.simformsolutions.shop.dto.ProductDetails;
import com.simformsolutions.shop.dto.UserDetails;
import com.simformsolutions.shop.entity.Category;
import com.simformsolutions.shop.entity.Product;
import com.simformsolutions.shop.entity.Role;
import com.simformsolutions.shop.entity.User;
import com.simformsolutions.shop.exception.CategoryNotFoundException;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.exception.SellerNotFoundException;
import com.simformsolutions.shop.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    UserRepository userRepository;
    RoleRepository roleRepository;
    ColourRepository colourRepository;
    CategoryRepository categoryRepository;

    public SellerService(ModelMapper modelMapper, UserRepository userRepository, RoleRepository roleRepository, ColourRepository colourRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.colourRepository = colourRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/webapp/productImages";
//    filePath = new ClassPathResource("").getFile().getAbsolutePath();

    public User userDetailsToUser(UserDetails userDetails) {
        return modelMapper.map(userDetails, User.class);
    }

    public User saveSeller(UserDetails userDetails) {
        Role role = roleRepository.findByName("seller");
        User user = userDetailsToUser(userDetails);
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

    public Product findProductById(int productId) throws ProductNotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new ProductNotFoundException(productId + "");
    }

    public void deleteProductById(int productId) {
        productRepository.deleteById(productId);
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
}