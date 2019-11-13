package br.com.softplan.person.service.impl;

import br.com.softplan.person.domain.Address;
import br.com.softplan.person.domain.User;
import br.com.softplan.person.repository.AddressRepository;
import br.com.softplan.person.service.AddressService;
import br.com.softplan.person.service.UserService;
import br.com.softplan.person.service.dto.AddressDTO;
import br.com.softplan.person.service.mapper.AddressMapper;
import br.com.softplan.person.service.util.StringUtils;
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
public class AddressServiceImpl implements AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private static final String ENTITY_NAME = "address";

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private UserService userService;

    /**
     * Save a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AddressDTO save(AddressDTO addressDTO) {
        log.debug("Request to save Address : {}", addressDTO);
        validate(addressDTO);
        User user = userService.getCurrentUser();
        addressDTO.setUserId(user.getId());
        Address address = addressRepository.save(addressMapper.toEntity(addressDTO));
        return addressMapper.toDto(address);
    }

    /**
     *  validate address.
     * @param addressDTO
     */
    public void validate(AddressDTO addressDTO) {
        if(addressDTO.getId() == null) {
            validateCreation(addressDTO);
        } else {
            validateUpdate(addressDTO);
        }
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
     * Get all the addresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll(pageable)
            .map(addressMapper::toDto);
    }

    /**
     * Get one address by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AddressDTO> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        Optional<Address> address = addressRepository.findById(id);
        validatePresence(address);
        return address.map(addressMapper::toDto);
    }

    /**
     * Delete the address by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        Optional<Address> address = addressRepository.findById(id);
        validatePresence(address);
        addressRepository.deleteById(id);
    }

    @Override
    public void validateCreation(AddressDTO addressDTO) {
        if (addressDTO.getId() != null) {
            ThrowUtils.badRequest("Address cannot have an ID", ENTITY_NAME, "idexists");
        }
        validateFields(addressDTO, true);
    }

    @Override
    public void validateUpdate(AddressDTO addressDTO) {
        if (addressDTO.getId() == null) {
            ThrowUtils.badRequest("Address cannot have ID null", ENTITY_NAME, "idnull");
        }
        validateFields(addressDTO, false);
        Optional<Address> address = addressRepository.findById(addressDTO.getId());
        if (address.isPresent()) {
            ThrowUtils.badRequest("Address found", ENTITY_NAME, "addressFound");
        }
    }

    @Override
    public List<AddressDTO> findAddressesByPersonId(Long personId) {
        return addressMapper.toDto(addressRepository.findAddressesByPersonId(personId));
    }

    @Override
    public void removeAll(List<Address> addresses) {
        addressRepository.deleteAll(addresses);
    }

    /**
     *
     * @param addressDTO
     * @param isCreate
     */
    private void validateFields(AddressDTO addressDTO, boolean isCreate) {
        if (addressDTO.getStreetAddress().isEmpty()) {
            ThrowUtils.badRequest("Address must is null", ENTITY_NAME, "addressNameNotNull");
        }
    }

    /**
     *
     * @param address
     */
    private void validatePresence(Optional<Address> address) {
        if (!address.isPresent()) {
            ThrowUtils.gone("Address not found", ENTITY_NAME, "addressNotFound");
        }
    }
}
