package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Matiere;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Matiere entity.
 */
@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {
    default Optional<Matiere> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Matiere> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Matiere> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select matiere from Matiere matiere left join fetch matiere.enseignant",
        countQuery = "select count(matiere) from Matiere matiere"
    )
    Page<Matiere> findAllWithToOneRelationships(Pageable pageable);

    @Query("select matiere from Matiere matiere left join fetch matiere.enseignant")
    List<Matiere> findAllWithToOneRelationships();

    @Query("select matiere from Matiere matiere left join fetch matiere.enseignant where matiere.id =:id")
    Optional<Matiere> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select matiere from Matiere matiere join fetch matiere.enseignant enseignant where enseignant.id = :enseignantId")
    List<Matiere> findByEnseignantId(@Param("enseignantId") Long enseignantId);
}
