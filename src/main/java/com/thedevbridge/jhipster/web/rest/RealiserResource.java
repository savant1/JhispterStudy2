package com.thedevbridge.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thedevbridge.jhipster.domain.Realiser;
import com.thedevbridge.jhipster.repository.RealiserRepository;
import com.thedevbridge.jhipster.repository.search.RealiserSearchRepository;
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
 * REST controller for managing Realiser.
 */
@RestController
@RequestMapping("/api")
public class RealiserResource {

    private final Logger log = LoggerFactory.getLogger(RealiserResource.class);
        
    @Inject
    private RealiserRepository realiserRepository;
    
    @Inject
    private RealiserSearchRepository realiserSearchRepository;
    
    /**
     * POST  /realisers : Create a new realiser.
     *
     * @param realiser the realiser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new realiser, or with status 400 (Bad Request) if the realiser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/realisers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Realiser> createRealiser(@RequestBody Realiser realiser) throws URISyntaxException {
        log.debug("REST request to save Realiser : {}", realiser);
        if (realiser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("realiser", "idexists", "A new realiser cannot already have an ID")).body(null);
        }
        Realiser result = realiserRepository.save(realiser);
        realiserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/realisers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("realiser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /realisers : Updates an existing realiser.
     *
     * @param realiser the realiser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated realiser,
     * or with status 400 (Bad Request) if the realiser is not valid,
     * or with status 500 (Internal Server Error) if the realiser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/realisers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Realiser> updateRealiser(@RequestBody Realiser realiser) throws URISyntaxException {
        log.debug("REST request to update Realiser : {}", realiser);
        if (realiser.getId() == null) {
            return createRealiser(realiser);
        }
        Realiser result = realiserRepository.save(realiser);
        realiserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("realiser", realiser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /realisers : get all the realisers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of realisers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/realisers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Realiser>> getAllRealisers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Realisers");
        Page<Realiser> page = realiserRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/realisers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /realisers/:id : get the "id" realiser.
     *
     * @param id the id of the realiser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the realiser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/realisers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Realiser> getRealiser(@PathVariable Long id) {
        log.debug("REST request to get Realiser : {}", id);
        Realiser realiser = realiserRepository.findOne(id);
        return Optional.ofNullable(realiser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /realisers/:id : delete the "id" realiser.
     *
     * @param id the id of the realiser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/realisers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRealiser(@PathVariable Long id) {
        log.debug("REST request to delete Realiser : {}", id);
        realiserRepository.delete(id);
        realiserSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("realiser", id.toString())).build();
    }

    /**
     * SEARCH  /_search/realisers?query=:query : search for the realiser corresponding
     * to the query.
     *
     * @param query the query of the realiser search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/realisers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Realiser>> searchRealisers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Realisers for query {}", query);
        Page<Realiser> page = realiserSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/realisers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
