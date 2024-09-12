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

class PaymentProduct {

    int id;
    int quantity;

    public PaymentProduct(int id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
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
            Command command = objectMapper.readValue(post, Command.class);
            res.put("test", command.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return res;
    }
}
