package com.supermatech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "pro_id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "pro_name", length = 255, nullable = false)
    private String pro_name;

    @Size(max = 255)
    @Column(name = "pro_description", length = 255)
    private String pro_description;

    @NotNull
    @Column(name = "pro_price", nullable = false)
    private Double pro_price;

    @NotNull
    @Column(name = "pro_quantity", nullable = false)
    private Integer pro_quantity;

    @NotNull
    @Column(name = "catt_id", nullable = false, insertable = false, updatable = false)
    private Integer catt_id;

    @Column(name = "pro_date")
    private Instant pro_date;

    @Column(name = "pro_promotion")
    private Integer pro_promotion;

    @Size(max = 255)
    @Column(name = "pro_mark", length = 255)
    private String pro_mark;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "catt_id", referencedColumnName = "catt_id")
    @JsonIgnoreProperties(value = { "category" }, allowSetters = true)
    private SubCategory subCategory;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPro_name() {
        return this.pro_name;
    }

    public Product pro_name(String pro_name) {
        this.setPro_name(pro_name);
        return this;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_description() {
        return this.pro_description;
    }

    public Product pro_description(String pro_description) {
        this.setPro_description(pro_description);
        return this;
    }

    public void setPro_description(String pro_description) {
        this.pro_description = pro_description;
    }

    public Double getPro_price() {
        return this.pro_price;
    }

    public Product pro_price(Double pro_price) {
        this.setPro_price(pro_price);
        return this;
    }

    public void setPro_price(Double pro_price) {
        this.pro_price = pro_price;
    }

    public Integer getPro_quantity() {
        return this.pro_quantity;
    }

    public Product pro_quantity(Integer pro_quantity) {
        this.setPro_quantity(pro_quantity);
        return this;
    }

    public void setPro_quantity(Integer pro_quantity) {
        this.pro_quantity = pro_quantity;
    }

    public Integer getCatt_id() {
        return this.catt_id;
    }

    public Product catt_id(Integer catt_id) {
        this.setCatt_id(catt_id);
        return this;
    }

    public void setCatt_id(Integer catt_id) {
        this.catt_id = catt_id;
    }

    public Instant getPro_date() {
        return this.pro_date;
    }

    public Product pro_date(Instant pro_date) {
        this.setPro_date(pro_date);
        return this;
    }

    public void setPro_date(Instant pro_date) {
        this.pro_date = pro_date;
    }

    public Integer getPro_promotion() {
        return this.pro_promotion;
    }

    public Product pro_promotion(Integer pro_promotion) {
        this.setPro_promotion(pro_promotion);
        return this;
    }

    public void setPro_promotion(Integer pro_promotion) {
        this.pro_promotion = pro_promotion;
    }

    public String getPro_mark() {
        return this.pro_mark;
    }

    public Product pro_mark(String pro_mark) {
        this.setPro_mark(pro_mark);
        return this;
    }

    public void setPro_mark(String pro_mark) {
        this.pro_mark = pro_mark;
    }

    public SubCategory getSubCategory() {
        return this.subCategory;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public Product subCategory(SubCategory subCategory) {
        this.setSubCategory(subCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "pro_id=" + getId() +
            ", pro_name='" + getPro_name() + "'" +
            ", pro_description='" + getPro_description() + "'" +
            ", pro_price=" + getPro_price() +
            ", pro_quantity=" + getPro_quantity() +
            ", catt_id=" + getCatt_id() +
            ", pro_date='" + getPro_date() + "'" +
            ", pro_promotion=" + getPro_promotion() +
            ", pro_mark='" + getPro_mark() + "'" +
            "}";
    }
}
