package com.simformsolutions.shop.constants;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class ReportConstants {

    public static final String INVOICE_FILENAME = System.getProperty("user.dir") + "/src/main/webapp/invoices";
    public static final String INVOICE_FILEPATH;

    static {
        try {
            INVOICE_FILEPATH = new ClassPathResource("").getFile().getAbsolutePath() + "/orderInvoice.jrxml";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String PRODUCT_FILEPATH = System.getProperty("user.dir") + "/src/main/webapp/productImages";
    public static final String BILLING_ADDRESS = "billingAddress";
    public static final String SHIPPING_ADDRESS = "shippingAddress";
    public static final String INVOICE_NAME = "invoiceName";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static UriComponentsBuilder INVOICE_URL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/buyer/invoice/");
}

