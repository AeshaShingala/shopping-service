package com.simformsolutions.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wishlistId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "wishlist_product", joinColumns = @JoinColumn(name = "wishlistId"), inverseJoinColumns = @JoinColumn(name = "productId"))
    private List<Product> wishlistProducts;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private User buyerWishlist;
}