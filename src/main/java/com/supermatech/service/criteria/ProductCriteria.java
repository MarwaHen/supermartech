package com.supermatech.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.supermatech.domain.Product} entity. This class is used
 * in {@link com.supermatech.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pro_name;

    private StringFilter pro_description;

    private DoubleFilter pro_price;

    private IntegerFilter pro_quantity;

    private IntegerFilter catt_id;

    private InstantFilter pro_date;

    private IntegerFilter pro_promotion;

    private StringFilter pro_mark;

    private LongFilter subCategoryId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.pro_name = other.optionalPro_name().map(StringFilter::copy).orElse(null);
        this.pro_description = other.optionalPro_description().map(StringFilter::copy).orElse(null);
        this.pro_price = other.optionalPro_price().map(DoubleFilter::copy).orElse(null);
        this.pro_quantity = other.optionalPro_quantity().map(IntegerFilter::copy).orElse(null);
        this.catt_id = other.optionalCatt_id().map(IntegerFilter::copy).orElse(null);
        this.pro_date = other.optionalPro_date().map(InstantFilter::copy).orElse(null);
        this.pro_promotion = other.optionalPro_promotion().map(IntegerFilter::copy).orElse(null);
        this.pro_mark = other.optionalPro_mark().map(StringFilter::copy).orElse(null);
        this.subCategoryId = other.optionalSubCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPro_name() {
        return pro_name;
    }

    public Optional<StringFilter> optionalPro_name() {
        return Optional.ofNullable(pro_name);
    }

    public StringFilter pro_name() {
        if (pro_name == null) {
            setPro_name(new StringFilter());
        }
        return pro_name;
    }

    public void setPro_name(StringFilter pro_name) {
        this.pro_name = pro_name;
    }

    public StringFilter getPro_description() {
        return pro_description;
    }

    public Optional<StringFilter> optionalPro_description() {
        return Optional.ofNullable(pro_description);
    }

    public StringFilter pro_description() {
        if (pro_description == null) {
            setPro_description(new StringFilter());
        }
        return pro_description;
    }

    public void setPro_description(StringFilter pro_description) {
        this.pro_description = pro_description;
    }

    public DoubleFilter getPro_price() {
        return pro_price;
    }

    public Optional<DoubleFilter> optionalPro_price() {
        return Optional.ofNullable(pro_price);
    }

    public DoubleFilter pro_price() {
        if (pro_price == null) {
            setPro_price(new DoubleFilter());
        }
        return pro_price;
    }

    public void setPro_price(DoubleFilter pro_price) {
        this.pro_price = pro_price;
    }

    public IntegerFilter getPro_quantity() {
        return pro_quantity;
    }

    public Optional<IntegerFilter> optionalPro_quantity() {
        return Optional.ofNullable(pro_quantity);
    }

    public IntegerFilter pro_quantity() {
        if (pro_quantity == null) {
            setPro_quantity(new IntegerFilter());
        }
        return pro_quantity;
    }

    public void setPro_quantity(IntegerFilter pro_quantity) {
        this.pro_quantity = pro_quantity;
    }

    public IntegerFilter getCatt_id() {
        return catt_id;
    }

    public Optional<IntegerFilter> optionalCatt_id() {
        return Optional.ofNullable(catt_id);
    }

    public IntegerFilter catt_id() {
        if (catt_id == null) {
            setCatt_id(new IntegerFilter());
        }
        return catt_id;
    }

    public void setCatt_id(IntegerFilter catt_id) {
        this.catt_id = catt_id;
    }

    public InstantFilter getPro_date() {
        return pro_date;
    }

    public Optional<InstantFilter> optionalPro_date() {
        return Optional.ofNullable(pro_date);
    }

    public InstantFilter pro_date() {
        if (pro_date == null) {
            setPro_date(new InstantFilter());
        }
        return pro_date;
    }

    public void setPro_date(InstantFilter pro_date) {
        this.pro_date = pro_date;
    }

    public IntegerFilter getPro_promotion() {
        return pro_promotion;
    }

    public Optional<IntegerFilter> optionalPro_promotion() {
        return Optional.ofNullable(pro_promotion);
    }

    public IntegerFilter pro_promotion() {
        if (pro_promotion == null) {
            setPro_promotion(new IntegerFilter());
        }
        return pro_promotion;
    }

    public void setPro_promotion(IntegerFilter pro_promotion) {
        this.pro_promotion = pro_promotion;
    }

    public StringFilter getPro_mark() {
        return pro_mark;
    }

    public Optional<StringFilter> optionalPro_mark() {
        return Optional.ofNullable(pro_mark);
    }

    public StringFilter pro_mark() {
        if (pro_mark == null) {
            setPro_mark(new StringFilter());
        }
        return pro_mark;
    }

    public void setPro_mark(StringFilter pro_mark) {
        this.pro_mark = pro_mark;
    }

    public LongFilter getSubCategoryId() {
        return subCategoryId;
    }

    public Optional<LongFilter> optionalSubCategoryId() {
        return Optional.ofNullable(subCategoryId);
    }

    public LongFilter subCategoryId() {
        if (subCategoryId == null) {
            setSubCategoryId(new LongFilter());
        }
        return subCategoryId;
    }

    public void setSubCategoryId(LongFilter subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(pro_name, that.pro_name) &&
            Objects.equals(pro_description, that.pro_description) &&
            Objects.equals(pro_price, that.pro_price) &&
            Objects.equals(pro_quantity, that.pro_quantity) &&
            Objects.equals(catt_id, that.catt_id) &&
            Objects.equals(pro_date, that.pro_date) &&
            Objects.equals(pro_promotion, that.pro_promotion) &&
            Objects.equals(pro_mark, that.pro_mark) &&
            Objects.equals(subCategoryId, that.subCategoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            pro_name,
            pro_description,
            pro_price,
            pro_quantity,
            catt_id,
            pro_date,
            pro_promotion,
            pro_mark,
            subCategoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPro_name().map(f -> "pro_name=" + f + ", ").orElse("") +
            optionalPro_description().map(f -> "pro_description=" + f + ", ").orElse("") +
            optionalPro_price().map(f -> "pro_price=" + f + ", ").orElse("") +
            optionalPro_quantity().map(f -> "pro_quantity=" + f + ", ").orElse("") +
            optionalCatt_id().map(f -> "catt_id=" + f + ", ").orElse("") +
            optionalPro_date().map(f -> "pro_date=" + f + ", ").orElse("") +
            optionalPro_promotion().map(f -> "pro_promotion=" + f + ", ").orElse("") +
            optionalPro_mark().map(f -> "pro_mark=" + f + ", ").orElse("") +
            optionalSubCategoryId().map(f -> "subCategoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
