package at.fhv.service.impl;

import at.fhv.service.ClubService;
import at.fhv.domain.Club;
import at.fhv.repository.ClubRepository;
import at.fhv.service.dto.ClubDTO;
import at.fhv.service.mapper.ClubMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Club}.
 */
@Service
@Transactional
public class ClubServiceImpl implements ClubService {

    private final Logger log = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;

    public ClubServiceImpl(ClubRepository clubRepository, ClubMapper clubMapper) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
    }

    /**
     * Save a club.
     *
     * @param clubDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ClubDTO save(ClubDTO clubDTO) {
        log.debug("Request to save Club : {}", clubDTO);
        Club club = clubMapper.toEntity(clubDTO);
        club = clubRepository.save(club);
        return clubMapper.toDto(club);
    }

    /**
     * Get all the clubs.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClubDTO> findAll() {
        log.debug("Request to get all Clubs");
        return clubRepository.findAll().stream()
            .map(clubMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one club by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ClubDTO> findOne(Long id) {
        log.debug("Request to get Club : {}", id);
        return clubRepository.findById(id)
            .map(clubMapper::toDto);
    }

    /**
     * Delete the club by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Club : {}", id);
        clubRepository.deleteById(id);
    }
}
