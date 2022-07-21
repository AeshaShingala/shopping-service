package com.simformsolutions.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int colourId;
    private String name;

    @ManyToMany(mappedBy = "colours", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}
