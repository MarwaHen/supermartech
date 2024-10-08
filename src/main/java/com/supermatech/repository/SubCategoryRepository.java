package com.supermatech.repository;

import com.supermatech.domain.SubCategory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubCategory entity.
 */
@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    default Optional<SubCategory> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SubCategory> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SubCategory> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select subCategory from SubCategory subCategory left join fetch subCategory.category",
        countQuery = "select count(subCategory) from SubCategory subCategory"
    )
    Page<SubCategory> findAllWithToOneRelationships(Pageable pageable);

    @Query("select subCategory from SubCategory subCategory left join fetch subCategory.category")
    List<SubCategory> findAllWithToOneRelationships();

    @Query("select subCategory from SubCategory subCategory left join fetch subCategory.category where subCategory.id =:id")
    Optional<SubCategory> findOneWithToOneRelationships(@Param("id") Long id);
}
