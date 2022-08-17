package com.simformsolutions.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Colour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int colourId;
    private String name;

    @ManyToMany(mappedBy = "colours")
    private List<Product> products;
}
