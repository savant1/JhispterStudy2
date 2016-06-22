package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Professeur;
import com.thedevbridge.jhipster.repository.ProfesseurRepository;
import com.thedevbridge.jhipster.repository.search.ProfesseurSearchRepository;

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
 * Test class for the ProfesseurResource REST controller.
 *
 * @see ProfesseurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class ProfesseurResourceIntTest {

    private static final String DEFAULT_DEPARTEMENT = "AAAAA";
    private static final String UPDATED_DEPARTEMENT = "BBBBB";
    private static final String DEFAULT_PROFESSION = "AAAAA";
    private static final String UPDATED_PROFESSION = "BBBBB";

    @Inject
    private ProfesseurRepository professeurRepository;

    @Inject
    private ProfesseurSearchRepository professeurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProfesseurMockMvc;

    private Professeur professeur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProfesseurResource professeurResource = new ProfesseurResource();
        ReflectionTestUtils.setField(professeurResource, "professeurSearchRepository", professeurSearchRepository);
        ReflectionTestUtils.setField(professeurResource, "professeurRepository", professeurRepository);
        this.restProfesseurMockMvc = MockMvcBuilders.standaloneSetup(professeurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        professeurSearchRepository.deleteAll();
        professeur = new Professeur();
        professeur.setDepartement(DEFAULT_DEPARTEMENT);
        professeur.setProfession(DEFAULT_PROFESSION);
    }

    @Test
    @Transactional
    public void createProfesseur() throws Exception {
        int databaseSizeBeforeCreate = professeurRepository.findAll().size();

        // Create the Professeur

        restProfesseurMockMvc.perform(post("/api/professeurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(professeur)))
                .andExpect(status().isCreated());

        // Validate the Professeur in the database
        List<Professeur> professeurs = professeurRepository.findAll();
        assertThat(professeurs).hasSize(databaseSizeBeforeCreate + 1);
        Professeur testProfesseur = professeurs.get(professeurs.size() - 1);
        assertThat(testProfesseur.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testProfesseur.getProfession()).isEqualTo(DEFAULT_PROFESSION);

        // Validate the Professeur in ElasticSearch
        Professeur professeurEs = professeurSearchRepository.findOne(testProfesseur.getId());
        assertThat(professeurEs).isEqualToComparingFieldByField(testProfesseur);
    }

    @Test
    @Transactional
    public void getAllProfesseurs() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurs
        restProfesseurMockMvc.perform(get("/api/professeurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(professeur.getId().intValue())))
                .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
                .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION.toString())));
    }

    @Test
    @Transactional
    public void getProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get the professeur
        restProfesseurMockMvc.perform(get("/api/professeurs/{id}", professeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(professeur.getId().intValue()))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfesseur() throws Exception {
        // Get the professeur
        restProfesseurMockMvc.perform(get("/api/professeurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);
        professeurSearchRepository.save(professeur);
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();

        // Update the professeur
        Professeur updatedProfesseur = new Professeur();
        updatedProfesseur.setId(professeur.getId());
        updatedProfesseur.setDepartement(UPDATED_DEPARTEMENT);
        updatedProfesseur.setProfession(UPDATED_PROFESSION);

        restProfesseurMockMvc.perform(put("/api/professeurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProfesseur)))
                .andExpect(status().isOk());

        // Validate the Professeur in the database
        List<Professeur> professeurs = professeurRepository.findAll();
        assertThat(professeurs).hasSize(databaseSizeBeforeUpdate);
        Professeur testProfesseur = professeurs.get(professeurs.size() - 1);
        assertThat(testProfesseur.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testProfesseur.getProfession()).isEqualTo(UPDATED_PROFESSION);

        // Validate the Professeur in ElasticSearch
        Professeur professeurEs = professeurSearchRepository.findOne(testProfesseur.getId());
        assertThat(professeurEs).isEqualToComparingFieldByField(testProfesseur);
    }

    @Test
    @Transactional
    public void deleteProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);
        professeurSearchRepository.save(professeur);
        int databaseSizeBeforeDelete = professeurRepository.findAll().size();

        // Get the professeur
        restProfesseurMockMvc.perform(delete("/api/professeurs/{id}", professeur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean professeurExistsInEs = professeurSearchRepository.exists(professeur.getId());
        assertThat(professeurExistsInEs).isFalse();

        // Validate the database is empty
        List<Professeur> professeurs = professeurRepository.findAll();
        assertThat(professeurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);
        professeurSearchRepository.save(professeur);

        // Search the professeur
        restProfesseurMockMvc.perform(get("/api/_search/professeurs?query=id:" + professeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION.toString())));
    }
}
