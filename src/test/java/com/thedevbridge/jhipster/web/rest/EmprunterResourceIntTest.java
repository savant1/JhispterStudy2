package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Emprunter;
import com.thedevbridge.jhipster.repository.EmprunterRepository;
import com.thedevbridge.jhipster.repository.search.EmprunterSearchRepository;

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
 * Test class for the EmprunterResource REST controller.
 *
 * @see EmprunterResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmprunterResourceIntTest {


    @Inject
    private EmprunterRepository emprunterRepository;

    @Inject
    private EmprunterSearchRepository emprunterSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmprunterMockMvc;

    private Emprunter emprunter;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmprunterResource emprunterResource = new EmprunterResource();
        ReflectionTestUtils.setField(emprunterResource, "emprunterSearchRepository", emprunterSearchRepository);
        ReflectionTestUtils.setField(emprunterResource, "emprunterRepository", emprunterRepository);
        this.restEmprunterMockMvc = MockMvcBuilders.standaloneSetup(emprunterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        emprunterSearchRepository.deleteAll();
        emprunter = new Emprunter();
    }

    @Test
    @Transactional
    public void createEmprunter() throws Exception {
        int databaseSizeBeforeCreate = emprunterRepository.findAll().size();

        // Create the Emprunter

        restEmprunterMockMvc.perform(post("/api/emprunters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(emprunter)))
                .andExpect(status().isCreated());

        // Validate the Emprunter in the database
        List<Emprunter> emprunters = emprunterRepository.findAll();
        assertThat(emprunters).hasSize(databaseSizeBeforeCreate + 1);
        Emprunter testEmprunter = emprunters.get(emprunters.size() - 1);

        // Validate the Emprunter in ElasticSearch
        Emprunter emprunterEs = emprunterSearchRepository.findOne(testEmprunter.getId());
        assertThat(emprunterEs).isEqualToComparingFieldByField(testEmprunter);
    }

    @Test
    @Transactional
    public void getAllEmprunters() throws Exception {
        // Initialize the database
        emprunterRepository.saveAndFlush(emprunter);

        // Get all the emprunters
        restEmprunterMockMvc.perform(get("/api/emprunters?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(emprunter.getId().intValue())));
    }

    @Test
    @Transactional
    public void getEmprunter() throws Exception {
        // Initialize the database
        emprunterRepository.saveAndFlush(emprunter);

        // Get the emprunter
        restEmprunterMockMvc.perform(get("/api/emprunters/{id}", emprunter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(emprunter.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEmprunter() throws Exception {
        // Get the emprunter
        restEmprunterMockMvc.perform(get("/api/emprunters/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmprunter() throws Exception {
        // Initialize the database
        emprunterRepository.saveAndFlush(emprunter);
        emprunterSearchRepository.save(emprunter);
        int databaseSizeBeforeUpdate = emprunterRepository.findAll().size();

        // Update the emprunter
        Emprunter updatedEmprunter = new Emprunter();
        updatedEmprunter.setId(emprunter.getId());

        restEmprunterMockMvc.perform(put("/api/emprunters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmprunter)))
                .andExpect(status().isOk());

        // Validate the Emprunter in the database
        List<Emprunter> emprunters = emprunterRepository.findAll();
        assertThat(emprunters).hasSize(databaseSizeBeforeUpdate);
        Emprunter testEmprunter = emprunters.get(emprunters.size() - 1);

        // Validate the Emprunter in ElasticSearch
        Emprunter emprunterEs = emprunterSearchRepository.findOne(testEmprunter.getId());
        assertThat(emprunterEs).isEqualToComparingFieldByField(testEmprunter);
    }

    @Test
    @Transactional
    public void deleteEmprunter() throws Exception {
        // Initialize the database
        emprunterRepository.saveAndFlush(emprunter);
        emprunterSearchRepository.save(emprunter);
        int databaseSizeBeforeDelete = emprunterRepository.findAll().size();

        // Get the emprunter
        restEmprunterMockMvc.perform(delete("/api/emprunters/{id}", emprunter.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean emprunterExistsInEs = emprunterSearchRepository.exists(emprunter.getId());
        assertThat(emprunterExistsInEs).isFalse();

        // Validate the database is empty
        List<Emprunter> emprunters = emprunterRepository.findAll();
        assertThat(emprunters).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEmprunter() throws Exception {
        // Initialize the database
        emprunterRepository.saveAndFlush(emprunter);
        emprunterSearchRepository.save(emprunter);

        // Search the emprunter
        restEmprunterMockMvc.perform(get("/api/_search/emprunters?query=id:" + emprunter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emprunter.getId().intValue())));
    }
}
