package br.com.softplan.person.repository;

import br.com.softplan.person.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the State entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    @Query("SELECT CASE WHEN COUNT(state) > 0 THEN 'true' ELSE 'false' END "
        + "FROM State state "
        + "WHERE state.name = LOWER(?1) "
        + "AND state.country.id = ?2 "
        + "AND (?3 IS NULL OR state.id != ?3)")
    boolean existsWithNameAndCountryId(String name, Long countryId, Long id);

    @Query("SELECT state "
        + "FROM State state "
        + "WHERE ?1 IS NULL OR state.country.id = ?1")
    List<State> findStatesByCountryId(Long countryId);

}
