package br.com.softplan.person.service.impl;

import br.com.softplan.person.domain.Country;
import br.com.softplan.person.domain.User;
import br.com.softplan.person.repository.CountryRepository;
import br.com.softplan.person.service.CountryService;
import br.com.softplan.person.service.UserService;
import br.com.softplan.person.service.dto.CountryDTO;
import br.com.softplan.person.service.mapper.CountryMapper;
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
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private static final String ENTITY_NAME = "country";

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private UserService userService;

    public CountryServiceImpl() {
    }

    /**
     * save a country.
     * @param countryDTO the entity to save.
     * @return
     */
    @Override
    public CountryDTO save(CountryDTO countryDTO) {
        log.debug("Request to save Country : {}", countryDTO);
        User user = userService.getCurrentUser();
        countryDTO.setUserId(user.getId());
        Country country = countryRepository.save(countryMapper.toEntity(countryDTO));
        return countryMapper.toDto(country);
    }

    /**
     * Get all the countries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CountryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Countries");
        return countryRepository.findAll(pageable)
            .map(countryMapper::toDto);
    }

    /**
     * Get all the countries.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CountryDTO> findAll() {
        log.debug("Request to get all Countries");
        return countryMapper.toDto(countryRepository.findAll());
    }

    /**
     * Get one country by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CountryDTO> findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id)
            .map(countryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete City : {}", id);
        Optional<Country> country = countryRepository.findById(id);
        validatePresence(country);
        countryRepository.deleteById(id);
    }

    private void validatePresence(Optional<Country> country) {
        if (country.isPresent() == false) {
            ThrowUtils.gone("City not found", ENTITY_NAME, "cityNotFound");
        }
    }
}
