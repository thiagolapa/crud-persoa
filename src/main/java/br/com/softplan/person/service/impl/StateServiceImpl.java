package br.com.softplan.person.service.impl;

import br.com.softplan.person.domain.State;
import br.com.softplan.person.domain.User;
import br.com.softplan.person.repository.StateRepository;
import br.com.softplan.person.service.StateService;
import br.com.softplan.person.service.UserService;
import br.com.softplan.person.service.dto.StateDTO;
import br.com.softplan.person.service.mapper.StateMapper;
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
public class StateServiceImpl implements StateService {

    private final Logger log = LoggerFactory.getLogger(StateServiceImpl.class);

    private static final String ENTITY_NAME = "state";

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private StateMapper stateMapper;

    @Autowired
    private UserService userService;

    /**
     * Save a state.
     *
     * @param stateDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StateDTO save(StateDTO stateDTO) {
        log.debug("Request to save State : {}", stateDTO);
        User user = userService.getCurrentUser();
        stateDTO.setUserId(user.getId());
        State state = stateRepository.save(stateMapper.toEntity(stateDTO));
        return stateMapper.toDto(state);
    }

    /**
     * Get all the states.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<StateDTO> findAll(Pageable pageable) {
        log.debug("Request to get all States");
        return stateRepository.findAll(pageable)
            .map(stateMapper::toDto);
    }

    /**
     * Get all the states.
     *
     * @param countryId to filter by country.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StateDTO> getStatesByCountryId(Long countryId) {
        log.debug("Request to get all States");
        return stateMapper.toDto(stateRepository.findStatesByCountryId(countryId));
    }

    /**
     * Get one state by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StateDTO> findOne(Long id) {
        log.debug("Request to get State : {}", id);
        Optional<State> state = stateRepository.findById(id);
        validatePresence(state);
        return state.map(stateMapper::toDto);
    }

    /**
     * Delete the state by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete State : {}", id);
        Optional<State> state = stateRepository.findById(id);
        validatePresence(state);
        stateRepository.deleteById(id);
    }

    @Override
    public void validateCreation(StateDTO stateDTO) {
        if (stateDTO.getId() != null) {
            ThrowUtils.badRequest("State cannot have an ID", ENTITY_NAME, "idexists");
        }
        validateFields(stateDTO);
    }

    @Override
    public void validateUpdate(StateDTO stateDTO) {
        if (stateDTO.getId() == null) {
            ThrowUtils.badRequest("State cannot have ID null", ENTITY_NAME, "idnull");
        }
        validateFields(stateDTO);
        Optional<State> state = stateRepository.findById(stateDTO.getId());
        validatePresence(state);
    }

    private void validateFields(StateDTO stateDTO) {
        if (stateDTO.getName().isEmpty()) {
            ThrowUtils.badRequest("State name is null", ENTITY_NAME, "stateNameNotNull");
        }
        if (stateRepository.existsWithNameAndCountryId(stateDTO.getName().trim().toLowerCase(), stateDTO.getCountryId(), stateDTO.getId())) {
            ThrowUtils.badRequest("State name already exists", ENTITY_NAME, "stateNameExists");
        }
    }

    private void validatePresence(Optional<State> state) {
        if (state.isPresent() == false) {
            ThrowUtils.gone("State not found", ENTITY_NAME, "stateNotFound");
        }
    }
}
