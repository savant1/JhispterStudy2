package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Noter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Noter entity.
 */
public interface NoterSearchRepository extends ElasticsearchRepository<Noter, Long> {
}
