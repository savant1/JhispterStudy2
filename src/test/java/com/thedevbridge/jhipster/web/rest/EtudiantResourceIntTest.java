package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Etudiant;
import com.thedevbridge.jhipster.repository.EtudiantRepository;
import com.thedevbridge.jhipster.repository.search.EtudiantSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EtudiantResource REST controller.
 *
 * @see EtudiantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class EtudiantResourceIntTest {

    private static final String DEFAULT_FILIERE = "AAAAA";
    private static final String UPDATED_FILIERE = "BBBBB";

    @Inject
    private EtudiantRepository etudiantRepository;

    @Inject
    private EtudiantSearchRepository etudiantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEtudiantMockMvc;

    private Etudiant etudiant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EtudiantResource etudiantResource = new EtudiantResource();
        ReflectionTestUtils.setField(etudiantResource, "etudiantSearchRepository", etudiantSearchRepository);
        ReflectionTestUtils.setField(etudiantResource, "etudiantRepository", etudiantRepository);
        this.restEtudiantMockMvc = MockMvcBuilders.standaloneSetup(etudiantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        etudiantSearchRepository.deleteAll();
        etudiant = new Etudiant();
        etudiant.setFiliere(DEFAULT_FILIERE);
    }

    @Test
    @Transactional
    public void createEtudiant() throws Exception {
        int databaseSizeBeforeCreate = etudiantRepository.findAll().size();

        // Create the Etudiant

        restEtudiantMockMvc.perform(post("/api/etudiants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(etudiant)))
                .andExpect(status().isCreated());

        // Validate the Etudiant in the database
        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeCreate + 1);
        Etudiant testEtudiant = etudiants.get(etudiants.size() - 1);
        assertThat(testEtudiant.getFiliere()).isEqualTo(DEFAULT_FILIERE);

        // Validate the Etudiant in ElasticSearch
        Etudiant etudiantEs = etudiantSearchRepository.findOne(testEtudiant.getId());
        assertThat(etudiantEs).isEqualToComparingFieldByField(testEtudiant);
    }

    @Test
    @Transactional
    public void getAllEtudiants() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiants
        restEtudiantMockMvc.perform(get("/api/etudiants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
                .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE.toString())));
    }

    @Test
    @Transactional
    public void getEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get the etudiant
        restEtudiantMockMvc.perform(get("/api/etudiants/{id}", etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(etudiant.getId().intValue()))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEtudiant() throws Exception {
        // Get the etudiant
        restEtudiantMockMvc.perform(get("/api/etudiants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        etudiantSearchRepository.save(etudiant);
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Update the etudiant
        Etudiant updatedEtudiant = new Etudiant();
        updatedEtudiant.setId(etudiant.getId());
        updatedEtudiant.setFiliere(UPDATED_FILIERE);

        restEtudiantMockMvc.perform(put("/api/etudiants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEtudiant)))
                .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiants.get(etudiants.size() - 1);
        assertThat(testEtudiant.getFiliere()).isEqualTo(UPDATED_FILIERE);

        // Validate the Etudiant in ElasticSearch
        Etudiant etudiantEs = etudiantSearchRepository.findOne(testEtudiant.getId());
        assertThat(etudiantEs).isEqualToComparingFieldByField(testEtudiant);
    }

    @Test
    @Transactional
    public void deleteEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        etudiantSearchRepository.save(etudiant);
        int databaseSizeBeforeDelete = etudiantRepository.findAll().size();

        // Get the etudiant
        restEtudiantMockMvc.perform(delete("/api/etudiants/{id}", etudiant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean etudiantExistsInEs = etudiantSearchRepository.exists(etudiant.getId());
        assertThat(etudiantExistsInEs).isFalse();

        // Validate the database is empty
        List<Etudiant> etudiants = etudiantRepository.findAll();
        assertThat(etudiants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);
        etudiantSearchRepository.save(etudiant);

        // Search the etudiant
        restEtudiantMockMvc.perform(get("/api/_search/etudiants?query=id:" + etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE.toString())));
    }
}
