package com.simformsolutions.shop.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchaseId;
    @Lob
    private byte[] invoice;
    private String shippingAddress;
    private BigDecimal amount;

    @OneToMany(targetEntity = CartProduct.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "purchaseId", referencedColumnName = "purchaseId")
    private List<CartProduct> purchasedProducts = new ArrayList<>();

    public void addPurchasedProducts(List<CartProduct> cartProducts){
        this.purchasedProducts.addAll(cartProducts);
    }
}