package at.fhv.service;

import at.fhv.service.dto.MembershipFeeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link at.fhv.domain.MembershipFee}.
 */
public interface MembershipFeeService {

    /**
     * Save a membershipFee.
     *
     * @param membershipFeeDTO the entity to save.
     * @return the persisted entity.
     */
    MembershipFeeDTO save(MembershipFeeDTO membershipFeeDTO);

    /**
     * Get all the membershipFees.
     *
     * @return the list of entities.
     */
    List<MembershipFeeDTO> findAll();


    /**
     * Get the "id" membershipFee.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MembershipFeeDTO> findOne(Long id);

    /**
     * Delete the "id" membershipFee.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
