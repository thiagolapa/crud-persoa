package br.com.softplan.person.service.impl;

import br.com.softplan.person.domain.City;
import br.com.softplan.person.domain.User;
import br.com.softplan.person.repository.CityRepository;
import br.com.softplan.person.service.CityService;
import br.com.softplan.person.service.UserService;
import br.com.softplan.person.service.dto.CityDTO;
import br.com.softplan.person.service.mapper.CityMapper;
import br.com.softplan.person.service.util.ThrowUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final Logger log = LoggerFactory.getLogger(CityServiceImpl.class);

    private static final String ENTITY_NAME = "city";

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private UserService userService;

    /**
     * Save a city.
     *
     * @param cityDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CityDTO save(CityDTO cityDTO) {
        log.debug("Request to save City : {}", cityDTO);
        User user = userService.getCurrentUser();
        cityDTO.setUserId(user.getId());
        City city = cityRepository.save(cityMapper.toEntity(cityDTO));
        return cityMapper.toDto(city);
    }

    /**
     * Get all the cities.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cities");
        return cityRepository.findAll(pageable)
            .map(cityMapper::toDto);
    }

    /**
     * Get all the cities.
     *
     * @param stateId to filter by state.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CityDTO> getCitiesByStateId(Long stateId) {
        log.debug("Request to get all Cities");
        return cityMapper.toDto(cityRepository.findCitiesByStateId(stateId));
    }

    /**
     * Get one city by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CityDTO> findOne(Long id) {
        log.debug("Request to get City : {}", id);
        Optional<City> city = cityRepository.findById(id);
        validatePresence(city);
        return city.map(cityMapper::toDto);
    }

    /**
     * Delete the city by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete City : {}", id);
        Optional<City> city = cityRepository.findById(id);
        validatePresence(city);
        cityRepository.deleteById(id);
    }

    @Override
    public void validateCreation(CityDTO cityDTO) {
        if (cityDTO.getId() != null) {
            ThrowUtils.badRequest("City cannot have an ID", ENTITY_NAME, "idexists");
        }
        validateFields(cityDTO);
    }

    @Override
    public void validateUpdate(CityDTO cityDTO) {
        if (cityDTO.getId() == null) {
            ThrowUtils.badRequest("City cannot have ID null", ENTITY_NAME, "idnull");
        }
        validateFields(cityDTO);
        Optional<City> city = cityRepository.findById(cityDTO.getId());
        validatePresence(city);
    }

    private void validateFields(CityDTO cityDTO) {
        if (cityDTO.getName().isEmpty()) {
            ThrowUtils.badRequest("City must is null", ENTITY_NAME, "cityNameNotNull");
        }
        if (cityRepository.existsWithNameAndStateId(cityDTO.getName().trim().toLowerCase(), cityDTO.getStateId(), cityDTO.getId())) {
            ThrowUtils.badRequest("City name already exists", ENTITY_NAME, "cityNameExists");
        }
    }

    private void validatePresence(Optional<City> city) {
        if (city.isPresent() == false) {
            ThrowUtils.gone("City not found", ENTITY_NAME, "cityNotFound");
        }
    }
}
