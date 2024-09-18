package com.supermatech.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ImagePro.
 */
@Entity
@Table(name = "image_pro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagePro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "\"imgP_id\"")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "\"imgP_Path\"", length = 255, nullable = false)
    private String imgP_Path;

    @NotNull
    @Column(name = "pro_id", nullable = false, insertable = false, updatable = false)
    private Integer pro_id;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "pro_id", referencedColumnName = "pro_id")
    @JsonIgnoreProperties(value = { "subCategory" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImagePro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgP_Path() {
        return this.imgP_Path;
    }

    public ImagePro imgP_Path(String imgP_Path) {
        this.setImgP_Path(imgP_Path);
        return this;
    }

    public void setImgP_Path(String imgP_Path) {
        this.imgP_Path = imgP_Path;
    }

    public Integer getPro_id() {
        return this.pro_id;
    }

    public ImagePro pro_id(Integer pro_id) {
        this.setPro_id(pro_id);
        return this;
    }

    public void setPro_id(Integer pro_id) {
        this.pro_id = pro_id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ImagePro product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagePro)) {
            return false;
        }
        return getId() != null && getId().equals(((ImagePro) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagePro{" +
            "imgP_id=" + getId() +
            ", imgP_Path='" + getImgP_Path() + "'" +
            ", pro_id=" + getPro_id() +
            "}";
    }
}
