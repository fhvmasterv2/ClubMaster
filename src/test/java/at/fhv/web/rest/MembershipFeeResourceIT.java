package at.fhv.web.rest;

import at.fhv.ClubmasterApp;
import at.fhv.domain.MembershipFee;
import at.fhv.repository.MembershipFeeRepository;
import at.fhv.service.MembershipFeeService;
import at.fhv.service.dto.MembershipFeeDTO;
import at.fhv.service.mapper.MembershipFeeMapper;
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

/**
 * Integration tests for the {@link MembershipFeeResource} REST controller.
 */
@SpringBootTest(classes = ClubmasterApp.class)
public class MembershipFeeResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_PAID = false;
    private static final Boolean UPDATED_IS_PAID = true;

    @Autowired
    private MembershipFeeRepository membershipFeeRepository;

    @Autowired
    private MembershipFeeMapper membershipFeeMapper;

    @Autowired
    private MembershipFeeService membershipFeeService;

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

    private MockMvc restMembershipFeeMockMvc;

    private MembershipFee membershipFee;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MembershipFeeResource membershipFeeResource = new MembershipFeeResource(membershipFeeService);
        this.restMembershipFeeMockMvc = MockMvcBuilders.standaloneSetup(membershipFeeResource)
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
    public static MembershipFee createEntity(EntityManager em) {
        MembershipFee membershipFee = new MembershipFee()
            .amount(DEFAULT_AMOUNT)
            .dueDate(DEFAULT_DUE_DATE)
            .isPaid(DEFAULT_IS_PAID);
        return membershipFee;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MembershipFee createUpdatedEntity(EntityManager em) {
        MembershipFee membershipFee = new MembershipFee()
            .amount(UPDATED_AMOUNT)
            .dueDate(UPDATED_DUE_DATE)
            .isPaid(UPDATED_IS_PAID);
        return membershipFee;
    }

    @BeforeEach
    public void initTest() {
        membershipFee = createEntity(em);
    }

    @Test
    @Transactional
    public void createMembershipFee() throws Exception {
        int databaseSizeBeforeCreate = membershipFeeRepository.findAll().size();

        // Create the MembershipFee
        MembershipFeeDTO membershipFeeDTO = membershipFeeMapper.toDto(membershipFee);
        restMembershipFeeMockMvc.perform(post("/api/membership-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(membershipFeeDTO)))
            .andExpect(status().isCreated());

        // Validate the MembershipFee in the database
        List<MembershipFee> membershipFeeList = membershipFeeRepository.findAll();
        assertThat(membershipFeeList).hasSize(databaseSizeBeforeCreate + 1);
        MembershipFee testMembershipFee = membershipFeeList.get(membershipFeeList.size() - 1);
        assertThat(testMembershipFee.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testMembershipFee.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testMembershipFee.isIsPaid()).isEqualTo(DEFAULT_IS_PAID);
    }

    @Test
    @Transactional
    public void createMembershipFeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = membershipFeeRepository.findAll().size();

        // Create the MembershipFee with an existing ID
        membershipFee.setId(1L);
        MembershipFeeDTO membershipFeeDTO = membershipFeeMapper.toDto(membershipFee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMembershipFeeMockMvc.perform(post("/api/membership-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(membershipFeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipFee in the database
        List<MembershipFee> membershipFeeList = membershipFeeRepository.findAll();
        assertThat(membershipFeeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMembershipFees() throws Exception {
        // Initialize the database
        membershipFeeRepository.saveAndFlush(membershipFee);

        // Get all the membershipFeeList
        restMembershipFeeMockMvc.perform(get("/api/membership-fees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(membershipFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMembershipFee() throws Exception {
        // Initialize the database
        membershipFeeRepository.saveAndFlush(membershipFee);

        // Get the membershipFee
        restMembershipFeeMockMvc.perform(get("/api/membership-fees/{id}", membershipFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(membershipFee.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMembershipFee() throws Exception {
        // Get the membershipFee
        restMembershipFeeMockMvc.perform(get("/api/membership-fees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMembershipFee() throws Exception {
        // Initialize the database
        membershipFeeRepository.saveAndFlush(membershipFee);

        int databaseSizeBeforeUpdate = membershipFeeRepository.findAll().size();

        // Update the membershipFee
        MembershipFee updatedMembershipFee = membershipFeeRepository.findById(membershipFee.getId()).get();
        // Disconnect from session so that the updates on updatedMembershipFee are not directly saved in db
        em.detach(updatedMembershipFee);
        updatedMembershipFee
            .amount(UPDATED_AMOUNT)
            .dueDate(UPDATED_DUE_DATE)
            .isPaid(UPDATED_IS_PAID);
        MembershipFeeDTO membershipFeeDTO = membershipFeeMapper.toDto(updatedMembershipFee);

        restMembershipFeeMockMvc.perform(put("/api/membership-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(membershipFeeDTO)))
            .andExpect(status().isOk());

        // Validate the MembershipFee in the database
        List<MembershipFee> membershipFeeList = membershipFeeRepository.findAll();
        assertThat(membershipFeeList).hasSize(databaseSizeBeforeUpdate);
        MembershipFee testMembershipFee = membershipFeeList.get(membershipFeeList.size() - 1);
        assertThat(testMembershipFee.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testMembershipFee.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testMembershipFee.isIsPaid()).isEqualTo(UPDATED_IS_PAID);
    }

    @Test
    @Transactional
    public void updateNonExistingMembershipFee() throws Exception {
        int databaseSizeBeforeUpdate = membershipFeeRepository.findAll().size();

        // Create the MembershipFee
        MembershipFeeDTO membershipFeeDTO = membershipFeeMapper.toDto(membershipFee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMembershipFeeMockMvc.perform(put("/api/membership-fees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(membershipFeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MembershipFee in the database
        List<MembershipFee> membershipFeeList = membershipFeeRepository.findAll();
        assertThat(membershipFeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMembershipFee() throws Exception {
        // Initialize the database
        membershipFeeRepository.saveAndFlush(membershipFee);

        int databaseSizeBeforeDelete = membershipFeeRepository.findAll().size();

        // Delete the membershipFee
        restMembershipFeeMockMvc.perform(delete("/api/membership-fees/{id}", membershipFee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MembershipFee> membershipFeeList = membershipFeeRepository.findAll();
        assertThat(membershipFeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
