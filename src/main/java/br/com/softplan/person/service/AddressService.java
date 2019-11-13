package br.com.softplan.person.service;

import br.com.softplan.person.domain.Address;
import br.com.softplan.person.service.dto.AddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface AddressService {

    /**
     * Save a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    AddressDTO save(AddressDTO addressDTO);

    /**
     * Get all the addresses.
     * @param pageable
     * @return the list of entities.
     */
    Page<AddressDTO> findAll(Pageable pageable);

    /**
     * Get the "id" address.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AddressDTO> findOne(Long id);

    /**
     * Delete the "id" address.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * @param addressDTO
     */
    void validateCreation(AddressDTO addressDTO);

    /**
     * @param addressDTO
     */
    void validateUpdate(AddressDTO addressDTO);

    /**
     * find addresses by person id.
     * @param personId
     * @return
     */
    List<AddressDTO> findAddressesByPersonId(Long personId);

    /**
     * remove all addres by addresses.
     * @param addresses
     */
    void removeAll(List<Address> addresses);

}
