package com.simformsolutions.shop.service;

import com.simformsolutions.shop.dto.PurchaseProductDetails;
import com.simformsolutions.shop.dto.UserDetails;
import com.simformsolutions.shop.entity.*;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.exception.SellerNotFoundException;
import com.simformsolutions.shop.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuyerService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    WishlistRepository wishlistRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PurchaseProductRepository purchaseProductRepository;
    @Autowired
    CartRepository cartRepository;

    public User userDetailsToUser(UserDetails userDetails) {
        return modelMapper.map(userDetails, User.class);
    }

    public User saveBuyer(UserDetails userDetails) {
        Role role = roleRepository.findByName("buyer");
        User user = userDetailsToUser(userDetails);
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    public User findBuyerById(int buyerId) {
        Optional<User> optionalUser = userRepository.findById(buyerId);
        if (optionalUser.isPresent())
            return optionalUser.get();
        throw new SellerNotFoundException(buyerId + "");
    }

    public User findBuyerByEmail(String email) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);
        if (optionalUser.isPresent())
            return optionalUser.get();
        throw new SellerNotFoundException(email);
    }

    public User updateBuyer(User user) {
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

    public void createWishlistAndCart(User user) {
        Wishlist wishlist = new Wishlist();
        wishlist.setBuyerWishlist(user);
        wishlistRepository.save(wishlist);

        Cart cart = new Cart();
        cart.setBuyerCart(user);
        cartRepository.save(cart);
    }

    public void saveProductInWishlist(Product product, int buyerId) {
        Wishlist wishlist = findBuyerById(buyerId).getWishlist();
        for (Product p : wishlist.getWishlistProducts()) {
            if (p == product)
                return;
        }
        wishlist.getWishlistProducts().add(product);
        wishlistRepository.save(wishlist);
    }

    public List<Product> findProductsInWishlist(int buyerId) {
        User buyer = findBuyerById(buyerId);
        return buyer.getWishlist().getWishlistProducts();
    }

    public List<Product> removeProductFromWishlist(int buyerId, int productId) throws ProductNotFoundException {
        Wishlist wishlist = findBuyerById(buyerId).getWishlist();
        wishlist.getWishlistProducts().remove(productService.findProductById(productId));
        wishlistRepository.save(wishlist);
        return findProductsInWishlist(buyerId);
    }

    public List<PurchaseProductDetails> findAllProductsInCart(int buyerId) {
        return purchaseProductRepository.productsInCart(findBuyerById(buyerId).getCart().getCartId());
    }

    public void saveProductToCart(int buyerId, Product product, String size, String colour, int quantity) {
        Optional<PurchaseProduct> optionalPurchaseProduct = purchaseProductRepository.purchaseProduct(product.getProductId(), colour, size, findBuyerById(buyerId).getCart().getCartId());
        if (optionalPurchaseProduct.isPresent()) {
            PurchaseProduct purchaseProduct = optionalPurchaseProduct.get();
            purchaseProduct.setQuantity(purchaseProduct.getQuantity() + quantity);
            purchaseProductRepository.save(purchaseProduct);
        } else {
            List<PurchaseProduct> cartProducts = findBuyerById(buyerId).getCart().getCartProducts();
            PurchaseProduct purchaseProduct = new PurchaseProduct(size, colour, quantity);
            cartProducts.add(purchaseProduct);
            purchaseProductRepository.save(purchaseProduct);
            product.getPurchaseProduct().add(purchaseProduct);
            productRepository.save(product);
        }
    }

    public void removeProductFromCart(int purchaseProductId) {
        Optional<PurchaseProduct> purchaseProduct = purchaseProductRepository.findById(purchaseProductId);
        purchaseProduct.ifPresent(product -> purchaseProductRepository.delete(product));
    }

    public void updateQuantity(int purchaseProductId, int quantity)
    {
        Optional<PurchaseProduct> optionalPurchaseProduct = purchaseProductRepository.findById(purchaseProductId);
        if(optionalPurchaseProduct.isPresent())
        {
            PurchaseProduct purchaseProduct = optionalPurchaseProduct.get();
            purchaseProduct.setQuantity(quantity);
            purchaseProductRepository.save(purchaseProduct);
        }

    }
}
