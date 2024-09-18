package com.supermatech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubCategory.
 */
@Entity
@Table(name = "sub_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "catt_id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "catt_name", length = 255, nullable = false)
    private String catt_name;

    @Size(max = 255)
    @Column(name = "catt_description", length = 255)
    private String catt_description;

    @NotNull
    @Column(name = "cat_id", nullable = false, insertable = false, updatable = false)
    private Integer cat_id;

    @Size(max = 255)
    @Column(name = "catt_icon", length = 255)
    private String catt_icon;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "cat_id", referencedColumnName = "cat_id")
    @JsonIgnoreProperties(value = { "subCategories" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCatt_name() {
        return this.catt_name;
    }

    public SubCategory catt_name(String catt_name) {
        this.setCatt_name(catt_name);
        return this;
    }

    public void setCatt_name(String catt_name) {
        this.catt_name = catt_name;
    }

    public String getCatt_description() {
        return this.catt_description;
    }

    public SubCategory catt_description(String catt_description) {
        this.setCatt_description(catt_description);
        return this;
    }

    public void setCatt_description(String catt_description) {
        this.catt_description = catt_description;
    }

    public Integer getCat_id() {
        return this.cat_id;
    }

    public SubCategory cat_id(Integer cat_id) {
        this.setCat_id(cat_id);
        return this;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }

    public String getCatt_icon() {
        return this.catt_icon;
    }

    public SubCategory catt_icon(String catt_icon) {
        this.setCatt_icon(catt_icon);
        return this;
    }

    public void setCatt_icon(String catt_icon) {
        this.catt_icon = catt_icon;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubCategory category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((SubCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubCategory{" +
            "catt_id=" + getId() +
            ", catt_name='" + getCatt_name() + "'" +
            ", catt_description='" + getCatt_description() + "'" +
            ", cat_id=" + getCat_id() +
            ", catt_icon='" + getCatt_icon() + "'" +
            "}";
    }
}
