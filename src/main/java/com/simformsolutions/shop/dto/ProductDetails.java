package com.simformsolutions.shop.dto;

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

    private int categoryId;
    private String name;
    private BigDecimal price;
    private String description;
    private MultipartFile image;
    private List<Size> size;
    private List<String> colour;
}
