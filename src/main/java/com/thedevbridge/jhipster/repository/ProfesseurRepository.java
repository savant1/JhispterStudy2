package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Professeur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Professeur entity.
 */
@SuppressWarnings("unused")
public interface ProfesseurRepository extends JpaRepository<Professeur,Long> {
    Professeur findByUtilisateurNom(String nom);
}
