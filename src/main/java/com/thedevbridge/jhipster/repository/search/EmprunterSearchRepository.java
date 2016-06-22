package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Emprunter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Emprunter entity.
 */
public interface EmprunterSearchRepository extends ElasticsearchRepository<Emprunter, Long> {
}
