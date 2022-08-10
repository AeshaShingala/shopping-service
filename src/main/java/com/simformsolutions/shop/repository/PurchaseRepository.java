package com.simformsolutions.shop.repository;

import com.simformsolutions.shop.dto.PurchaseDetails;
import com.simformsolutions.shop.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    @Query(value = "SELECT p.name as name," +
            "p.price as price, " +
            "cp.quantity as quantity " +
            "FROM product p " +
            "JOIN cart_product cp ON cp.product_id = p.product_id " +
            "WHERE cp.cart_id=?"
            , nativeQuery = true)
    List<PurchaseDetails> purchaseDetails(int cartId);

}