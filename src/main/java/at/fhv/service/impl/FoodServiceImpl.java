package at.fhv.service.impl;

import at.fhv.service.FoodService;
import at.fhv.domain.Food;
import at.fhv.repository.FoodRepository;
import at.fhv.service.dto.FoodDTO;
import at.fhv.service.mapper.FoodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Food}.
 */
@Service
@Transactional
public class FoodServiceImpl implements FoodService {

    private final Logger log = LoggerFactory.getLogger(FoodServiceImpl.class);

    private final FoodRepository foodRepository;

    private final FoodMapper foodMapper;

    public FoodServiceImpl(FoodRepository foodRepository, FoodMapper foodMapper) {
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
    }

    /**
     * Save a food.
     *
     * @param foodDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FoodDTO save(FoodDTO foodDTO) {
        log.debug("Request to save Food : {}", foodDTO);
        Food food = foodMapper.toEntity(foodDTO);
        food = foodRepository.save(food);
        return foodMapper.toDto(food);
    }

    /**
     * Get all the foods.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FoodDTO> findAll() {
        log.debug("Request to get all Foods");
        return foodRepository.findAll().stream()
            .map(foodMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one food by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FoodDTO> findOne(Long id) {
        log.debug("Request to get Food : {}", id);
        return foodRepository.findById(id)
            .map(foodMapper::toDto);
    }

    /**
     * Delete the food by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Food : {}", id);
        foodRepository.deleteById(id);
    }
}
