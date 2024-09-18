package com.supermatech.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/brand")
public class BrandRessource {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @GetMapping
    public Map<String, Object> getAllBrand() {
        HashMap<String, Object> res = new HashMap<>();
        Query query = em.createNativeQuery("SELECT DISTINCT pro_mark FROM product ORDER BY pro_mark ASC");
        res.put("brand", query.getResultList());
        return res;
    }
}
