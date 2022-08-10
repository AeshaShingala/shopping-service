package com.simformsolutions.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartProductId;
    private String size;
    private String colour;
    private int quantity;

    public CartProduct(String size, String colour, int quantity) {
        this.size = size;
        this.colour = colour;
        this.quantity = quantity;
    }
}
