package com.simformsolutions.shop.service;

import com.simformsolutions.shop.entity.Purchase;
import com.simformsolutions.shop.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    PurchaseRepository purchaseRepository;

    public Purchase findPurchaseByInvoiceName(String invoiceName) throws Exception {
        Optional<Purchase> purchaseOptional = purchaseRepository.findByInvoiceName(invoiceName);
        if (purchaseOptional.isPresent())
            return purchaseOptional.get();
        throw new Exception();
    }
}
