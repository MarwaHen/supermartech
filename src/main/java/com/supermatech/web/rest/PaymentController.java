package com.supermatech.web.rest;

import com.supermatech.domain.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.*;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.hibernate.processor.xml.jaxb.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

class PaymentProduct {

    int id;
    int quantity;
}

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PersistenceContext
    private EntityManager em;

    @PostMapping
    public Map<String, Object> processPayment(@RequestBody Map<String, Object> post) {
        HashMap<String, Object> res = new HashMap<>();

        // Récupérer les informations du JSON
        int id_client = (int) post.get("id_client");
        String adresse = (String) post.get("address");
        String phone_number = (String) post.get("phone_number");
        ArrayList<PaymentProduct> paymentProducts = (ArrayList<PaymentProduct>) post.get("cart_list");

        ArrayList<Product> products = new ArrayList<Product>();

        for (PaymentProduct paymentProduct : paymentProducts) {
            // Locking
            try {
                // Récupération du produit avec un accès en écriture
                products.add(em.find(Product.class, paymentProduct.id, LockModeType.PESSIMISTIC_WRITE));
            } catch (PessimisticLockException | LockTimeoutException e) {
                res.put("Error", true);
                return res;
            }
        }

        return res;
    }
}
