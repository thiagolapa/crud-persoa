package br.com.softplan.person.service.mapper;

import br.com.softplan.person.domain.City;
import br.com.softplan.person.service.dto.CityDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {StateMapper.class, UserMapper.class})
public interface CityMapper extends EntityMapper<CityDTO, City> {

    @Mapping(source = "state.country.id", target = "countryId")
    @Mapping(source = "state.country.name", target = "countryName")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "login")
    @Mapping(target = "addresses", ignore = true)
    CityDTO toDto(City city);

    @Mapping(source = "stateId", target = "state")
    @Mapping(source = "userId", target = "user")
    @Mapping(target = "addresses", ignore = true)
    City toEntity(CityDTO cityDTO);

    default City fromId(Long id) {
        if (id == null) {
            return null;
        }
        City city = new City();
        city.setId(id);
        return city;
    }
}
