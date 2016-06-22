package com.thedevbridge.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thedevbridge.jhipster.domain.Recommander;
import com.thedevbridge.jhipster.repository.RecommanderRepository;
import com.thedevbridge.jhipster.repository.search.RecommanderSearchRepository;
import com.thedevbridge.jhipster.web.rest.util.HeaderUtil;
import com.thedevbridge.jhipster.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Recommander.
 */
@RestController
@RequestMapping("/api")
public class RecommanderResource {

    private final Logger log = LoggerFactory.getLogger(RecommanderResource.class);
        
    @Inject
    private RecommanderRepository recommanderRepository;
    
    @Inject
    private RecommanderSearchRepository recommanderSearchRepository;
    
    /**
     * POST  /recommanders : Create a new recommander.
     *
     * @param recommander the recommander to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recommander, or with status 400 (Bad Request) if the recommander has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/recommanders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recommander> createRecommander(@RequestBody Recommander recommander) throws URISyntaxException {
        log.debug("REST request to save Recommander : {}", recommander);
        if (recommander.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("recommander", "idexists", "A new recommander cannot already have an ID")).body(null);
        }
        Recommander result = recommanderRepository.save(recommander);
        recommanderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/recommanders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("recommander", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recommanders : Updates an existing recommander.
     *
     * @param recommander the recommander to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recommander,
     * or with status 400 (Bad Request) if the recommander is not valid,
     * or with status 500 (Internal Server Error) if the recommander couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/recommanders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recommander> updateRecommander(@RequestBody Recommander recommander) throws URISyntaxException {
        log.debug("REST request to update Recommander : {}", recommander);
        if (recommander.getId() == null) {
            return createRecommander(recommander);
        }
        Recommander result = recommanderRepository.save(recommander);
        recommanderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("recommander", recommander.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recommanders : get all the recommanders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of recommanders in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/recommanders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Recommander>> getAllRecommanders(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Recommanders");
        Page<Recommander> page = recommanderRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/recommanders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /recommanders/:id : get the "id" recommander.
     *
     * @param id the id of the recommander to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recommander, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/recommanders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Recommander> getRecommander(@PathVariable Long id) {
        log.debug("REST request to get Recommander : {}", id);
        Recommander recommander = recommanderRepository.findOne(id);
        return Optional.ofNullable(recommander)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /recommanders/:id : delete the "id" recommander.
     *
     * @param id the id of the recommander to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/recommanders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRecommander(@PathVariable Long id) {
        log.debug("REST request to delete Recommander : {}", id);
        recommanderRepository.delete(id);
        recommanderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("recommander", id.toString())).build();
    }

    /**
     * SEARCH  /_search/recommanders?query=:query : search for the recommander corresponding
     * to the query.
     *
     * @param query the query of the recommander search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/recommanders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Recommander>> searchRecommanders(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Recommanders for query {}", query);
        Page<Recommander> page = recommanderSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/recommanders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
