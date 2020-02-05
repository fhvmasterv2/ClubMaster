package at.fhv.service.mapper;

import at.fhv.domain.*;
import at.fhv.service.dto.FoodDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Food} and its DTO {@link FoodDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FoodMapper extends EntityMapper<FoodDTO, Food> {



    default Food fromId(Long id) {
        if (id == null) {
            return null;
        }
        Food food = new Food();
        food.setId(id);
        return food;
    }
}
