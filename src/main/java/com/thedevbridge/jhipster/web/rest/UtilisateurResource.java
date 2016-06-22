package com.thedevbridge.jhipster.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.thedevbridge.jhipster.domain.Utilisateur;
import com.thedevbridge.jhipster.repository.UtilisateurRepository;
import com.thedevbridge.jhipster.repository.search.UtilisateurSearchRepository;
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
 * REST controller for managing Utilisateur.
 */
@RestController
@RequestMapping("/api")
public class UtilisateurResource {

    private final Logger log = LoggerFactory.getLogger(UtilisateurResource.class);
        
    @Inject
    private UtilisateurRepository utilisateurRepository;
    
    @Inject
    private UtilisateurSearchRepository utilisateurSearchRepository;
    
    /**
     * POST  /utilisateurs : Create a new utilisateur.
     *
     * @param utilisateur the utilisateur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new utilisateur, or with status 400 (Bad Request) if the utilisateur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/utilisateurs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) throws URISyntaxException {
        log.debug("REST request to save Utilisateur : {}", utilisateur);
        if (utilisateur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("utilisateur", "idexists", "A new utilisateur cannot already have an ID")).body(null);
        }
        Utilisateur result = utilisateurRepository.save(utilisateur);
        utilisateurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/utilisateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("utilisateur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /utilisateurs : Updates an existing utilisateur.
     *
     * @param utilisateur the utilisateur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated utilisateur,
     * or with status 400 (Bad Request) if the utilisateur is not valid,
     * or with status 500 (Internal Server Error) if the utilisateur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/utilisateurs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Utilisateur> updateUtilisateur(@RequestBody Utilisateur utilisateur) throws URISyntaxException {
        log.debug("REST request to update Utilisateur : {}", utilisateur);
        if (utilisateur.getId() == null) {
            return createUtilisateur(utilisateur);
        }
        Utilisateur result = utilisateurRepository.save(utilisateur);
        utilisateurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("utilisateur", utilisateur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /utilisateurs : get all the utilisateurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of utilisateurs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/utilisateurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Utilisateurs");
        Page<Utilisateur> page = utilisateurRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/utilisateurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /utilisateurs/:id : get the "id" utilisateur.
     *
     * @param id the id of the utilisateur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the utilisateur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/utilisateurs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Long id) {
        log.debug("REST request to get Utilisateur : {}", id);
        Utilisateur utilisateur = utilisateurRepository.findOne(id);
        return Optional.ofNullable(utilisateur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /utilisateurs/:id : delete the "id" utilisateur.
     *
     * @param id the id of the utilisateur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/utilisateurs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        log.debug("REST request to delete Utilisateur : {}", id);
        utilisateurRepository.delete(id);
        utilisateurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("utilisateur", id.toString())).build();
    }

    /**
     * SEARCH  /_search/utilisateurs?query=:query : search for the utilisateur corresponding
     * to the query.
     *
     * @param query the query of the utilisateur search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/utilisateurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Utilisateur>> searchUtilisateurs(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Utilisateurs for query {}", query);
        Page<Utilisateur> page = utilisateurSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/utilisateurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
