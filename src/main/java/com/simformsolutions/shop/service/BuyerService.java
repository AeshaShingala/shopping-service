package com.simformsolutions.shop.service;

import com.simformsolutions.shop.dto.CartProductDetails;
import com.simformsolutions.shop.dto.PurchaseDetails;
import com.simformsolutions.shop.dto.UserDetails;
import com.simformsolutions.shop.entity.*;
import com.simformsolutions.shop.exception.ProductNotFoundException;
import com.simformsolutions.shop.exception.SellerNotFoundException;
import com.simformsolutions.shop.repository.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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
    CartProductRepository cartProductRepository;
    @Autowired
    CartRepository cartRepository;

    @Autowired
    PurchaseRepository purchaseRepository;

    static String filePath;
    static String filename = System.getProperty("user.dir") + "/src/main/webapp/invoices";

    static {
        try {
            filePath = new ClassPathResource("").getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public void removeProductFromWishlist(int buyerId, int productId) throws ProductNotFoundException {
        Wishlist wishlist = findBuyerById(buyerId).getWishlist();
        wishlist.getWishlistProducts().remove(productService.findProductById(productId));
        wishlistRepository.save(wishlist);
    }

    public List<CartProductDetails> findAllProductsInCart(int buyerId) {
        return cartProductRepository.allProductsInCart(findBuyerById(buyerId).getCart().getCartId());
    }

    public void saveProductToCart(int buyerId, Product product, String size, String colour, int quantity) {
        Optional<CartProduct> optionalCartProduct = cartProductRepository.findProductInCart(product.getProductId(), colour, size, findBuyerById(buyerId).getCart().getCartId());
        if (optionalCartProduct.isPresent()) {
            CartProduct cartProduct = optionalCartProduct.get();
            cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
            cartProductRepository.save(cartProduct);
        } else {
            List<CartProduct> cartProducts = findBuyerById(buyerId).getCart().getCartProducts();
            CartProduct cartProduct = new CartProduct(size, colour, quantity);
            cartProducts.add(cartProduct);
            cartProductRepository.save(cartProduct);
            product.getCartProduct().add(cartProduct);
            productRepository.save(product);
        }
    }

    public void removeProductFromCart(int cartProductId) {
        Optional<CartProduct> cartProduct = cartProductRepository.findById(cartProductId);
        cartProduct.ifPresent(product -> cartProductRepository.delete(product));
    }

    public void updateQuantity(int cartProductId, int quantity) {
        Optional<CartProduct> optionalCartProduct = cartProductRepository.findById(cartProductId);
        if (optionalCartProduct.isPresent()) {
            CartProduct cartProduct = optionalCartProduct.get();
            cartProduct.setQuantity(quantity);
            cartProductRepository.save(cartProduct);
        }
    }

    public void generateInvoice(int buyerId, String shippingAddress, Purchase purchase, BigDecimal amount) throws JRException {
        User buyer = findBuyerById(buyerId);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("billingAddress", buyer.getAddress());
        parameters.put("shippingAddress", shippingAddress);
        parameters.put("purchaseId", purchase.getPurchaseId());
        parameters.put("amount", amount);
        parameters.put("date", new Date());

        List<PurchaseDetails> cartProductList = purchaseRepository.purchaseDetails(buyer.getCart().getCartId());
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(cartProductList);
        JasperReport jasperReport = JasperCompileManager.compileReport(filePath + "/orderInvoice.jrxml");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrBeanCollectionDataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, filename + "/" + buyer.getName() + "" + purchase.getPurchaseId() + ".pdf");
        purchase.setInvoice(JasperExportManager.exportReportToPdf(jasperPrint));
    }

    public void updateCart(int buyerId, String shippingAddress, BigDecimal amount) throws JRException {
        User user = findBuyerById(buyerId);
        Purchase purchase = new Purchase();
        purchase.setShippingAddress(shippingAddress);
        purchase.setAmount(amount);
        purchase.addPurchasedProducts(user.getCart().getCartProducts());
        user.getPurchases().add(purchaseRepository.save(purchase));

        generateInvoice(buyerId, shippingAddress, purchase, amount);

        user.getCart().getCartProducts().clear();
        userRepository.save(user);
    }

}
