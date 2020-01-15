package at.fhv.service.mapper;

import at.fhv.domain.*;
import at.fhv.service.dto.MembershipFeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MembershipFee} and its DTO {@link MembershipFeeDTO}.
 */
@Mapper(componentModel = "spring", uses = {MemberMapper.class})
public interface MembershipFeeMapper extends EntityMapper<MembershipFeeDTO, MembershipFee> {

    @Mapping(source = "member.id", target = "memberId")
    MembershipFeeDTO toDto(MembershipFee membershipFee);

    @Mapping(source = "memberId", target = "member")
    MembershipFee toEntity(MembershipFeeDTO membershipFeeDTO);

    default MembershipFee fromId(Long id) {
        if (id == null) {
            return null;
        }
        MembershipFee membershipFee = new MembershipFee();
        membershipFee.setId(id);
        return membershipFee;
    }
}
