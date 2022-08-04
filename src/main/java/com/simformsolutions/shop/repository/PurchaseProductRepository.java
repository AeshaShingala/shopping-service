package com.simformsolutions.shop.repository;

import com.simformsolutions.shop.dto.PurchaseProductDetails;
import com.simformsolutions.shop.entity.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Integer> {

    @Query(value = "SELECT p.name as name, " +
            "p.price as price , " +
            "p.image as image, " +
            "p.product_id as productId," +
            "pp.size as size, " +
            "pp.purchase_product_id as purchaseProductId, " +
            "pp.colour as colour, " +
            "pp.quantity as quantity, " +
            "c.cart_id as cartId " +
            "FROM cart c " +
            "JOIN purchase_product pp ON c.cart_id = pp.cart_id " +
            "JOIN product p ON pp.product_id = p.product_id " +
            "WHERE c.cart_id = ?", nativeQuery = true)
    List<PurchaseProductDetails> productsInCart(int cartId);

/*    @Query(value = "SELECT p.name as name,"+
    "p.price as price ,"+
    "p.image as image,"+
    "p.product_id as productId,"+
    "pp.size as size,"+
    "pp.purchase_product_id as purchaseProductId,"+
    "pp.colour as colour,"+
    "SUM(pp.quantity) as quantity,"+
    "c.cart_id as cartId "+
    "FROM cart c "+
    "JOIN purchase_product pp ON c.cart_id = pp.cart_id "+
    "JOIN product p ON pp.product_id = p.product_id "+
    "WHERE c.cart_id = ? group by p.product_id,pp.colour,pp.size",nativeQuery = true)
    List<PurchaseProductDetails> productsInCart(int cartId);*/

    @Query(value = "SELECT * FROM purchase_product p" +
            " JOIN cart c" +
            " ON c.cart_id=p.cart_id " +
            "WHERE p.product_id=?1 " +
            "AND p.colour=?2 " +
            "AND p.size=?3 " +
            "AND c.cart_id=?4", nativeQuery = true)
    Optional<PurchaseProduct> purchaseProduct(int productId, String colour, String size, int cartId);

}
