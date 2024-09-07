package com.supermatech.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Category.
 */
@Entity
@Table(name = "category_cat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "cat_id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "cat_name", length = 255, nullable = false)
    private String cat_name;

    @Size(max = 255)
    @Column(name = "cat_description", length = 255)
    private String cat_description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCat_name() {
        return this.cat_name;
    }

    public Category cat_name(String cat_name) {
        this.setCat_name(cat_name);
        return this;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_description() {
        return this.cat_description;
    }

    public Category cat_description(String cat_description) {
        this.setCat_description(cat_description);
        return this;
    }

    public void setCat_description(String cat_description) {
        this.cat_description = cat_description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return getId() != null && getId().equals(((Category) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "cat_id=" + getId() +
            ", cat_name='" + getCat_name() + "'" +
            ", cat_description='" + getCat_description() + "'" +
            "}";
    }
}
