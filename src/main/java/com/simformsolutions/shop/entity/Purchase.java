package com.simformsolutions.shop.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchaseId;
    @Lob
    private byte[] invoice;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String invoiceName;
    private String shippingAddress;
    private BigDecimal amount;

    @OneToMany(targetEntity = CartProduct.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "purchaseId", referencedColumnName = "purchaseId")
    private List<CartProduct> purchasedProducts = new ArrayList<>();

    public void addPurchasedProducts(List<CartProduct> cartProducts){
        this.purchasedProducts.addAll(cartProducts);
    }
}