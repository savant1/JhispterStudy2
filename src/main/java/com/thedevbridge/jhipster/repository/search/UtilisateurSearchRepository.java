package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Utilisateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Utilisateur entity.
 */
public interface UtilisateurSearchRepository extends ElasticsearchRepository<Utilisateur, Long> {
}
