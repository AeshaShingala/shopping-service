package com.simformsolutions.shop.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
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