package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Auteur;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Auteur entity.
 */
@SuppressWarnings("unused")
public interface AuteurRepository extends JpaRepository<Auteur,Long> {
    //ici nous mettons toutes les requetes que spring ne genere pas pour une entite
    Auteur findByName(String nom);
}
