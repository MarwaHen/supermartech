package com.supermatech.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class BrandRessource {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @PostMapping
    public Map<String, Object> getAllBrand() {
        HashMap<String, Object> res = new HashMap<>();
        Query query = em.createNativeQuery("SELECT DISTINCT pro_mark FROM product");
        res.put("brand", query.getResultList());
        return res;
    }
}
