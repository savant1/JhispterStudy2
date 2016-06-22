package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Noter;
import com.thedevbridge.jhipster.repository.NoterRepository;
import com.thedevbridge.jhipster.repository.search.NoterSearchRepository;

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
 * Test class for the NoterResource REST controller.
 *
 * @see NoterResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class NoterResourceIntTest {


    private static final Double DEFAULT_NOTE = 1D;
    private static final Double UPDATED_NOTE = 2D;

    @Inject
    private NoterRepository noterRepository;

    @Inject
    private NoterSearchRepository noterSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNoterMockMvc;

    private Noter noter;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NoterResource noterResource = new NoterResource();
        ReflectionTestUtils.setField(noterResource, "noterSearchRepository", noterSearchRepository);
        ReflectionTestUtils.setField(noterResource, "noterRepository", noterRepository);
        this.restNoterMockMvc = MockMvcBuilders.standaloneSetup(noterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        noterSearchRepository.deleteAll();
        noter = new Noter();
        noter.setNote(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createNoter() throws Exception {
        int databaseSizeBeforeCreate = noterRepository.findAll().size();

        // Create the Noter

        restNoterMockMvc.perform(post("/api/noters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noter)))
                .andExpect(status().isCreated());

        // Validate the Noter in the database
        List<Noter> noters = noterRepository.findAll();
        assertThat(noters).hasSize(databaseSizeBeforeCreate + 1);
        Noter testNoter = noters.get(noters.size() - 1);
        assertThat(testNoter.getNote()).isEqualTo(DEFAULT_NOTE);

        // Validate the Noter in ElasticSearch
        Noter noterEs = noterSearchRepository.findOne(testNoter.getId());
        assertThat(noterEs).isEqualToComparingFieldByField(testNoter);
    }

    @Test
    @Transactional
    public void getAllNoters() throws Exception {
        // Initialize the database
        noterRepository.saveAndFlush(noter);

        // Get all the noters
        restNoterMockMvc.perform(get("/api/noters?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(noter.getId().intValue())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())));
    }

    @Test
    @Transactional
    public void getNoter() throws Exception {
        // Initialize the database
        noterRepository.saveAndFlush(noter);

        // Get the noter
        restNoterMockMvc.perform(get("/api/noters/{id}", noter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(noter.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNoter() throws Exception {
        // Get the noter
        restNoterMockMvc.perform(get("/api/noters/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNoter() throws Exception {
        // Initialize the database
        noterRepository.saveAndFlush(noter);
        noterSearchRepository.save(noter);
        int databaseSizeBeforeUpdate = noterRepository.findAll().size();

        // Update the noter
        Noter updatedNoter = new Noter();
        updatedNoter.setId(noter.getId());
        updatedNoter.setNote(UPDATED_NOTE);

        restNoterMockMvc.perform(put("/api/noters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNoter)))
                .andExpect(status().isOk());

        // Validate the Noter in the database
        List<Noter> noters = noterRepository.findAll();
        assertThat(noters).hasSize(databaseSizeBeforeUpdate);
        Noter testNoter = noters.get(noters.size() - 1);
        assertThat(testNoter.getNote()).isEqualTo(UPDATED_NOTE);

        // Validate the Noter in ElasticSearch
        Noter noterEs = noterSearchRepository.findOne(testNoter.getId());
        assertThat(noterEs).isEqualToComparingFieldByField(testNoter);
    }

    @Test
    @Transactional
    public void deleteNoter() throws Exception {
        // Initialize the database
        noterRepository.saveAndFlush(noter);
        noterSearchRepository.save(noter);
        int databaseSizeBeforeDelete = noterRepository.findAll().size();

        // Get the noter
        restNoterMockMvc.perform(delete("/api/noters/{id}", noter.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean noterExistsInEs = noterSearchRepository.exists(noter.getId());
        assertThat(noterExistsInEs).isFalse();

        // Validate the database is empty
        List<Noter> noters = noterRepository.findAll();
        assertThat(noters).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNoter() throws Exception {
        // Initialize the database
        noterRepository.saveAndFlush(noter);
        noterSearchRepository.save(noter);

        // Search the noter
        restNoterMockMvc.perform(get("/api/_search/noters?query=id:" + noter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(noter.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.doubleValue())));
    }
}
