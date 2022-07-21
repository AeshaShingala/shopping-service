package com.simformsolutions.shop.repository;

import com.simformsolutions.shop.entity.Colour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColourRepository extends JpaRepository<Colour, Integer> {

    Colour findByName(String name);
}