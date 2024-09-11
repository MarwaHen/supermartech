package com.supermatech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderLine.
 */
@Entity
@Table(name = "order_line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "odl_id")
    private Long id;

    @NotNull
    @Column(name = "odl_itemquantity", nullable = false)
    private Integer odl_itemquantity;

    @NotNull
    @Column(name = "odl_soldprice", nullable = false)
    private Double odl_soldprice;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "subCategory" }, allowSetters = true)
    private Product product;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "user", "orderLines" }, allowSetters = true)
    private Order order;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOdl_itemquantity() {
        return this.odl_itemquantity;
    }

    public OrderLine odl_itemquantity(Integer odl_itemquantity) {
        this.setOdl_itemquantity(odl_itemquantity);
        return this;
    }

    public void setOdl_itemquantity(Integer odl_itemquantity) {
        this.odl_itemquantity = odl_itemquantity;
    }

    public Double getOdl_soldprice() {
        return this.odl_soldprice;
    }

    public OrderLine odl_soldprice(Double odl_soldprice) {
        this.setOdl_soldprice(odl_soldprice);
        return this;
    }

    public void setOdl_soldprice(Double odl_soldprice) {
        this.odl_soldprice = odl_soldprice;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderLine product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Order getOrder() {
        return this.order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderLine order(Order order) {
        this.setOrder(order);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderLine)) {
            return false;
        }
        return getId() != null && getId().equals(((OrderLine) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderLine{" +
            "id=" + getId() +
            ", odl_itemquantity=" + getOdl_itemquantity() +
            ", odl_soldprice=" + getOdl_soldprice() +
            "}";
    }
}
