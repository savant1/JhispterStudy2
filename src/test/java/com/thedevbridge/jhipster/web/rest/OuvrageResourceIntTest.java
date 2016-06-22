package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Ouvrage;
import com.thedevbridge.jhipster.repository.OuvrageRepository;
import com.thedevbridge.jhipster.repository.search.OuvrageSearchRepository;

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

import com.thedevbridge.jhipster.domain.enumeration.Typeouvrage;

/**
 * Test class for the OuvrageResource REST controller.
 *
 * @see OuvrageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class OuvrageResourceIntTest {

    private static final String DEFAULT_FILIERE = "AAAAA";
    private static final String UPDATED_FILIERE = "BBBBB";
    private static final String DEFAULT_CATEGORIE = "AAAAA";
    private static final String UPDATED_CATEGORIE = "BBBBB";
    private static final String DEFAULT_DISCIPLINE = "AAAAA";
    private static final String UPDATED_DISCIPLINE = "BBBBB";
    private static final String DEFAULT_TITRE = "AAAAA";
    private static final String UPDATED_TITRE = "BBBBB";

    private static final Typeouvrage DEFAULT_GENRE = Typeouvrage.CD;
    private static final Typeouvrage UPDATED_GENRE = Typeouvrage.Livre;

    @Inject
    private OuvrageRepository ouvrageRepository;

    @Inject
    private OuvrageSearchRepository ouvrageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOuvrageMockMvc;

    private Ouvrage ouvrage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OuvrageResource ouvrageResource = new OuvrageResource();
        ReflectionTestUtils.setField(ouvrageResource, "ouvrageSearchRepository", ouvrageSearchRepository);
        ReflectionTestUtils.setField(ouvrageResource, "ouvrageRepository", ouvrageRepository);
        this.restOuvrageMockMvc = MockMvcBuilders.standaloneSetup(ouvrageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ouvrageSearchRepository.deleteAll();
        ouvrage = new Ouvrage();
        ouvrage.setFiliere(DEFAULT_FILIERE);
        ouvrage.setCategorie(DEFAULT_CATEGORIE);
        ouvrage.setDiscipline(DEFAULT_DISCIPLINE);
        ouvrage.setTitre(DEFAULT_TITRE);
        ouvrage.setGenre(DEFAULT_GENRE);
    }

    @Test
    @Transactional
    public void createOuvrage() throws Exception {
        int databaseSizeBeforeCreate = ouvrageRepository.findAll().size();

        // Create the Ouvrage

        restOuvrageMockMvc.perform(post("/api/ouvrages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ouvrage)))
                .andExpect(status().isCreated());

        // Validate the Ouvrage in the database
        List<Ouvrage> ouvrages = ouvrageRepository.findAll();
        assertThat(ouvrages).hasSize(databaseSizeBeforeCreate + 1);
        Ouvrage testOuvrage = ouvrages.get(ouvrages.size() - 1);
        assertThat(testOuvrage.getFiliere()).isEqualTo(DEFAULT_FILIERE);
        assertThat(testOuvrage.getCategorie()).isEqualTo(DEFAULT_CATEGORIE);
        assertThat(testOuvrage.getDiscipline()).isEqualTo(DEFAULT_DISCIPLINE);
        assertThat(testOuvrage.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testOuvrage.getGenre()).isEqualTo(DEFAULT_GENRE);

        // Validate the Ouvrage in ElasticSearch
        Ouvrage ouvrageEs = ouvrageSearchRepository.findOne(testOuvrage.getId());
        assertThat(ouvrageEs).isEqualToComparingFieldByField(testOuvrage);
    }

    @Test
    @Transactional
    public void getAllOuvrages() throws Exception {
        // Initialize the database
        ouvrageRepository.saveAndFlush(ouvrage);

        // Get all the ouvrages
        restOuvrageMockMvc.perform(get("/api/ouvrages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ouvrage.getId().intValue())))
                .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE.toString())))
                .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
                .andExpect(jsonPath("$.[*].discipline").value(hasItem(DEFAULT_DISCIPLINE.toString())))
                .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
                .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())));
    }

    @Test
    @Transactional
    public void getOuvrage() throws Exception {
        // Initialize the database
        ouvrageRepository.saveAndFlush(ouvrage);

        // Get the ouvrage
        restOuvrageMockMvc.perform(get("/api/ouvrages/{id}", ouvrage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ouvrage.getId().intValue()))
            .andExpect(jsonPath("$.filiere").value(DEFAULT_FILIERE.toString()))
            .andExpect(jsonPath("$.categorie").value(DEFAULT_CATEGORIE.toString()))
            .andExpect(jsonPath("$.discipline").value(DEFAULT_DISCIPLINE.toString()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOuvrage() throws Exception {
        // Get the ouvrage
        restOuvrageMockMvc.perform(get("/api/ouvrages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOuvrage() throws Exception {
        // Initialize the database
        ouvrageRepository.saveAndFlush(ouvrage);
        ouvrageSearchRepository.save(ouvrage);
        int databaseSizeBeforeUpdate = ouvrageRepository.findAll().size();

        // Update the ouvrage
        Ouvrage updatedOuvrage = new Ouvrage();
        updatedOuvrage.setId(ouvrage.getId());
        updatedOuvrage.setFiliere(UPDATED_FILIERE);
        updatedOuvrage.setCategorie(UPDATED_CATEGORIE);
        updatedOuvrage.setDiscipline(UPDATED_DISCIPLINE);
        updatedOuvrage.setTitre(UPDATED_TITRE);
        updatedOuvrage.setGenre(UPDATED_GENRE);

        restOuvrageMockMvc.perform(put("/api/ouvrages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOuvrage)))
                .andExpect(status().isOk());

        // Validate the Ouvrage in the database
        List<Ouvrage> ouvrages = ouvrageRepository.findAll();
        assertThat(ouvrages).hasSize(databaseSizeBeforeUpdate);
        Ouvrage testOuvrage = ouvrages.get(ouvrages.size() - 1);
        assertThat(testOuvrage.getFiliere()).isEqualTo(UPDATED_FILIERE);
        assertThat(testOuvrage.getCategorie()).isEqualTo(UPDATED_CATEGORIE);
        assertThat(testOuvrage.getDiscipline()).isEqualTo(UPDATED_DISCIPLINE);
        assertThat(testOuvrage.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testOuvrage.getGenre()).isEqualTo(UPDATED_GENRE);

        // Validate the Ouvrage in ElasticSearch
        Ouvrage ouvrageEs = ouvrageSearchRepository.findOne(testOuvrage.getId());
        assertThat(ouvrageEs).isEqualToComparingFieldByField(testOuvrage);
    }

    @Test
    @Transactional
    public void deleteOuvrage() throws Exception {
        // Initialize the database
        ouvrageRepository.saveAndFlush(ouvrage);
        ouvrageSearchRepository.save(ouvrage);
        int databaseSizeBeforeDelete = ouvrageRepository.findAll().size();

        // Get the ouvrage
        restOuvrageMockMvc.perform(delete("/api/ouvrages/{id}", ouvrage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean ouvrageExistsInEs = ouvrageSearchRepository.exists(ouvrage.getId());
        assertThat(ouvrageExistsInEs).isFalse();

        // Validate the database is empty
        List<Ouvrage> ouvrages = ouvrageRepository.findAll();
        assertThat(ouvrages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOuvrage() throws Exception {
        // Initialize the database
        ouvrageRepository.saveAndFlush(ouvrage);
        ouvrageSearchRepository.save(ouvrage);

        // Search the ouvrage
        restOuvrageMockMvc.perform(get("/api/_search/ouvrages?query=id:" + ouvrage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ouvrage.getId().intValue())))
            .andExpect(jsonPath("$.[*].filiere").value(hasItem(DEFAULT_FILIERE.toString())))
            .andExpect(jsonPath("$.[*].categorie").value(hasItem(DEFAULT_CATEGORIE.toString())))
            .andExpect(jsonPath("$.[*].discipline").value(hasItem(DEFAULT_DISCIPLINE.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE.toString())));
    }
}
