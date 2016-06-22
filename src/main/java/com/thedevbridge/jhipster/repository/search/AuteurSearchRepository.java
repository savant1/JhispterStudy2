package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Auteur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Auteur entity.
 */
public interface AuteurSearchRepository extends ElasticsearchRepository<Auteur, Long> {
}
