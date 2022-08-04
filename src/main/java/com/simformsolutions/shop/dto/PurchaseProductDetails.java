package com.simformsolutions.shop.dto;

import java.math.BigDecimal;

public interface PurchaseProductDetails {
    BigDecimal getPrice();

    String getImage();

    int getCartId();

    String getSize();

    String getColour();

    int getProductId();

    int getQuantity();

    int getPurchaseProductId();

    String getName();

}
