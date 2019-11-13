package br.com.softplan.person.service;

import br.com.softplan.person.service.dto.PersonDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface PersonService {

    /**
     * Save a person.
     *
     * @param personDTO the entity to save.
     * @return the persisted entity.
     */
    PersonDTO save(PersonDTO personDTO);

    /**
     * Get all the persons.
     * @param pageable
     * @return the list of entities.
     */
    Page<PersonDTO> findAll(Pageable pageable);

    /**
     * Get all the persons.
     * @param query
     * @param paginate
     * @param pageable
     * @return the list of entities.
     */
    ResponseEntity<List<PersonDTO>> findAll(String query, String sex, ZonedDateTime fromDate, ZonedDateTime toDate, Boolean paginate, Pageable pageable);

    /**
     * Get the "id" person.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PersonDTO> findOne(Long id);

    /**
     * Delete the "id" person.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * @param personDTO
     */
    void validateCreation(PersonDTO personDTO);

    /**
     * @param personDTO
     */
    void validateUpdate(PersonDTO personDTO);
}
