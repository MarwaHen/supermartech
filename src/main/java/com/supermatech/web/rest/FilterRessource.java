package com.supermatech.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supermatech.domain.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class Filter {

    @JsonProperty("name")
    String name;

    @JsonProperty("sub_cat")
    int sub_cat;

    @JsonProperty("brand")
    List<String> brand;

    @JsonProperty("max_price")
    int max_price;

    @JsonProperty("min_price")
    int min_price;

    @JsonProperty("promo")
    boolean promo;

    @JsonProperty("added_after")
    Date added_after;

    @JsonProperty("size")
    int size;

    @JsonProperty("page")
    int page;
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

        // Parse the incoming filter JSON
        Filter filter = objectMapper.readValue(post, Filter.class);

        // Initialize JPQL query builder
        StringBuilder jpql = new StringBuilder();
        jpql.append("SELECT p FROM Product p");

        boolean first = true;

        // Filter by subcategory
        if (filter.sub_cat != -1) {
            jpql.append(" WHERE ");
            first = false;
            jpql.append("p.catt_id = ").append(filter.sub_cat);
        }

        // Filter by promotion
        if (filter.promo) {
            jpql.append(first ? " WHERE " : " AND ");
            first = false;
            jpql.append("p.pro_promotion > 0");
        }

        System.out.println("JPQL Query: " + jpql.toString());

        // Filter by date added (after 01/01/2000)
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        if (filter.added_after != null && filter.added_after.after(calendar.getTime())) {
            jpql.append(first ? " WHERE " : " AND ");
            first = false;
            jpql.append("p.pro_date >= :added_after");
        }

        // Filter by brand (multiple options with IN clause)
        if (filter.brand != null && !filter.brand.isEmpty()) {
            jpql.append(first ? " WHERE " : " AND ");
            jpql.append("p.pro_mark IN :brands");
            first = false;
        }

        if (!Objects.equals(filter.name, "")) {
            jpql.append(first ? " WHERE " : " AND ");
            jpql.append("pro_name ILIKE '");
            jpql.append(filter.name);
            jpql.append("%'");
            first = false;
        }

        // Create the JPQL query
        TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);

        // Set parameters dynamically
        if (filter.added_after != null && filter.added_after.after(calendar.getTime())) {
            query.setParameter("added_after", filter.added_after);
        }

        if (filter.brand != null && !filter.brand.isEmpty()) {
            query.setParameter("brands", filter.brand);
        }

        // Check Price
        List<Product> result = query.getResultList();
        ArrayList<Integer> toDelete = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            // Filter by minimum price
            if (
                (filter.min_price > 0 && result.get(i).get_promo_price() < filter.min_price) ||
                (filter.max_price != -1 && result.get(i).get_promo_price() > filter.max_price)
            ) {
                toDelete.add(i);
            }
        }

        for (int i = toDelete.size() - 1; i >= 0; i--) result.remove(result.get(toDelete.get(i)));

        int start = filter.page * filter.size;
        int total = result.size();
        if (start + filter.size > result.size()) result = result.subList(start, result.size());
        else result = result.subList(start, start + filter.size);
        // Execute the query and return the results
        res.put("res_list", result);
        res.put("total", total);
        return res;
    }
}
