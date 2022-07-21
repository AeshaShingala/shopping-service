package com.simformsolutions.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String address;
    @Column(length = 20)
    private String contact;
    private String hasRole;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_product", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "productId"))
    private List<Product> products = new ArrayList<>();

    @OneToMany(targetEntity = Purchase.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private List<Purchase> purchases;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private List<Role> roles = new ArrayList<>();

    public void setRole(Role role) {
        this.roles.add(role);
    }

    public void setProduct(Product product) {
        this.products.add(product);
    }
}