package at.fhv.service.mapper;

import at.fhv.domain.*;
import at.fhv.service.dto.MemberDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Member} and its DTO {@link MemberDTO}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, ClubMapper.class})
public interface MemberMapper extends EntityMapper<MemberDTO, Member> {

    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "club.id", target = "clubId")
    MemberDTO toDto(Member member);

    @Mapping(source = "addressId", target = "address")
    @Mapping(target = "fees", ignore = true)
    @Mapping(target = "removeFees", ignore = true)
    @Mapping(source = "clubId", target = "club")
    Member toEntity(MemberDTO memberDTO);

    default Member fromId(Long id) {
        if (id == null) {
            return null;
        }
        Member member = new Member();
        member.setId(id);
        return member;
    }
}
