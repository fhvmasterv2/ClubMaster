package at.fhv.web.rest;

import at.fhv.ClubmasterApp;
import at.fhv.domain.Member;
import at.fhv.repository.MemberRepository;
import at.fhv.service.MemberService;
import at.fhv.service.dto.MemberDTO;
import at.fhv.service.mapper.MemberMapper;
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
 * Integration tests for the {@link MemberResource} REST controller.
 */
@SpringBootTest(classes = ClubmasterApp.class)
public class MemberResourceIT {

    private static final String DEFAULT_FNAME = "AAAAAAAAAA";
    private static final String UPDATED_FNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LNAME = "AAAAAAAAAA";
    private static final String UPDATED_LNAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOB = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOB = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService memberService;

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

    private MockMvc restMemberMockMvc;

    private Member member;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MemberResource memberResource = new MemberResource(memberService);
        this.restMemberMockMvc = MockMvcBuilders.standaloneSetup(memberResource)
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
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .fname(DEFAULT_FNAME)
            .lname(DEFAULT_LNAME)
            .dob(DEFAULT_DOB);
        return member;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity(EntityManager em) {
        Member member = new Member()
            .fname(UPDATED_FNAME)
            .lname(UPDATED_LNAME)
            .dob(UPDATED_DOB);
        return member;
    }

    @BeforeEach
    public void initTest() {
        member = createEntity(em);
    }

    @Test
    @Transactional
    public void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);
        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFname()).isEqualTo(DEFAULT_FNAME);
        assertThat(testMember.getLname()).isEqualTo(DEFAULT_LNAME);
        assertThat(testMember.getDob()).isEqualTo(DEFAULT_DOB);
    }

    @Test
    @Transactional
    public void createMemberWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // Create the Member with an existing ID
        member.setId(1L);
        MemberDTO memberDTO = memberMapper.toDto(member);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setFname(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = memberRepository.findAll().size();
        // set the field null
        member.setLname(null);

        // Create the Member, which fails.
        MemberDTO memberDTO = memberMapper.toDto(member);

        restMemberMockMvc.perform(post("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc.perform(get("/api/members?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].fname").value(hasItem(DEFAULT_FNAME)))
            .andExpect(jsonPath("$.[*].lname").value(hasItem(DEFAULT_LNAME)))
            .andExpect(jsonPath("$.[*].dob").value(hasItem(DEFAULT_DOB.toString())));
    }
    
    @Test
    @Transactional
    public void getMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.fname").value(DEFAULT_FNAME))
            .andExpect(jsonPath("$.lname").value(DEFAULT_LNAME))
            .andExpect(jsonPath("$.dob").value(DEFAULT_DOB.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get("/api/members/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).get();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .fname(UPDATED_FNAME)
            .lname(UPDATED_LNAME)
            .dob(UPDATED_DOB);
        MemberDTO memberDTO = memberMapper.toDto(updatedMember);

        restMemberMockMvc.perform(put("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFname()).isEqualTo(UPDATED_FNAME);
        assertThat(testMember.getLname()).isEqualTo(UPDATED_LNAME);
        assertThat(testMember.getDob()).isEqualTo(UPDATED_DOB);
    }

    @Test
    @Transactional
    public void updateNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Create the Member
        MemberDTO memberDTO = memberMapper.toDto(member);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc.perform(put("/api/members")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(memberDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Delete the member
        restMemberMockMvc.perform(delete("/api/members/{id}", member.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
