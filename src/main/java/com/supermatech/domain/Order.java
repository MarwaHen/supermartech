package com.supermatech.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "odr_id")
    private Long id;

    @NotNull
    @Size(max = 600)
    @Column(name = "odr_adresse", length = 600, nullable = false)
    private String odr_adresse;

    @NotNull
    @Size(max = 45)
    @Column(name = "odr_phonenumber", length = 45, nullable = false)
    private String odr_phonenumber;

    @NotNull
    @Column(name = "odr_price", nullable = false)
    private Double odr_price;

    @NotNull
    @Column(name = "odr_date", nullable = false)
    private Instant odr_date;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOdr_adresse() {
        return this.odr_adresse;
    }

    public Order odr_adresse(String odr_adresse) {
        this.setOdr_adresse(odr_adresse);
        return this;
    }

    public void setOdr_adresse(String odr_adresse) {
        this.odr_adresse = odr_adresse;
    }

    public String getOdr_phonenumber() {
        return this.odr_phonenumber;
    }

    public Order odr_phonenumber(String odr_phonenumber) {
        this.setOdr_phonenumber(odr_phonenumber);
        return this;
    }

    public void setOdr_phonenumber(String odr_phonenumber) {
        this.odr_phonenumber = odr_phonenumber;
    }

    public Double getOdr_price() {
        return this.odr_price;
    }

    public Order odr_price(Double odr_price) {
        this.setOdr_price(odr_price);
        return this;
    }

    public void setOdr_price(Double odr_price) {
        this.odr_price = odr_price;
    }

    public Instant getOdr_date() {
        return this.odr_date;
    }

    public Order odr_date(Instant odr_date) {
        this.setOdr_date(odr_date);
        return this;
    }

    public void setOdr_date(Instant odr_date) {
        this.odr_date = odr_date;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return getId() != null && getId().equals(((Order) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", odr_adresse='" + getOdr_adresse() + "'" +
            ", odr_phonenumber='" + getOdr_phonenumber() + "'" +
            ", odr_price=" + getOdr_price() +
            ", odr_date='" + getOdr_date() + "'" +
            "}";
    }
}
