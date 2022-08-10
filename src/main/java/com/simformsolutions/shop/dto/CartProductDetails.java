package com.simformsolutions.shop.dto;

import java.math.BigDecimal;

public interface CartProductDetails {
    BigDecimal getPrice();

    String getImage();

    int getCartId();

    String getSize();

    String getColour();

    int getProductId();

    int getQuantity();

    int getCartProductId();

    String getName();

}
