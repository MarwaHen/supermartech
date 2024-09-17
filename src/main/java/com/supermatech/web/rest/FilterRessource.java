package com.supermatech.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermatech.domain.Product;
import com.supermatech.service.impl.OrderLineServiceImpl;
import com.supermatech.service.impl.OrderServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class Filter {

    @JsonProperty("sub_cat")
    int sub_cat;

    @JsonProperty("max_price")
    int max_price;

    @JsonProperty("min_price")
    int min_price;

    @JsonProperty("promo")
    boolean promo;

    @JsonProperty("new")
    Date added_after;
}

@RestController
@RequestMapping("/api/filter")
public class FilterRessource {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @PostMapping
    public Map<String, Object> processPayment(@RequestBody String post) throws JsonProcessingException {
        HashMap<String, Object> res = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<HashMap<String, Object>> productMissing = new ArrayList<>();

        Filter filter = objectMapper.readValue(post, Filter.class);

        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT * FROM product p");
        boolean first = true;

        if (filter.sub_cat != -1) {
            jpql.append(" WHERE ");
            first = false;
            jpql.append("p.catt_id == ");
            jpql.append(filter.sub_cat);
        }
        if (filter.min_price > 0) {
            if (first) {
                jpql.append(" WHERE ");
                first = false;
            } else {
                jpql.append(" AND ");
            }
            jpql.append("p.pro_price >= ");
            jpql.append(filter.min_price);
        }
        if (filter.max_price != -1) {
            if (first) {
                jpql.append(" WHERE ");
                first = false;
            } else {
                jpql.append(" AND ");
            }
            jpql.append("p.pro_price <= ");
            jpql.append(filter.max_price);
        }
        if (filter.promo) {
            if (first) {
                jpql.append(" WHERE ");
                first = false;
            } else {
                jpql.append(" AND ");
            }
            jpql.append("p.pro_promotion > 0");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        if (filter.added_after.before(calendar.getTime())) {
            if (first) {
                jpql.append(" WHERE ");
                first = false;
            } else {
                jpql.append(" AND ");
            }
            jpql.append("p.pro_date >= ");
            jpql.append(filter.added_after);
        }

        TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);
        res.put("res_list", query.getResultList());
        return res;
    }
}
