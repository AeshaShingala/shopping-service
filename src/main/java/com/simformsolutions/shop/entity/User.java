package com.simformsolutions.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"roles"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String address;
    @Column(length = 20)
    private String contact;
    private boolean enable = false;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_product", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "productId"))
    private List<Product> products;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roles = new ArrayList<>();

    @OneToOne(mappedBy = "buyerCart")
    private Cart cart;

    @OneToOne(mappedBy = "buyerWishlist")
    private Wishlist wishlist;

    @OneToMany(targetEntity = Purchase.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private List<Purchase> purchases = new ArrayList<>();

    public void setProduct(Product product) {
        this.products.add(product);
    }
}