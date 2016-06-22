package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Recommander;
import com.thedevbridge.jhipster.repository.RecommanderRepository;
import com.thedevbridge.jhipster.repository.search.RecommanderSearchRepository;

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
 * Test class for the RecommanderResource REST controller.
 *
 * @see RecommanderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class RecommanderResourceIntTest {

    private static final String DEFAULT_RAISON = "AAAAA";
    private static final String UPDATED_RAISON = "BBBBB";

    @Inject
    private RecommanderRepository recommanderRepository;

    @Inject
    private RecommanderSearchRepository recommanderSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRecommanderMockMvc;

    private Recommander recommander;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RecommanderResource recommanderResource = new RecommanderResource();
        ReflectionTestUtils.setField(recommanderResource, "recommanderSearchRepository", recommanderSearchRepository);
        ReflectionTestUtils.setField(recommanderResource, "recommanderRepository", recommanderRepository);
        this.restRecommanderMockMvc = MockMvcBuilders.standaloneSetup(recommanderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        recommanderSearchRepository.deleteAll();
        recommander = new Recommander();
        recommander.setRaison(DEFAULT_RAISON);
    }

    @Test
    @Transactional
    public void createRecommander() throws Exception {
        int databaseSizeBeforeCreate = recommanderRepository.findAll().size();

        // Create the Recommander

        restRecommanderMockMvc.perform(post("/api/recommanders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recommander)))
                .andExpect(status().isCreated());

        // Validate the Recommander in the database
        List<Recommander> recommanders = recommanderRepository.findAll();
        assertThat(recommanders).hasSize(databaseSizeBeforeCreate + 1);
        Recommander testRecommander = recommanders.get(recommanders.size() - 1);
        assertThat(testRecommander.getRaison()).isEqualTo(DEFAULT_RAISON);

        // Validate the Recommander in ElasticSearch
        Recommander recommanderEs = recommanderSearchRepository.findOne(testRecommander.getId());
        assertThat(recommanderEs).isEqualToComparingFieldByField(testRecommander);
    }

    @Test
    @Transactional
    public void getAllRecommanders() throws Exception {
        // Initialize the database
        recommanderRepository.saveAndFlush(recommander);

        // Get all the recommanders
        restRecommanderMockMvc.perform(get("/api/recommanders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recommander.getId().intValue())))
                .andExpect(jsonPath("$.[*].raison").value(hasItem(DEFAULT_RAISON.toString())));
    }

    @Test
    @Transactional
    public void getRecommander() throws Exception {
        // Initialize the database
        recommanderRepository.saveAndFlush(recommander);

        // Get the recommander
        restRecommanderMockMvc.perform(get("/api/recommanders/{id}", recommander.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(recommander.getId().intValue()))
            .andExpect(jsonPath("$.raison").value(DEFAULT_RAISON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRecommander() throws Exception {
        // Get the recommander
        restRecommanderMockMvc.perform(get("/api/recommanders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecommander() throws Exception {
        // Initialize the database
        recommanderRepository.saveAndFlush(recommander);
        recommanderSearchRepository.save(recommander);
        int databaseSizeBeforeUpdate = recommanderRepository.findAll().size();

        // Update the recommander
        Recommander updatedRecommander = new Recommander();
        updatedRecommander.setId(recommander.getId());
        updatedRecommander.setRaison(UPDATED_RAISON);

        restRecommanderMockMvc.perform(put("/api/recommanders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRecommander)))
                .andExpect(status().isOk());

        // Validate the Recommander in the database
        List<Recommander> recommanders = recommanderRepository.findAll();
        assertThat(recommanders).hasSize(databaseSizeBeforeUpdate);
        Recommander testRecommander = recommanders.get(recommanders.size() - 1);
        assertThat(testRecommander.getRaison()).isEqualTo(UPDATED_RAISON);

        // Validate the Recommander in ElasticSearch
        Recommander recommanderEs = recommanderSearchRepository.findOne(testRecommander.getId());
        assertThat(recommanderEs).isEqualToComparingFieldByField(testRecommander);
    }

    @Test
    @Transactional
    public void deleteRecommander() throws Exception {
        // Initialize the database
        recommanderRepository.saveAndFlush(recommander);
        recommanderSearchRepository.save(recommander);
        int databaseSizeBeforeDelete = recommanderRepository.findAll().size();

        // Get the recommander
        restRecommanderMockMvc.perform(delete("/api/recommanders/{id}", recommander.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean recommanderExistsInEs = recommanderSearchRepository.exists(recommander.getId());
        assertThat(recommanderExistsInEs).isFalse();

        // Validate the database is empty
        List<Recommander> recommanders = recommanderRepository.findAll();
        assertThat(recommanders).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchRecommander() throws Exception {
        // Initialize the database
        recommanderRepository.saveAndFlush(recommander);
        recommanderSearchRepository.save(recommander);

        // Search the recommander
        restRecommanderMockMvc.perform(get("/api/_search/recommanders?query=id:" + recommander.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recommander.getId().intValue())))
            .andExpect(jsonPath("$.[*].raison").value(hasItem(DEFAULT_RAISON.toString())));
    }
}
