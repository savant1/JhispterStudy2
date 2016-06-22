package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Realiser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Realiser entity.
 */
public interface RealiserSearchRepository extends ElasticsearchRepository<Realiser, Long> {
}
