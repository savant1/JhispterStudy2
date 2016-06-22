package com.thedevbridge.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thedevbridge.jhipster.domain.Emprunter;
import com.thedevbridge.jhipster.repository.EmprunterRepository;
import com.thedevbridge.jhipster.repository.search.EmprunterSearchRepository;
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
 * REST controller for managing Emprunter.
 */
@RestController
@RequestMapping("/api")
public class EmprunterResource {

    private final Logger log = LoggerFactory.getLogger(EmprunterResource.class);
        
    @Inject
    private EmprunterRepository emprunterRepository;
    
    @Inject
    private EmprunterSearchRepository emprunterSearchRepository;
    
    /**
     * POST  /emprunters : Create a new emprunter.
     *
     * @param emprunter the emprunter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new emprunter, or with status 400 (Bad Request) if the emprunter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/emprunters",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Emprunter> createEmprunter(@RequestBody Emprunter emprunter) throws URISyntaxException {
        log.debug("REST request to save Emprunter : {}", emprunter);
        if (emprunter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("emprunter", "idexists", "A new emprunter cannot already have an ID")).body(null);
        }
        Emprunter result = emprunterRepository.save(emprunter);
        emprunterSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/emprunters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("emprunter", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /emprunters : Updates an existing emprunter.
     *
     * @param emprunter the emprunter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated emprunter,
     * or with status 400 (Bad Request) if the emprunter is not valid,
     * or with status 500 (Internal Server Error) if the emprunter couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/emprunters",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Emprunter> updateEmprunter(@RequestBody Emprunter emprunter) throws URISyntaxException {
        log.debug("REST request to update Emprunter : {}", emprunter);
        if (emprunter.getId() == null) {
            return createEmprunter(emprunter);
        }
        Emprunter result = emprunterRepository.save(emprunter);
        emprunterSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("emprunter", emprunter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /emprunters : get all the emprunters.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of emprunters in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/emprunters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Emprunter>> getAllEmprunters(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Emprunters");
        Page<Emprunter> page = emprunterRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/emprunters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /emprunters/:id : get the "id" emprunter.
     *
     * @param id the id of the emprunter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the emprunter, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/emprunters/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Emprunter> getEmprunter(@PathVariable Long id) {
        log.debug("REST request to get Emprunter : {}", id);
        Emprunter emprunter = emprunterRepository.findOne(id);
        return Optional.ofNullable(emprunter)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /emprunters/:id : delete the "id" emprunter.
     *
     * @param id the id of the emprunter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/emprunters/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmprunter(@PathVariable Long id) {
        log.debug("REST request to delete Emprunter : {}", id);
        emprunterRepository.delete(id);
        emprunterSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("emprunter", id.toString())).build();
    }

    /**
     * SEARCH  /_search/emprunters?query=:query : search for the emprunter corresponding
     * to the query.
     *
     * @param query the query of the emprunter search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/emprunters",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Emprunter>> searchEmprunters(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Emprunters for query {}", query);
        Page<Emprunter> page = emprunterSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/emprunters");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
