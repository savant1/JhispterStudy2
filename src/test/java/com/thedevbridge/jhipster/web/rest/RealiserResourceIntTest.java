package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Realiser;
import com.thedevbridge.jhipster.repository.RealiserRepository;
import com.thedevbridge.jhipster.repository.search.RealiserSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the RealiserResource REST controller.
 *
 * @see RealiserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class RealiserResourceIntTest {


    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private RealiserRepository realiserRepository;

    @Inject
    private RealiserSearchRepository realiserSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRealiserMockMvc;

    private Realiser realiser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RealiserResource realiserResource = new RealiserResource();
        ReflectionTestUtils.setField(realiserResource, "realiserSearchRepository", realiserSearchRepository);
        ReflectionTestUtils.setField(realiserResource, "realiserRepository", realiserRepository);
        this.restRealiserMockMvc = MockMvcBuilders.standaloneSetup(realiserResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        realiserSearchRepository.deleteAll();
        realiser = new Realiser();
        realiser.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createRealiser() throws Exception {
        int databaseSizeBeforeCreate = realiserRepository.findAll().size();

        // Create the Realiser

        restRealiserMockMvc.perform(post("/api/realisers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(realiser)))
                .andExpect(status().isCreated());

        // Validate the Realiser in the database
        List<Realiser> realisers = realiserRepository.findAll();
        assertThat(realisers).hasSize(databaseSizeBeforeCreate + 1);
        Realiser testRealiser = realisers.get(realisers.size() - 1);
        assertThat(testRealiser.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the Realiser in ElasticSearch
        Realiser realiserEs = realiserSearchRepository.findOne(testRealiser.getId());
        assertThat(realiserEs).isEqualToComparingFieldByField(testRealiser);
    }

    @Test
    @Transactional
    public void getAllRealisers() throws Exception {
        // Initialize the database
        realiserRepository.saveAndFlush(realiser);

        // Get all the realisers
        restRealiserMockMvc.perform(get("/api/realisers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(realiser.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    public void getRealiser() throws Exception {
        // Initialize the database
        realiserRepository.saveAndFlush(realiser);

        // Get the realiser
        restRealiserMockMvc.perform(get("/api/realisers/{id}", realiser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(realiser.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRealiser() throws Exception {
        // Get the realiser
        restRealiserMockMvc.perform(get("/api/realisers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRealiser() throws Exception {
        // Initialize the database
        realiserRepository.saveAndFlush(realiser);
        realiserSearchRepository.save(realiser);
        int databaseSizeBeforeUpdate = realiserRepository.findAll().size();

        // Update the realiser
        Realiser updatedRealiser = new Realiser();
        updatedRealiser.setId(realiser.getId());
        updatedRealiser.setDate(UPDATED_DATE);

        restRealiserMockMvc.perform(put("/api/realisers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRealiser)))
                .andExpect(status().isOk());

        // Validate the Realiser in the database
        List<Realiser> realisers = realiserRepository.findAll();
        assertThat(realisers).hasSize(databaseSizeBeforeUpdate);
        Realiser testRealiser = realisers.get(realisers.size() - 1);
        assertThat(testRealiser.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the Realiser in ElasticSearch
        Realiser realiserEs = realiserSearchRepository.findOne(testRealiser.getId());
        assertThat(realiserEs).isEqualToComparingFieldByField(testRealiser);
    }

    @Test
    @Transactional
    public void deleteRealiser() throws Exception {
        // Initialize the database
        realiserRepository.saveAndFlush(realiser);
        realiserSearchRepository.save(realiser);
        int databaseSizeBeforeDelete = realiserRepository.findAll().size();

        // Get the realiser
        restRealiserMockMvc.perform(delete("/api/realisers/{id}", realiser.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean realiserExistsInEs = realiserSearchRepository.exists(realiser.getId());
        assertThat(realiserExistsInEs).isFalse();

        // Validate the database is empty
        List<Realiser> realisers = realiserRepository.findAll();
        assertThat(realisers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRealiser() throws Exception {
        // Initialize the database
        realiserRepository.saveAndFlush(realiser);
        realiserSearchRepository.save(realiser);

        // Search the realiser
        restRealiserMockMvc.perform(get("/api/_search/realisers?query=id:" + realiser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(realiser.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
}
