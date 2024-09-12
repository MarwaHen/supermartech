package com.supermatech.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.supermatech.domain.Order;
import com.supermatech.domain.Product;
import io.swagger.v3.core.util.Json;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import java.util.*;
import org.h2.command.Command;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.springframework.web.bind.annotation.*;

class CartProduct {

    int id;
    int quantity;
}

class Cart {

    int id_client;
    int address;
    String phone_number;
    CartProduct cart_list;
}

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PersistenceContext
    private EntityManager em;

    @PostMapping
    public Map<String, Object> processPayment(@RequestBody String post) {
        HashMap<String, Object> res = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Cart command = objectMapper.readValue(post, Cart.class);
            res.put("test", 0);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
}
