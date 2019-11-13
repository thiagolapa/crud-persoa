package br.com.softplan.person.service.impl;

import br.com.softplan.person.domain.Person;
import br.com.softplan.person.domain.User;
import br.com.softplan.person.repository.PersonRepository;
import br.com.softplan.person.service.AddressService;
import br.com.softplan.person.service.PersonService;
import br.com.softplan.person.service.UserService;
import br.com.softplan.person.service.dto.AddressDTO;
import br.com.softplan.person.service.dto.PersonDTO;
import br.com.softplan.person.service.mapper.AddressMapper;
import br.com.softplan.person.service.mapper.PersonMapper;
import br.com.softplan.person.service.util.StringUtils;
import br.com.softplan.person.service.util.ThrowUtils;
import io.github.jhipster.web.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    private static final String ENTITY_NAME = "person";

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private AddressMapper addressMapper;

    /**
     * Save a person.
     *
     * @param personDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PersonDTO save(PersonDTO personDTO) {
        log.debug("Request to save Person : {}", personDTO);
        validate(personDTO);
        User user = userService.getCurrentUser();
        personDTO.setUserId(user.getId());
        Person person = personRepository.save(personMapper.toEntity(personDTO));
        createAddress(person, personDTO);
        return personMapper.toDto(person);
    }

    /**
     *
     * @param person
     * @param personDTO
     */
    private void createAddress(Person person, PersonDTO personDTO) {
        removeAllAddress(personDTO);
        personDTO.getAddresses().stream().map(addressDTO -> {
            addressDTO.setPersonId(person.getId());
            return addressService.save(addressDTO);
        }).collect(Collectors.toList());
    }

    /**
     * remove all address.
     */
    public void removeAllAddress(PersonDTO personDTO) {
        if (personDTO.getId() != null) {
            addressService.removeAll(addressMapper.toEntity(personDTO.getAddresses()));
        }
    }

    /**
     *  validate person.
     * @param personDTO
     */
    public void validate(PersonDTO personDTO) {
        if (personDTO.getAddresses().isEmpty()) {
            ThrowUtils.badRequest("Address not add.", ENTITY_NAME, "addressNotAdd");
        }
        if (personDTO.getDateOfBirth() == null) {
            ThrowUtils.badRequest("Date of Birth null.", ENTITY_NAME, "dateOfBirthNull");
        }
        if (personDTO.getId() == null) {
            validateCreation(personDTO);
        } else {
            validateUpdate(personDTO);
        }
    }

    /**
     * Get all the people.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<PersonDTO>> findAll(String query, String sex, ZonedDateTime fromDate, ZonedDateTime toDate, Boolean paginate, Pageable pageable) {
        log.debug("Request to get all People");
        query = isNullOrEmpty(query);
        sex = isNullOrEmpty(sex);
        Page<PersonDTO> personDTOS;
        if (Boolean.TRUE.equals(paginate)) {
            personDTOS = personRepository.findAll(query, sex, fromDate, toDate, pageable).map(personMapper::toDto);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), personDTOS);
            return new ResponseEntity<>(personDTOS.getContent(), headers, HttpStatus.OK);
        }
        personDTOS = this.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), personDTOS);
        return ResponseEntity.ok().headers(headers).body(personDTOS.getContent());
    }

    /**
     *
     * @param parameter
     * @return
     */
    public String isNullOrEmpty(String parameter) {
        return StringUtils.isNullOrEmpty(parameter) ? null : parameter.toUpperCase();
    }

    /**
     * Get all the people.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PersonDTO> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return personRepository.findAll(pageable)
            .map(personMapper::toDto);
    }

    /**
     * Get one person by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PersonDTO> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            Optional<PersonDTO> personDTO = person.map(personMapper::toDto);
            findAddressesByPersonId(personDTO);
        }
        return person.map(personMapper::toDto);
    }

    /**
     *
     * @param personDTO
     */
    private void findAddressesByPersonId(Optional<PersonDTO> personDTO) {
        personDTO.get().getAddresses().addAll(addressService.findAddressesByPersonId(personDTO.get().getId()));
    }

    /**
     * Delete the person by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        Optional<Person> person = personRepository.findById(id);
        validatePresence(person);
        List<AddressDTO> addressDTOS = addressService.findAddressesByPersonId(id);
        addressService.removeAll(addressMapper.toEntity(addressDTOS));
        personRepository.deleteById(id);
    }

    @Override
    public void validateCreation(PersonDTO personDTO) {
        if (personDTO.getId() != null) {
            ThrowUtils.badRequest("Person cannot have an ID", ENTITY_NAME, "idexists");
        }
        validateFields(personDTO, true);
    }

    @Override
    public void validateUpdate(PersonDTO personDTO) {
        if (personDTO.getId() == null) {
            ThrowUtils.badRequest("Person cannot have ID null", ENTITY_NAME, "idnull");
        }
        validateFields(personDTO, false);
        Optional<Person> person = personRepository.findById(personDTO.getId());
    }

    /**
     *
     * @param personDTO
     * @param isCreate
     */
    private void validateFields(PersonDTO personDTO, boolean isCreate) {
        if (personDTO.getName().isEmpty()) {
            ThrowUtils.badRequest("Person must is null", ENTITY_NAME, "personNameNotNull");
        }
        if (isCreate) {
            if (personRepository.existsCpf(personDTO.getCpf().trim().toLowerCase())) {
                ThrowUtils.badRequest("Person cpf already exists", ENTITY_NAME, "personCpfExists");
            }
            if (personRepository.existsWithName(personDTO.getName().trim().toLowerCase())) {
                ThrowUtils.badRequest("Person name already exists", ENTITY_NAME, "personNameExists");
            }
        } else {
            if (personRepository.existsCpf(personDTO.getId(), personDTO.getCpf().trim().toLowerCase())) {
                ThrowUtils.badRequest("Person cpf already exists", ENTITY_NAME, "personCpfExists");
            }
            if (personRepository.existsWithName(personDTO.getName().trim().toLowerCase(), personDTO.getId())) {
                ThrowUtils.badRequest("Person name already exists", ENTITY_NAME, "personNameExists");
            }
        }
    }

    /**
     *
     * @param person
     */
    private void validatePresence(Optional<Person> person) {
        if (!person.isPresent()) {
            ThrowUtils.gone("Person not found", ENTITY_NAME, "personNotFound");
        }
    }
}
