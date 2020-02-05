package at.fhv.service;

import at.fhv.service.dto.FoodDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link at.fhv.domain.Food}.
 */
public interface FoodService {

    /**
     * Save a food.
     *
     * @param foodDTO the entity to save.
     * @return the persisted entity.
     */
    FoodDTO save(FoodDTO foodDTO);

    /**
     * Get all the foods.
     *
     * @return the list of entities.
     */
    List<FoodDTO> findAll();


    /**
     * Get the "id" food.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FoodDTO> findOne(Long id);

    /**
     * Delete the "id" food.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
