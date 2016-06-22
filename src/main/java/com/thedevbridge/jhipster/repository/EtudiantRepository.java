package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Etudiant;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Etudiant entity.
 */
@SuppressWarnings("unused")
public interface EtudiantRepository extends JpaRepository<Etudiant,Long> {

}
