package at.fhv.web.rest;

import at.fhv.ClubmasterApp;
import at.fhv.domain.Club;
import at.fhv.repository.ClubRepository;
import at.fhv.service.ClubService;
import at.fhv.service.dto.ClubDTO;
import at.fhv.service.mapper.ClubMapper;
import at.fhv.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static at.fhv.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.fhv.domain.enumeration.ClubType;
/**
 * Integration tests for the {@link ClubResource} REST controller.
 */
@SpringBootTest(classes = ClubmasterApp.class)
public class ClubResourceIT {

    private static final String DEFAULT_CLUB_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLUB_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ClubType DEFAULT_TYPE = ClubType.SPORT;
    private static final ClubType UPDATED_TYPE = ClubType.MUSIC;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubService clubService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restClubMockMvc;

    private Club club;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClubResource clubResource = new ClubResource(clubService);
        this.restClubMockMvc = MockMvcBuilders.standaloneSetup(clubResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createEntity(EntityManager em) {
        Club club = new Club()
            .clubName(DEFAULT_CLUB_NAME)
            .creationDate(DEFAULT_CREATION_DATE)
            .type(DEFAULT_TYPE);
        return club;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createUpdatedEntity(EntityManager em) {
        Club club = new Club()
            .clubName(UPDATED_CLUB_NAME)
            .creationDate(UPDATED_CREATION_DATE)
            .type(UPDATED_TYPE);
        return club;
    }

    @BeforeEach
    public void initTest() {
        club = createEntity(em);
    }

    @Test
    @Transactional
    public void createClub() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);
        restClubMockMvc.perform(post("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isCreated());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate + 1);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getClubName()).isEqualTo(DEFAULT_CLUB_NAME);
        assertThat(testClub.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testClub.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createClubWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club with an existing ID
        club.setId(1L);
        ClubDTO clubDTO = clubMapper.toDto(club);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubMockMvc.perform(post("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkClubNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubRepository.findAll().size();
        // set the field null
        club.setClubName(null);

        // Create the Club, which fails.
        ClubDTO clubDTO = clubMapper.toDto(club);

        restClubMockMvc.perform(post("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClubs() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList
        restClubMockMvc.perform(get("/api/clubs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].clubName").value(hasItem(DEFAULT_CLUB_NAME)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(club.getId().intValue()))
            .andExpect(jsonPath("$.clubName").value(DEFAULT_CLUB_NAME))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClub() throws Exception {
        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club
        Club updatedClub = clubRepository.findById(club.getId()).get();
        // Disconnect from session so that the updates on updatedClub are not directly saved in db
        em.detach(updatedClub);
        updatedClub
            .clubName(UPDATED_CLUB_NAME)
            .creationDate(UPDATED_CREATION_DATE)
            .type(UPDATED_TYPE);
        ClubDTO clubDTO = clubMapper.toDto(updatedClub);

        restClubMockMvc.perform(put("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getClubName()).isEqualTo(UPDATED_CLUB_NAME);
        assertThat(testClub.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testClub.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubMockMvc.perform(put("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeDelete = clubRepository.findAll().size();

        // Delete the club
        restClubMockMvc.perform(delete("/api/clubs/{id}", club.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
