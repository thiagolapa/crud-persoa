package br.com.softplan.person.web.rest;

import br.com.softplan.person.service.StateService;
import br.com.softplan.person.service.dto.StateDTO;
import br.com.softplan.person.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.com.softplan.person.domain.State}.
 */
@RestController
@RequestMapping("/api")
public class StateResource {

    private final Logger log = LoggerFactory.getLogger(StateResource.class);

    private static final String ENTITY_NAME = "state";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private StateService stateService;

    public StateResource() {

    }

    /**
     * {@code POST  /states} : Create a new state.
     *
     * @param stateDTO the stateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stateDTO, or with status {@code 400 (Bad Request)} if the state has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/states")
    public ResponseEntity<StateDTO> createState(@Valid @RequestBody StateDTO stateDTO) throws URISyntaxException {
        log.debug("REST request to save State : {}", stateDTO);
        stateService.validateCreation(stateDTO);
        StateDTO result = stateService.save(stateDTO);
        return ResponseEntity.created(new URI("/api/states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /states} : Updates an existing state.
     *
     * @param stateDTO the stateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateDTO,
     * or with status {@code 400 (Bad Request)} if the stateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/states")
    public ResponseEntity<StateDTO> updateState(@Valid @RequestBody StateDTO stateDTO) throws URISyntaxException {
        log.debug("REST request to update State : {}", stateDTO);
        stateService.validateUpdate(stateDTO);
        StateDTO result = stateService.save(stateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /states} : get all the states.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of states in body.
     */
    @GetMapping("/states")
    public ResponseEntity<List<StateDTO>> getAllStates(Pageable pageable) {
        log.debug("REST request to get a page of States");
        Page<StateDTO> page = stateService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /states/:id} : get the "id" state.
     *
     * @param id the id of the stateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/states/{id}")
    public ResponseEntity<StateDTO> getState(@PathVariable Long id) {
        log.debug("REST request to get State : {}", id);
        Optional<StateDTO> stateDTO = stateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stateDTO);
    }

    /**
     * {@code DELETE  /states/:id} : delete the "id" state.
     *
     * @param id the id of the stateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/states/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable Long id) {
        log.debug("REST request to delete State : {}", id);
        try {
            stateService.delete(id);
            return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
        } catch (Exception e) {
            throw new BadRequestAlertException("State cannot be deleted", ENTITY_NAME, "stateCannotBeDeleted");
        }
    }

    /**
     * {@code GET  /cities} : get all the cities by countryId.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cities in body.
     */
    @GetMapping("/states/country/{countryId}")
    public ResponseEntity<List<StateDTO>> getCitiesByStateId(@PathVariable Long countryId) {
        log.debug("REST request to get a page of States by countryId");
        List<StateDTO> sectorDTOS = stateService.getStatesByCountryId(countryId);
        return new ResponseEntity<>(sectorDTOS, HttpStatus.OK);
    }
}
