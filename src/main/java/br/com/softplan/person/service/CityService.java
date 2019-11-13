package br.com.softplan.person.service;

import br.com.softplan.person.service.dto.CityDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface CityService {

    /**
     * Save a city.
     *
     * @param cityDTO the entity to save.
     * @return the persisted entity.
     */
    CityDTO save(CityDTO cityDTO);

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CityDTO> findAll(Pageable pageable);

    /**
     * Get all the cities (without pagination).
     *
     * @param stateId to filter by state.
     * @return the list of entities.
     */
    List<CityDTO> getCitiesByStateId(Long stateId);

    /**
     * Get the "id" city.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CityDTO> findOne(Long id);

    /**
     * Delete the "id" city.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * @param cityDTO
     */
    void validateCreation(CityDTO cityDTO);

    /**
     * @param cityDTO
     */
    void validateUpdate(CityDTO cityDTO);
}
