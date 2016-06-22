package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Commenter;
import com.thedevbridge.jhipster.repository.CommenterRepository;
import com.thedevbridge.jhipster.repository.search.CommenterSearchRepository;

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
 * Test class for the CommenterResource REST controller.
 *
 * @see CommenterResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class CommenterResourceIntTest {


    @Inject
    private CommenterRepository commenterRepository;

    @Inject
    private CommenterSearchRepository commenterSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommenterMockMvc;

    private Commenter commenter;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommenterResource commenterResource = new CommenterResource();
        ReflectionTestUtils.setField(commenterResource, "commenterSearchRepository", commenterSearchRepository);
        ReflectionTestUtils.setField(commenterResource, "commenterRepository", commenterRepository);
        this.restCommenterMockMvc = MockMvcBuilders.standaloneSetup(commenterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        commenterSearchRepository.deleteAll();
        commenter = new Commenter();
    }

    @Test
    @Transactional
    public void createCommenter() throws Exception {
        int databaseSizeBeforeCreate = commenterRepository.findAll().size();

        // Create the Commenter

        restCommenterMockMvc.perform(post("/api/commenters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(commenter)))
                .andExpect(status().isCreated());

        // Validate the Commenter in the database
        List<Commenter> commenters = commenterRepository.findAll();
        assertThat(commenters).hasSize(databaseSizeBeforeCreate + 1);
        Commenter testCommenter = commenters.get(commenters.size() - 1);

        // Validate the Commenter in ElasticSearch
        Commenter commenterEs = commenterSearchRepository.findOne(testCommenter.getId());
        assertThat(commenterEs).isEqualToComparingFieldByField(testCommenter);
    }

    @Test
    @Transactional
    public void getAllCommenters() throws Exception {
        // Initialize the database
        commenterRepository.saveAndFlush(commenter);

        // Get all the commenters
        restCommenterMockMvc.perform(get("/api/commenters?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(commenter.getId().intValue())));
    }

    @Test
    @Transactional
    public void getCommenter() throws Exception {
        // Initialize the database
        commenterRepository.saveAndFlush(commenter);

        // Get the commenter
        restCommenterMockMvc.perform(get("/api/commenters/{id}", commenter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(commenter.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCommenter() throws Exception {
        // Get the commenter
        restCommenterMockMvc.perform(get("/api/commenters/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommenter() throws Exception {
        // Initialize the database
        commenterRepository.saveAndFlush(commenter);
        commenterSearchRepository.save(commenter);
        int databaseSizeBeforeUpdate = commenterRepository.findAll().size();

        // Update the commenter
        Commenter updatedCommenter = new Commenter();
        updatedCommenter.setId(commenter.getId());

        restCommenterMockMvc.perform(put("/api/commenters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCommenter)))
                .andExpect(status().isOk());

        // Validate the Commenter in the database
        List<Commenter> commenters = commenterRepository.findAll();
        assertThat(commenters).hasSize(databaseSizeBeforeUpdate);
        Commenter testCommenter = commenters.get(commenters.size() - 1);

        // Validate the Commenter in ElasticSearch
        Commenter commenterEs = commenterSearchRepository.findOne(testCommenter.getId());
        assertThat(commenterEs).isEqualToComparingFieldByField(testCommenter);
    }

    @Test
    @Transactional
    public void deleteCommenter() throws Exception {
        // Initialize the database
        commenterRepository.saveAndFlush(commenter);
        commenterSearchRepository.save(commenter);
        int databaseSizeBeforeDelete = commenterRepository.findAll().size();

        // Get the commenter
        restCommenterMockMvc.perform(delete("/api/commenters/{id}", commenter.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean commenterExistsInEs = commenterSearchRepository.exists(commenter.getId());
        assertThat(commenterExistsInEs).isFalse();

        // Validate the database is empty
        List<Commenter> commenters = commenterRepository.findAll();
        assertThat(commenters).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCommenter() throws Exception {
        // Initialize the database
        commenterRepository.saveAndFlush(commenter);
        commenterSearchRepository.save(commenter);

        // Search the commenter
        restCommenterMockMvc.perform(get("/api/_search/commenters?query=id:" + commenter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commenter.getId().intValue())));
    }
}
