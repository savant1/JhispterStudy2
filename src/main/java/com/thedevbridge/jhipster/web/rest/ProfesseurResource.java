package com.thedevbridge.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thedevbridge.jhipster.domain.Professeur;
import com.thedevbridge.jhipster.repository.ProfesseurRepository;
import com.thedevbridge.jhipster.repository.search.ProfesseurSearchRepository;
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
 * REST controller for managing Professeur.
 */
@RestController
@RequestMapping("/api")
public class ProfesseurResource {

    private final Logger log = LoggerFactory.getLogger(ProfesseurResource.class);
        
    @Inject
    private ProfesseurRepository professeurRepository;
    
    @Inject
    private ProfesseurSearchRepository professeurSearchRepository;
    
    /**
     * POST  /professeurs : Create a new professeur.
     *
     * @param professeur the professeur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new professeur, or with status 400 (Bad Request) if the professeur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/professeurs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Professeur> createProfesseur(@RequestBody Professeur professeur) throws URISyntaxException {
        log.debug("REST request to save Professeur : {}", professeur);
        if (professeur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("professeur", "idexists", "A new professeur cannot already have an ID")).body(null);
        }
        Professeur result = professeurRepository.save(professeur);
        professeurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/professeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("professeur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /professeurs : Updates an existing professeur.
     *
     * @param professeur the professeur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated professeur,
     * or with status 400 (Bad Request) if the professeur is not valid,
     * or with status 500 (Internal Server Error) if the professeur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/professeurs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Professeur> updateProfesseur(@RequestBody Professeur professeur) throws URISyntaxException {
        log.debug("REST request to update Professeur : {}", professeur);
        if (professeur.getId() == null) {
            return createProfesseur(professeur);
        }
        Professeur result = professeurRepository.save(professeur);
        professeurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("professeur", professeur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /professeurs : get all the professeurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of professeurs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/professeurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Professeur>> getAllProfesseurs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Professeurs");
        Page<Professeur> page = professeurRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/professeurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /professeurs/:id : get the "id" professeur.
     *
     * @param id the id of the professeur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the professeur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/professeurs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Professeur> getProfesseur(@PathVariable Long id) {
        log.debug("REST request to get Professeur : {}", id);
        Professeur professeur = professeurRepository.findOne(id);
        return Optional.ofNullable(professeur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /professeurs/:id : delete the "id" professeur.
     *
     * @param id the id of the professeur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/professeurs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProfesseur(@PathVariable Long id) {
        log.debug("REST request to delete Professeur : {}", id);
        professeurRepository.delete(id);
        professeurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("professeur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/professeurs?query=:query : search for the professeur corresponding
     * to the query.
     *
     * @param query the query of the professeur search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/professeurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Professeur>> searchProfesseurs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Professeurs for query {}", query);
        Page<Professeur> page = professeurSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/professeurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
