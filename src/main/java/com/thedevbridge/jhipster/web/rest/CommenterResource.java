package com.thedevbridge.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thedevbridge.jhipster.domain.Commenter;
import com.thedevbridge.jhipster.repository.CommenterRepository;
import com.thedevbridge.jhipster.repository.search.CommenterSearchRepository;
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
 * REST controller for managing Commenter.
 */
@RestController
@RequestMapping("/api")
public class CommenterResource {

    private final Logger log = LoggerFactory.getLogger(CommenterResource.class);
        
    @Inject
    private CommenterRepository commenterRepository;
    
    @Inject
    private CommenterSearchRepository commenterSearchRepository;
    
    /**
     * POST  /commenters : Create a new commenter.
     *
     * @param commenter the commenter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new commenter, or with status 400 (Bad Request) if the commenter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commenters",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commenter> createCommenter(@RequestBody Commenter commenter) throws URISyntaxException {
        log.debug("REST request to save Commenter : {}", commenter);
        if (commenter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("commenter", "idexists", "A new commenter cannot already have an ID")).body(null);
        }
        Commenter result = commenterRepository.save(commenter);
        commenterSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/commenters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("commenter", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commenters : Updates an existing commenter.
     *
     * @param commenter the commenter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated commenter,
     * or with status 400 (Bad Request) if the commenter is not valid,
     * or with status 500 (Internal Server Error) if the commenter couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/commenters",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commenter> updateCommenter(@RequestBody Commenter commenter) throws URISyntaxException {
        log.debug("REST request to update Commenter : {}", commenter);
        if (commenter.getId() == null) {
            return createCommenter(commenter);
        }
        Commenter result = commenterRepository.save(commenter);
        commenterSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("commenter", commenter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commenters : get all the commenters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of commenters in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/commenters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Commenter>> getAllCommenters(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Commenters");
        Page<Commenter> page = commenterRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commenters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /commenters/:id : get the "id" commenter.
     *
     * @param id the id of the commenter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the commenter, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/commenters/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Commenter> getCommenter(@PathVariable Long id) {
        log.debug("REST request to get Commenter : {}", id);
        Commenter commenter = commenterRepository.findOne(id);
        return Optional.ofNullable(commenter)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commenters/:id : delete the "id" commenter.
     *
     * @param id the id of the commenter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/commenters/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommenter(@PathVariable Long id) {
        log.debug("REST request to delete Commenter : {}", id);
        commenterRepository.delete(id);
        commenterSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("commenter", id.toString())).build();
    }

    /**
     * SEARCH  /_search/commenters?query=:query : search for the commenter corresponding
     * to the query.
     *
     * @param query the query of the commenter search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/commenters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Commenter>> searchCommenters(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Commenters for query {}", query);
        Page<Commenter> page = commenterSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/commenters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
