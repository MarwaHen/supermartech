package com.supermatech.repository;

import com.supermatech.domain.ImagePro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ImagePro entity.
 */
@Repository
public interface ImageProRepository extends JpaRepository<ImagePro, Long> {
    default Optional<ImagePro> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ImagePro> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ImagePro> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select imagePro from ImagePro imagePro left join fetch imagePro.product",
        countQuery = "select count(imagePro) from ImagePro imagePro"
    )
    Page<ImagePro> findAllWithToOneRelationships(Pageable pageable);

    @Query("select imagePro from ImagePro imagePro left join fetch imagePro.product")
    List<ImagePro> findAllWithToOneRelationships();

    @Query("select imagePro from ImagePro imagePro left join fetch imagePro.product where imagePro.id =:id")
    Optional<ImagePro> findOneWithToOneRelationships(@Param("id") Long id);
}
