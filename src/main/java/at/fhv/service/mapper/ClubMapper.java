package at.fhv.service.mapper;

import at.fhv.domain.*;
import at.fhv.service.dto.ClubDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Club} and its DTO {@link ClubDTO}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface ClubMapper extends EntityMapper<ClubDTO, Club> {

    @Mapping(source = "address.id", target = "addressId")
    ClubDTO toDto(Club club);

    @Mapping(source = "addressId", target = "address")
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "removeMember", ignore = true)
    @Mapping(target = "events", ignore = true)
    @Mapping(target = "removeEvent", ignore = true)
    Club toEntity(ClubDTO clubDTO);

    default Club fromId(Long id) {
        if (id == null) {
            return null;
        }
        Club club = new Club();
        club.setId(id);
        return club;
    }
}
