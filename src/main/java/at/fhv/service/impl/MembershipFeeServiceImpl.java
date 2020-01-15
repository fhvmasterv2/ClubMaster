package at.fhv.service.impl;

import at.fhv.service.MembershipFeeService;
import at.fhv.domain.MembershipFee;
import at.fhv.repository.MembershipFeeRepository;
import at.fhv.service.dto.MembershipFeeDTO;
import at.fhv.service.mapper.MembershipFeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MembershipFee}.
 */
@Service
@Transactional
public class MembershipFeeServiceImpl implements MembershipFeeService {

    private final Logger log = LoggerFactory.getLogger(MembershipFeeServiceImpl.class);

    private final MembershipFeeRepository membershipFeeRepository;

    private final MembershipFeeMapper membershipFeeMapper;

    public MembershipFeeServiceImpl(MembershipFeeRepository membershipFeeRepository, MembershipFeeMapper membershipFeeMapper) {
        this.membershipFeeRepository = membershipFeeRepository;
        this.membershipFeeMapper = membershipFeeMapper;
    }

    /**
     * Save a membershipFee.
     *
     * @param membershipFeeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public MembershipFeeDTO save(MembershipFeeDTO membershipFeeDTO) {
        log.debug("Request to save MembershipFee : {}", membershipFeeDTO);
        MembershipFee membershipFee = membershipFeeMapper.toEntity(membershipFeeDTO);
        membershipFee = membershipFeeRepository.save(membershipFee);
        return membershipFeeMapper.toDto(membershipFee);
    }

    /**
     * Get all the membershipFees.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<MembershipFeeDTO> findAll() {
        log.debug("Request to get all MembershipFees");
        return membershipFeeRepository.findAll().stream()
            .map(membershipFeeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one membershipFee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MembershipFeeDTO> findOne(Long id) {
        log.debug("Request to get MembershipFee : {}", id);
        return membershipFeeRepository.findById(id)
            .map(membershipFeeMapper::toDto);
    }

    /**
     * Delete the membershipFee by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MembershipFee : {}", id);
        membershipFeeRepository.deleteById(id);
    }
}
