package br.com.softplan.person.repository;

import br.com.softplan.person.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the City entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT CASE WHEN COUNT(city) > 0 THEN 'true' ELSE 'false' END "
        + "FROM City city "
        + "WHERE city.name = LOWER(?1) "
        + "AND city.state.id = ?2 "
        + "AND (?3 IS NULL OR city.id != ?3)")
    boolean existsWithNameAndStateId(String name, Long stateId, Long cityId);

    @Query("SELECT city "
        + "FROM City city "
        + "WHERE ?1 IS NULL OR city.state.id = ?1")
    List<City> findCitiesByStateId(Long stateId);

}
