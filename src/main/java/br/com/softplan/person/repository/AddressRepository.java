package br.com.softplan.person.repository;

import br.com.softplan.person.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     *
     * @param personId
     * @return
     */
    @Query("SELECT a "
        + "FROM Address a "
        + "WHERE a.person.id = ?1")
    List<Address> findAddressesByPersonId(Long personId);
}
