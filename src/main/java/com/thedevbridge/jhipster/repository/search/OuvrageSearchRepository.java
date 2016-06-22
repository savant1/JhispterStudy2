package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Ouvrage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Ouvrage entity.
 */
public interface OuvrageSearchRepository extends ElasticsearchRepository<Ouvrage, Long> {
}
