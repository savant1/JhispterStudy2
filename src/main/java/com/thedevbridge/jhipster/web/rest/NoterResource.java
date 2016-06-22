package com.thedevbridge.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thedevbridge.jhipster.domain.Noter;
import com.thedevbridge.jhipster.repository.NoterRepository;
import com.thedevbridge.jhipster.repository.search.NoterSearchRepository;
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
 * REST controller for managing Noter.
 */
@RestController
@RequestMapping("/api")
public class NoterResource {

    private final Logger log = LoggerFactory.getLogger(NoterResource.class);
        
    @Inject
    private NoterRepository noterRepository;
    
    @Inject
    private NoterSearchRepository noterSearchRepository;
    
    /**
     * POST  /noters : Create a new noter.
     *
     * @param noter the noter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new noter, or with status 400 (Bad Request) if the noter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/noters",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Noter> createNoter(@RequestBody Noter noter) throws URISyntaxException {
        log.debug("REST request to save Noter : {}", noter);
        if (noter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("noter", "idexists", "A new noter cannot already have an ID")).body(null);
        }
        Noter result = noterRepository.save(noter);
        noterSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/noters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("noter", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /noters : Updates an existing noter.
     *
     * @param noter the noter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated noter,
     * or with status 400 (Bad Request) if the noter is not valid,
     * or with status 500 (Internal Server Error) if the noter couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/noters",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Noter> updateNoter(@RequestBody Noter noter) throws URISyntaxException {
        log.debug("REST request to update Noter : {}", noter);
        if (noter.getId() == null) {
            return createNoter(noter);
        }
        Noter result = noterRepository.save(noter);
        noterSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("noter", noter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /noters : get all the noters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of noters in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/noters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Noter>> getAllNoters(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Noters");
        Page<Noter> page = noterRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/noters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /noters/:id : get the "id" noter.
     *
     * @param id the id of the noter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the noter, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/noters/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Noter> getNoter(@PathVariable Long id) {
        log.debug("REST request to get Noter : {}", id);
        Noter noter = noterRepository.findOne(id);
        return Optional.ofNullable(noter)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /noters/:id : delete the "id" noter.
     *
     * @param id the id of the noter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/noters/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNoter(@PathVariable Long id) {
        log.debug("REST request to delete Noter : {}", id);
        noterRepository.delete(id);
        noterSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("noter", id.toString())).build();
    }

    /**
     * SEARCH  /_search/noters?query=:query : search for the noter corresponding
     * to the query.
     *
     * @param query the query of the noter search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/noters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Noter>> searchNoters(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Noters for query {}", query);
        Page<Noter> page = noterSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/noters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
