package com.supermatech.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProductCriteriaTest {

    @Test
    void newProductCriteriaHasAllFiltersNullTest() {
        var productCriteria = new ProductCriteria();
        assertThat(productCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void productCriteriaFluentMethodsCreatesFiltersTest() {
        var productCriteria = new ProductCriteria();

        setAllFilters(productCriteria);

        assertThat(productCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void productCriteriaCopyCreatesNullFilterTest() {
        var productCriteria = new ProductCriteria();
        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void productCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var productCriteria = new ProductCriteria();
        setAllFilters(productCriteria);

        var copy = productCriteria.copy();

        assertThat(productCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(productCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var productCriteria = new ProductCriteria();

        assertThat(productCriteria).hasToString("ProductCriteria{}");
    }

    private static void setAllFilters(ProductCriteria productCriteria) {
        productCriteria.id();
        productCriteria.pro_name();
        productCriteria.pro_description();
        productCriteria.pro_price();
        productCriteria.pro_quantity();
        productCriteria.catt_id();
        productCriteria.pro_date();
        productCriteria.pro_promotion();
        productCriteria.pro_mark();
        productCriteria.subCategoryId();
        productCriteria.distinct();
    }

    private static Condition<ProductCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPro_name()) &&
                condition.apply(criteria.getPro_description()) &&
                condition.apply(criteria.getPro_price()) &&
                condition.apply(criteria.getPro_quantity()) &&
                condition.apply(criteria.getCatt_id()) &&
                condition.apply(criteria.getPro_date()) &&
                condition.apply(criteria.getPro_promotion()) &&
                condition.apply(criteria.getPro_mark()) &&
                condition.apply(criteria.getSubCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProductCriteria> copyFiltersAre(ProductCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPro_name(), copy.getPro_name()) &&
                condition.apply(criteria.getPro_description(), copy.getPro_description()) &&
                condition.apply(criteria.getPro_price(), copy.getPro_price()) &&
                condition.apply(criteria.getPro_quantity(), copy.getPro_quantity()) &&
                condition.apply(criteria.getCatt_id(), copy.getCatt_id()) &&
                condition.apply(criteria.getPro_date(), copy.getPro_date()) &&
                condition.apply(criteria.getPro_promotion(), copy.getPro_promotion()) &&
                condition.apply(criteria.getPro_mark(), copy.getPro_mark()) &&
                condition.apply(criteria.getSubCategoryId(), copy.getSubCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
