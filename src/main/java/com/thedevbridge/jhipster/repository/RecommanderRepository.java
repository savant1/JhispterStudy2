package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Recommander;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Recommander entity.
 */
@SuppressWarnings("unused")
public interface RecommanderRepository extends JpaRepository<Recommander,Long> {

}
