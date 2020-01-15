package at.fhv.service;

import at.fhv.service.dto.ClubDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link at.fhv.domain.Club}.
 */
public interface ClubService {

    /**
     * Save a club.
     *
     * @param clubDTO the entity to save.
     * @return the persisted entity.
     */
    ClubDTO save(ClubDTO clubDTO);

    /**
     * Get all the clubs.
     *
     * @return the list of entities.
     */
    List<ClubDTO> findAll();


    /**
     * Get the "id" club.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClubDTO> findOne(Long id);

    /**
     * Delete the "id" club.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
