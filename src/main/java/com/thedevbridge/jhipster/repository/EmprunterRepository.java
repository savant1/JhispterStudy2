package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Emprunter;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Emprunter entity.
 */
@SuppressWarnings("unused")
public interface EmprunterRepository extends JpaRepository<Emprunter,Long> {

}
