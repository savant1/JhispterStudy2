package com.thedevbridge.jhipster.repository;

import com.thedevbridge.jhipster.domain.Commenter;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Commenter entity.
 */
@SuppressWarnings("unused")
public interface CommenterRepository extends JpaRepository<Commenter,Long> {

}
