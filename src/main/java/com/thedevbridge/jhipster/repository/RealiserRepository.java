package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Ouvrage;
import com.thedevbridge.jhipster.domain.Realiser;

import org.jboss.logging.annotations.Param;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Realiser entity.
 */
@SuppressWarnings("unused")
public interface RealiserRepository extends JpaRepository<Realiser,Long> {
//	@Query("select * from realiser r,auteur a,ouvrage o "
//			+ "where r.auteur = :a.id and a.ouvrage = :o.ouvrage ,")
//	Realiser findByIdAndOuvrage(@Param("a.id") Long id,
//            @Param("ouvage") Ouvrage ouvrage);
	Long findByAuteurId(Long id);
	Long countByAuteurId(Long id);
}
