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
	Long findByAuteurId(Long id);
	Long countByAuteurId(Long id);
	Long countByAuteurNom(String nom);

}
