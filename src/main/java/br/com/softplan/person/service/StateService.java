package br.com.softplan.person.service;

import br.com.softplan.person.service.dto.StateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface StateService {

    /**
     * Save a state.
     *
     * @param stateDTO the entity to save.
     * @return the persisted entity.
     */
    StateDTO save(StateDTO stateDTO);

    /**
     * Get all the states.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StateDTO> findAll(Pageable pageable);

    /**
     * Get all the states (without pagination).
     *
     * @param countryId to filter by country.
     * @return the list of entities.
     */
    List<StateDTO> getStatesByCountryId(Long countryId);

    /**
     * Get the "id" state.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StateDTO> findOne(Long id);

    /**
     * Delete the "id" state.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * @param stateDTO
     */
    void validateCreation(StateDTO stateDTO);

    /**
     * @param stateDTO
     */
    void validateUpdate(StateDTO stateDTO);
}
