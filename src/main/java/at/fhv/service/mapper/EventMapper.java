package at.fhv.service.mapper;

import at.fhv.domain.*;
import at.fhv.service.dto.EventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Event} and its DTO {@link EventDTO}.
 */
@Mapper(componentModel = "spring", uses = {ClubMapper.class})
public interface EventMapper extends EntityMapper<EventDTO, Event> {

    @Mapping(source = "club.id", target = "clubId")
    EventDTO toDto(Event event);

    @Mapping(source = "clubId", target = "club")
    Event toEntity(EventDTO eventDTO);

    default Event fromId(Long id) {
        if (id == null) {
            return null;
        }
        Event event = new Event();
        event.setId(id);
        return event;
    }
}
