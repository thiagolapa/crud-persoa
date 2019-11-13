package br.com.softplan.person.service.mapper;

import br.com.softplan.person.domain.State;
import br.com.softplan.person.service.dto.StateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 */
@Mapper(componentModel = "spring", uses = {CountryMapper.class, UserMapper.class})
public interface StateMapper extends EntityMapper<StateDTO, State> {

    @Mapping(source = "country.id", target = "countryId")
    @Mapping(source = "country.name", target = "countryName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "login")
    StateDTO toDto(State state);

    @Mapping(source = "countryId", target = "country")
    @Mapping(target = "cities", ignore = true)
    @Mapping(source = "userId", target = "user")
    State toEntity(StateDTO stateDTO);

    default State fromId(Long id) {
        if (id == null) {
            return null;
        }
        State state = new State();
        state.setId(id);
        return state;
    }
}
