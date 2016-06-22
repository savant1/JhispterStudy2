package com.thedevbridge.jhipster.repository.search;

import com.thedevbridge.jhipster.domain.Commenter;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Commenter entity.
 */
public interface CommenterSearchRepository extends ElasticsearchRepository<Commenter, Long> {
}
