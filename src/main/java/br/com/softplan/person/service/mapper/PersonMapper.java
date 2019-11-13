package br.com.softplan.person.service.mapper;

import br.com.softplan.person.domain.Person;
import br.com.softplan.person.service.dto.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {UserMapper.class, AddressMapper.class})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "person.addresses", target = "addresses")
    PersonDTO toDto(Person person);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "addresses", ignore = true)
    Person toEntity(PersonDTO personDTO);

    default Person fromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.setId(id);
        return person;
    }
}
