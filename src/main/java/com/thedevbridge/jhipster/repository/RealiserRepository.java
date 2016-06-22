package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Realiser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Realiser entity.
 */
@SuppressWarnings("unused")
public interface RealiserRepository extends JpaRepository<Realiser,Long> {

}
