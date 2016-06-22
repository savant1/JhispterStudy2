package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Professeur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Professeur entity.
 */
public interface ProfesseurSearchRepository extends ElasticsearchRepository<Professeur, Long> {
}
