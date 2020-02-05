package at.fhv.web.rest;

import at.fhv.ClubmasterApp;
import at.fhv.domain.Food;
import at.fhv.repository.FoodRepository;
import at.fhv.service.FoodService;
import at.fhv.service.dto.FoodDTO;
import at.fhv.service.mapper.FoodMapper;
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
import java.util.List;

import static at.fhv.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FoodResource} REST controller.
 */
@SpringBootTest(classes = ClubmasterApp.class)
public class FoodResourceIT {

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private FoodService foodService;

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

    private MockMvc restFoodMockMvc;

    private Food food;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FoodResource foodResource = new FoodResource(foodService);
        this.restFoodMockMvc = MockMvcBuilders.standaloneSetup(foodResource)
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
    public static Food createEntity(EntityManager em) {
        Food food = new Food()
            .amount(DEFAULT_AMOUNT);
        return food;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Food createUpdatedEntity(EntityManager em) {
        Food food = new Food()
            .amount(UPDATED_AMOUNT);
        return food;
    }

    @BeforeEach
    public void initTest() {
        food = createEntity(em);
    }

    @Test
    @Transactional
    public void createFood() throws Exception {
        int databaseSizeBeforeCreate = foodRepository.findAll().size();

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(food);
        restFoodMockMvc.perform(post("/api/foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isCreated());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeCreate + 1);
        Food testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createFoodWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = foodRepository.findAll().size();

        // Create the Food with an existing ID
        food.setId(1L);
        FoodDTO foodDTO = foodMapper.toDto(food);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFoodMockMvc.perform(post("/api/foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllFoods() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get all the foodList
        restFoodMockMvc.perform(get("/api/foods?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(food.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        // Get the food
        restFoodMockMvc.perform(get("/api/foods/{id}", food.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(food.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFood() throws Exception {
        // Get the food
        restFoodMockMvc.perform(get("/api/foods/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Update the food
        Food updatedFood = foodRepository.findById(food.getId()).get();
        // Disconnect from session so that the updates on updatedFood are not directly saved in db
        em.detach(updatedFood);
        updatedFood
            .amount(UPDATED_AMOUNT);
        FoodDTO foodDTO = foodMapper.toDto(updatedFood);

        restFoodMockMvc.perform(put("/api/foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isOk());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
        Food testFood = foodList.get(foodList.size() - 1);
        assertThat(testFood.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingFood() throws Exception {
        int databaseSizeBeforeUpdate = foodRepository.findAll().size();

        // Create the Food
        FoodDTO foodDTO = foodMapper.toDto(food);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFoodMockMvc.perform(put("/api/foods")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(foodDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Food in the database
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFood() throws Exception {
        // Initialize the database
        foodRepository.saveAndFlush(food);

        int databaseSizeBeforeDelete = foodRepository.findAll().size();

        // Delete the food
        restFoodMockMvc.perform(delete("/api/foods/{id}", food.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
