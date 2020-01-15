package at.fhv.repository;

import at.fhv.domain.Club;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Club entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {

}
