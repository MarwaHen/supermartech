package com.supermatech.web.rest;

import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonElement;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.supermatech.domain.Order;
import com.supermatech.domain.Product;
import io.swagger.v3.core.util.Json;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.util.*;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

class PaymentProduct {

    int id;
    int quantity;

    public PaymentProduct(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}

class Quantity {

    PaymentProduct paymentProduct;
    Product product;

    public Quantity(PaymentProduct pp, Product p) {
        paymentProduct = pp;
        product = p;
    }
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
        JsonArray jsonArray = (JsonArray) post.get("cart_list");
        ArrayList<PaymentProduct> paymentProducts = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            paymentProducts.add(new PaymentProduct(jsonObject.get("product_id").getAsInt(), jsonObject.get("quantity").getAsInt()));
        }
        ArrayList<Quantity> products = new ArrayList<Quantity>();
        boolean isValid = true;
        ArrayList<PaymentProduct> quantityError = new ArrayList<>();

        for (PaymentProduct paymentProduct : paymentProducts) {
            // Locking
            try {
                // Récupération du produit avec un accès en écriture
                products.add(new Quantity(paymentProduct, em.find(Product.class, paymentProduct.id, LockModeType.PESSIMISTIC_WRITE)));
                if (products.get(-1).product.getPro_quantity() < products.get(-1).paymentProduct.quantity) {
                    isValid = false;
                    quantityError.add(
                        new PaymentProduct(
                            products.get(-1).paymentProduct.id,
                            products.get(-1).paymentProduct.quantity - products.get(-1).product.getPro_quantity()
                        )
                    );
                }
            } catch (PessimisticLockException | LockTimeoutException e) {
                res.put("Error", true);
                return res;
            }
        }
        if (!isValid) {
            return res;
        }

        //creer order
        //em.createQuery()
        //for (Quantity quantity : products) {
        //creer orderlines et update products
        //}

        return res;
    }
}
