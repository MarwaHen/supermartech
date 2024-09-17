package com.supermatech.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermatech.domain.Order;
import com.supermatech.domain.OrderLine;
import com.supermatech.domain.Product;
import com.supermatech.domain.User;
import com.supermatech.service.impl.OrderLineServiceImpl;
import com.supermatech.service.impl.OrderServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.*;
import org.hibernate.PessimisticLockException;
import org.hibernate.exception.LockTimeoutException;
import org.springframework.web.bind.annotation.*;

class CartProduct {

    @JsonProperty("product_id")
    int product_id;

    @JsonProperty("quantity")
    int quantity;

    Product product;
}

class Cart {

    @JsonProperty("address")
    String address;

    @JsonProperty("cart_list")
    ArrayList<CartProduct> cart_list;

    @JsonProperty("id_client")
    int id_client;

    @JsonProperty("phone_number")
    String phone_number;
}

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PersistenceContext
    private EntityManager em;

    private final OrderServiceImpl orderService;

    private final OrderLineServiceImpl orderLineService;

    public PaymentController(OrderServiceImpl orderService, OrderLineServiceImpl orderLineService) {
        this.orderService = orderService;
        this.orderLineService = orderLineService;
    }

    @Transactional
    @PostMapping
    public Map<String, Object> processPayment(@RequestBody String post) {
        HashMap<String, Object> res = new HashMap<>();
        boolean isValid = true;
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<HashMap<String, Object>> productMissing = new ArrayList<>();

        try {
            Cart cart = objectMapper.readValue(post, Cart.class);

            // Take lock
            for (CartProduct cartProduct : cart.cart_list) {
                cartProduct.product = new Product();
                // Récupération du produit avec un accès en écriture
                cartProduct.product = em.find(Product.class, cartProduct.product_id, LockModeType.PESSIMISTIC_WRITE);
                if (cartProduct.product.getPro_quantity() < cartProduct.quantity) {
                    productMissing.add(new HashMap<>());
                    productMissing.get(productMissing.size() - 1).put("product_id", cartProduct.product_id);
                    productMissing.get(productMissing.size() - 1).put("product_name", cartProduct.product.getPro_name());
                    productMissing
                        .get(productMissing.size() - 1)
                        .put("quantity", cartProduct.quantity - cartProduct.product.getPro_quantity());
                    isValid = false;
                }
            }

            if (!isValid) {
                res.put("cart_list", productMissing);
            } else {
                Order order = new Order();
                order.setUser(em.find(User.class, cart.id_client));
                order.setOdr_adresse(cart.address);
                order.setOdr_price(0.0);
                order.setOdr_date(new Date().toInstant());
                order.setOdr_phonenumber(cart.phone_number);
                orderService.save(order);

                for (CartProduct cartProduct : cart.cart_list) {
                    cartProduct.product.setPro_quantity(cartProduct.product.getPro_quantity() - cartProduct.quantity);
                    OrderLine orderLine = new OrderLine();
                    orderLine.setOrder(order);
                    orderLine.setProduct(cartProduct.product);
                    orderLine.setOdl_itemquantity(cartProduct.quantity);

                    Double soldprice = cartProduct.product.getPro_price();
                    Double minus = ((double) cartProduct.product.getPro_promotion() / 100.0);
                    minus = soldprice * minus;
                    soldprice = soldprice - minus;

                    orderLine.setOdl_soldprice(soldprice);
                    order.setOdr_price(order.getOdr_price() + orderLine.getOdl_soldprice() * cartProduct.quantity);
                    orderLineService.save(orderLine);
                }

                orderService.update(order);
                res.put("id", order.getId());
            }
        } catch (JsonProcessingException | PessimisticLockException | LockTimeoutException e) {
            res.put("error", e);
        }

        return res;
    }
}
