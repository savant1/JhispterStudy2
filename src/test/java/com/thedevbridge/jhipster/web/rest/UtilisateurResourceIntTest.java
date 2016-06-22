package com.thedevbridge.jhipster.web.rest;

import com.thedevbridge.jhipster.ProjetApp;
import com.thedevbridge.jhipster.domain.Utilisateur;
import com.thedevbridge.jhipster.repository.UtilisateurRepository;
import com.thedevbridge.jhipster.repository.search.UtilisateurSearchRepository;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UtilisateurResource REST controller.
 *
 * @see UtilisateurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ProjetApp.class)
@WebAppConfiguration
@IntegrationTest
public class UtilisateurResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAA";
    private static final String UPDATED_NOM = "BBBBB";
    private static final String DEFAULT_PRENOM = "AAAAA";
    private static final String UPDATED_PRENOM = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";
    private static final String DEFAULT_PASS = "AAAAA";
    private static final String UPDATED_PASS = "BBBBB";

    private static final Boolean DEFAULT_BLACKLIST = false;
    private static final Boolean UPDATED_BLACKLIST = true;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    @Inject
    private UtilisateurRepository utilisateurRepository;

    @Inject
    private UtilisateurSearchRepository utilisateurSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UtilisateurResource utilisateurResource = new UtilisateurResource();
        ReflectionTestUtils.setField(utilisateurResource, "utilisateurSearchRepository", utilisateurSearchRepository);
        ReflectionTestUtils.setField(utilisateurResource, "utilisateurRepository", utilisateurRepository);
        this.restUtilisateurMockMvc = MockMvcBuilders.standaloneSetup(utilisateurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        utilisateurSearchRepository.deleteAll();
        utilisateur = new Utilisateur();
        utilisateur.setNom(DEFAULT_NOM);
        utilisateur.setPrenom(DEFAULT_PRENOM);
        utilisateur.setEmail(DEFAULT_EMAIL);
        utilisateur.setPass(DEFAULT_PASS);
        utilisateur.setBlacklist(DEFAULT_BLACKLIST);
        utilisateur.setPicture(DEFAULT_PICTURE);
        utilisateur.setPictureContentType(DEFAULT_PICTURE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createUtilisateur() throws Exception {
        int databaseSizeBeforeCreate = utilisateurRepository.findAll().size();

        // Create the Utilisateur

        restUtilisateurMockMvc.perform(post("/api/utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(utilisateur)))
                .andExpect(status().isCreated());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeCreate + 1);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testUtilisateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUtilisateur.getPass()).isEqualTo(DEFAULT_PASS);
        assertThat(testUtilisateur.isBlacklist()).isEqualTo(DEFAULT_BLACKLIST);
        assertThat(testUtilisateur.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testUtilisateur.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);

        // Validate the Utilisateur in ElasticSearch
        Utilisateur utilisateurEs = utilisateurSearchRepository.findOne(testUtilisateur.getId());
        assertThat(utilisateurEs).isEqualToComparingFieldByField(testUtilisateur);
    }

    @Test
    @Transactional
    public void getAllUtilisateurs() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurs
        restUtilisateurMockMvc.perform(get("/api/utilisateurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].pass").value(hasItem(DEFAULT_PASS.toString())))
                .andExpect(jsonPath("$.[*].blacklist").value(hasItem(DEFAULT_BLACKLIST.booleanValue())))
                .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }

    @Test
    @Transactional
    public void getUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.pass").value(DEFAULT_PASS.toString()))
            .andExpect(jsonPath("$.blacklist").value(DEFAULT_BLACKLIST.booleanValue()))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)));
    }

    @Test
    @Transactional
    public void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get("/api/utilisateurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);
        int databaseSizeBeforeUpdate = utilisateurRepository.findAll().size();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = new Utilisateur();
        updatedUtilisateur.setId(utilisateur.getId());
        updatedUtilisateur.setNom(UPDATED_NOM);
        updatedUtilisateur.setPrenom(UPDATED_PRENOM);
        updatedUtilisateur.setEmail(UPDATED_EMAIL);
        updatedUtilisateur.setPass(UPDATED_PASS);
        updatedUtilisateur.setBlacklist(UPDATED_BLACKLIST);
        updatedUtilisateur.setPicture(UPDATED_PICTURE);
        updatedUtilisateur.setPictureContentType(UPDATED_PICTURE_CONTENT_TYPE);

        restUtilisateurMockMvc.perform(put("/api/utilisateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUtilisateur)))
                .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeUpdate);
        Utilisateur testUtilisateur = utilisateurs.get(utilisateurs.size() - 1);
        assertThat(testUtilisateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testUtilisateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testUtilisateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUtilisateur.getPass()).isEqualTo(UPDATED_PASS);
        assertThat(testUtilisateur.isBlacklist()).isEqualTo(UPDATED_BLACKLIST);
        assertThat(testUtilisateur.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testUtilisateur.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);

        // Validate the Utilisateur in ElasticSearch
        Utilisateur utilisateurEs = utilisateurSearchRepository.findOne(testUtilisateur.getId());
        assertThat(utilisateurEs).isEqualToComparingFieldByField(testUtilisateur);
    }

    @Test
    @Transactional
    public void deleteUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);
        int databaseSizeBeforeDelete = utilisateurRepository.findAll().size();

        // Get the utilisateur
        restUtilisateurMockMvc.perform(delete("/api/utilisateurs/{id}", utilisateur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean utilisateurExistsInEs = utilisateurSearchRepository.exists(utilisateur.getId());
        assertThat(utilisateurExistsInEs).isFalse();

        // Validate the database is empty
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        assertThat(utilisateurs).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUtilisateur() throws Exception {
        // Initialize the database
        utilisateurRepository.saveAndFlush(utilisateur);
        utilisateurSearchRepository.save(utilisateur);

        // Search the utilisateur
        restUtilisateurMockMvc.perform(get("/api/_search/utilisateurs?query=id:" + utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].pass").value(hasItem(DEFAULT_PASS.toString())))
            .andExpect(jsonPath("$.[*].blacklist").value(hasItem(DEFAULT_BLACKLIST.booleanValue())))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))));
    }
}
