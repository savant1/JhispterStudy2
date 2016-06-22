package com.thedevbridge.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thedevbridge.jhipster.domain.Ouvrage;
import com.thedevbridge.jhipster.repository.OuvrageRepository;
import com.thedevbridge.jhipster.repository.search.OuvrageSearchRepository;
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
 * REST controller for managing Ouvrage.
 */
@RestController
@RequestMapping("/api")
public class OuvrageResource {

    private final Logger log = LoggerFactory.getLogger(OuvrageResource.class);
        
    @Inject
    private OuvrageRepository ouvrageRepository;
    
    @Inject
    private OuvrageSearchRepository ouvrageSearchRepository;
    
    /**
     * POST  /ouvrages : Create a new ouvrage.
     *
     * @param ouvrage the ouvrage to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ouvrage, or with status 400 (Bad Request) if the ouvrage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ouvrages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ouvrage> createOuvrage(@RequestBody Ouvrage ouvrage) throws URISyntaxException {
        log.debug("REST request to save Ouvrage : {}", ouvrage);
        if (ouvrage.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ouvrage", "idexists", "A new ouvrage cannot already have an ID")).body(null);
        }
        Ouvrage result = ouvrageRepository.save(ouvrage);
        ouvrageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/ouvrages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ouvrage", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ouvrages : Updates an existing ouvrage.
     *
     * @param ouvrage the ouvrage to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ouvrage,
     * or with status 400 (Bad Request) if the ouvrage is not valid,
     * or with status 500 (Internal Server Error) if the ouvrage couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/ouvrages",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ouvrage> updateOuvrage(@RequestBody Ouvrage ouvrage) throws URISyntaxException {
        log.debug("REST request to update Ouvrage : {}", ouvrage);
        if (ouvrage.getId() == null) {
            return createOuvrage(ouvrage);
        }
        Ouvrage result = ouvrageRepository.save(ouvrage);
        ouvrageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ouvrage", ouvrage.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ouvrages : get all the ouvrages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of ouvrages in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/ouvrages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ouvrage>> getAllOuvrages(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Ouvrages");
        Page<Ouvrage> page = ouvrageRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ouvrages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ouvrages/:id : get the "id" ouvrage.
     *
     * @param id the id of the ouvrage to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ouvrage, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/ouvrages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ouvrage> getOuvrage(@PathVariable Long id) {
        log.debug("REST request to get Ouvrage : {}", id);
        Ouvrage ouvrage = ouvrageRepository.findOne(id);
        return Optional.ofNullable(ouvrage)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ouvrages/:id : delete the "id" ouvrage.
     *
     * @param id the id of the ouvrage to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/ouvrages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOuvrage(@PathVariable Long id) {
        log.debug("REST request to delete Ouvrage : {}", id);
        ouvrageRepository.delete(id);
        ouvrageSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ouvrage", id.toString())).build();
    }

    /**
     * SEARCH  /_search/ouvrages?query=:query : search for the ouvrage corresponding
     * to the query.
     *
     * @param query the query of the ouvrage search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/ouvrages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ouvrage>> searchOuvrages(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Ouvrages for query {}", query);
        Page<Ouvrage> page = ouvrageSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/ouvrages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
