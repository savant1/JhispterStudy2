package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Noter;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Noter entity.
 */
@SuppressWarnings("unused")
public interface NoterRepository extends JpaRepository<Noter,Long> {

}
