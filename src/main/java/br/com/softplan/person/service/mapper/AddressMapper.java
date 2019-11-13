package br.com.softplan.person.service.mapper;

import br.com.softplan.person.domain.Address;
import br.com.softplan.person.service.dto.AddressDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {CityMapper.class, UserMapper.class, PersonMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    @Mapping(source = "city.id", target = "cityId")
    @Mapping(source = "city.name", target = "cityName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "person.id", target = "personId")
    @Mapping(source = "person.name", target = "personName")
    AddressDTO toDto(Address address);

    @Mapping(source = "cityId", target = "city")
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "personId", target = "person")
    Address toEntity(AddressDTO addressDTO);

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address Address = new Address();
        Address.setId(id);
        return Address;
    }
}
