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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_colour", joinColumns = @JoinColumn(name = "productId"), inverseJoinColumns = @JoinColumn(name = "colourId"))
    private List<Colour> colours;

    @ManyToMany(mappedBy = "products")
    private List<User> sellers;

    @ManyToMany(mappedBy = "wishlistProducts")
    private List<Wishlist> wishlist;

    @ElementCollection(targetClass = Size.class)
    @CollectionTable(name = "product_size", joinColumns = @JoinColumn(name = "productId"))
    @Enumerated(EnumType.STRING)
    private List<Size> sizes;

    @OneToMany(targetEntity = PurchaseProduct.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private List<PurchaseProduct> purchaseProduct;

}
