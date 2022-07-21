package com.simformsolutions.shop.dto;

import com.simformsolutions.shop.entity.Colour;
import com.simformsolutions.shop.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails {

    private int sellerId;
    private String name;
    private String description;
    private BigDecimal price;
    private int categoryId;
    private MultipartFile image;
    private List<Size> size;
    private List<Colour> colour;
}
