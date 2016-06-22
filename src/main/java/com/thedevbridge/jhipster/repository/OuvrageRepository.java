package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Ouvrage;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Ouvrage entity.
 */
@SuppressWarnings("unused")
public interface OuvrageRepository extends JpaRepository<Ouvrage,Long> {

}
