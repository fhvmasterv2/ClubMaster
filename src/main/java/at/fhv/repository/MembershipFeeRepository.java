package at.fhv.repository;

import at.fhv.domain.MembershipFee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MembershipFee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MembershipFeeRepository extends JpaRepository<MembershipFee, Long> {

}
