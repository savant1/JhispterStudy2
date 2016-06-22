package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Recommander;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Recommander entity.
 */
public interface RecommanderSearchRepository extends ElasticsearchRepository<Recommander, Long> {
}
