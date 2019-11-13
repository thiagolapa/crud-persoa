package br.com.softplan.person.service.mapper;

import br.com.softplan.person.domain.Country;
import br.com.softplan.person.service.dto.CountryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 *
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "login")
    CountryDTO toDto(Country country);

    @Mapping(target = "states", ignore = true)
    @Mapping(source = "userId", target = "user")
    Country toEntity(CountryDTO countryDTO);

    default Country fromId(Long id) {
        if (id == null) {
            return null;
        }
        Country country = new Country();
        country.setId(id);
        return country;
    }
}
