package com.simformsolutions.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String name;
    private BigDecimal price;
    private String image;
    private String description;
    private boolean isAvailable;

    @ManyToMany
    @JoinTable(name = "product_colour", joinColumns = @JoinColumn(name = "productId"), inverseJoinColumns = @JoinColumn(name = "colourId"))
    private List<Colour> colours = new ArrayList<>();

    @ManyToMany(mappedBy = "products")
    private List<User> users = new ArrayList<>();

    @ElementCollection(targetClass = Size.class)
    @CollectionTable(name = "product_size", joinColumns = @JoinColumn(name = "productId"))
    @Enumerated(EnumType.STRING)
    private List<Size> sizes = new ArrayList<>();

}
