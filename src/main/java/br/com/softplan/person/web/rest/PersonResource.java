package br.com.softplan.person.web.rest;

import br.com.softplan.person.service.AddressService;
import br.com.softplan.person.service.PersonService;
import br.com.softplan.person.service.dto.PersonDTO;
import br.com.softplan.person.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.com.softplan.person.domain.Person}.
 */
@RestController
@RequestMapping("/api")
public class PersonResource {

    private final Logger log = LoggerFactory.getLogger(PersonResource.class);

    private static final String ENTITY_NAME = "person";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private PersonService personService;

    @Autowired
    private AddressService addressService;

    public PersonResource() {

    }

    /**
     * {@code POST  /people} : Create a new person.
     *
     * @param personDTO the person to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new person, or with status {@code 400 (Bad Request)} if the person has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/people")
    public ResponseEntity<PersonDTO> createPerson(@Valid @RequestBody PersonDTO personDTO) throws URISyntaxException {
        log.debug("REST request to save Person : {}", personDTO);
        if (personDTO.getId() != null) {
            throw new BadRequestAlertException("A new person cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonDTO result = personService.save(personDTO);
        return ResponseEntity.created(new URI("/api/people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /people} : Updates an existing person.
     *
     * @param personDTO the person to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated person,
     * or with status {@code 400 (Bad Request)} if the person is not valid,
     * or with status {@code 500 (Internal Server Error)} if the person couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/people")
    public ResponseEntity<PersonDTO> updatePerson(@Valid @RequestBody PersonDTO personDTO) throws URISyntaxException {
        log.debug("REST request to update Person : {}", personDTO);
        if (personDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PersonDTO result = personService.save(personDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /people} : get all the people.
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of people in body.
     */
    @GetMapping("/people")
    public ResponseEntity<List<PersonDTO>> getAllPeople(
        @RequestParam(value = "from_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime fromDate,
        @RequestParam(value = "to_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime toDate,
        @RequestParam(value = "q", required = false) String query,
        @RequestParam(value = "sex", required = false) String sex,
        @RequestParam(value = "paginate", required = false, defaultValue = "true") Boolean paginate,
        Pageable pageable) {
        log.debug("REST request to get a page of People");
        return personService.findAll(query, sex, fromDate, toDate, paginate, pageable);
    }

    /**
     * {@code GET  /people/:id} : get the "id" person.
     *
     * @param id the id of the person to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the person, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/people/{id}")
    public ResponseEntity<PersonDTO> getPerson(@PathVariable Long id) {
        log.debug("REST request to get Person : {}", id);
        Optional<PersonDTO> person = personService.findOne(id);
        return ResponseUtil.wrapOrNotFound(person);
    }

    /**
     * {@code DELETE  /people/:id} : delete the "id" person.
     *
     * @param id the id of the person to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/people/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        log.debug("REST request to delete Person : {}", id);
        personService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


//    /**
//     * DELETE /person/:id/address/:address_id : remove the "addressId" from a person.
//     *
//     * @param id
//     *            the id of the personDTO
//     * @param addressId
//     *            the id of the addressDTO to remove from the person
//     * @return the ResponseEntity with status 200 (OK)
//     */
//    @DeleteMapping("/people/{id}/address-remove/{addressId}")
//    @Secured({AuthoritiesConstants.USER})
//    public ResponseEntity<Void> removeAddressFromPerson(@PathVariable Long id, @PathVariable Long addressId) {
//        if (personService.findOne(id) == null) {
//            throw new GoneAlertException("Person not Found", ENTITY_NAME, "personNotFound");
//        }
//        Optional<AddressDTO> address = addressService.findOne(addressId);
//        if (!address.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .headers(HeaderUtil.createAlert(ENTITY_NAME, "addressNotFound", "This address has not been found"))
//                .body(null);
//        }
//        addressService.delete(address.get().getId());
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createAlert(ENTITY_NAME, "personAddressRemoved", addressId.toString())).build();
//    }
}
