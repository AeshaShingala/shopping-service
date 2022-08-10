package com.simformsolutions.shop.dto;


import java.math.BigDecimal;

public interface PurchaseDetails {
    String getName();
    int getQuantity();
    BigDecimal getPrice();
}
