package com.simformsolutions.shop.dto;

import java.math.BigDecimal;

public interface PurchaseProductDetails {
    String getName();

    BigDecimal getPrice();

    String getImage();

    int getProductId();

    String getSize();

    String getColour();

    int getQuantity();

    int getCartId();

}
