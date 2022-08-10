package com.simformsolutions.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String name;
    private BigDecimal price;
    private String image;
    private String description;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "product_colour", joinColumns = @JoinColumn(name = "productId"), inverseJoinColumns = @JoinColumn(name = "colourId"))
    private List<Colour> colours;

    @ManyToMany(mappedBy = "products",cascade = CascadeType.PERSIST)
    private List<User> users;

    @ManyToMany(mappedBy = "wishlistProducts",cascade = CascadeType.PERSIST)
    private List<Wishlist> wishlist;

    @ElementCollection(targetClass = Size.class)
    @CollectionTable(name = "product_size", joinColumns = @JoinColumn(name = "productId"))
    @Enumerated(EnumType.STRING)
    private List<Size> sizes;

    @OneToMany(targetEntity = CartProduct.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private List<CartProduct> cartProduct;

}
