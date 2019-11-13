package br.com.softplan.person.repository;

import br.com.softplan.person.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;


/**
 * Spring Data  repository for the Person entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    /**
     * @param query
     * @param pageable
     * @return
     */
    @Query("SELECT person FROM Person person "
        + "WHERE (?1 IS NULL OR UPPER(person.name) LIKE %?1% "
        + "OR UPPER(person.cpf) LIKE %?1% "
        + "OR UPPER(person.email) LIKE %?1% "
        + "OR UPPER(person.naturalness) LIKE %?1% "
        + "OR UPPER(person.nationality) LIKE %?1%) "
        + "AND (?2 IS NULL OR person.sex = ?2) "
        + "AND (cast(person.dateOfBirth as date) between cast(?3 as date) and cast(?4 as date) "
        + "OR ?4 is null and cast(person.dateOfBirth as date) >= cast(?3 as date) "
        + "OR ?3 is null and cast(person.dateOfBirth as date) <= cast(?4 as date) "
        + "OR (?3 is null and ?4 is null)) " )
    Page<Person> findAll(String query, String sex, ZonedDateTime fromDate, ZonedDateTime toDate, Pageable pageable);

    /**
     *
     * @param cpf
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(person) > 0 THEN 'true' ELSE 'false' END "
        + "FROM Person person "
        + "WHERE person.cpf = ?1 ")
    boolean existsCpf(String cpf);

    /**
     *
     * @param personId
     * @param cpf
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(person) > 0 THEN 'true' ELSE 'false' END "
        + "FROM Person person "
        + "WHERE person.id = ?1 "
        + "AND person.cpf != ?2 ")
    boolean existsCpf(Long personId, String cpf);

    /**
     *
     * @param name
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(person) > 0 THEN 'true' ELSE 'false' END "
        + "FROM Person person "
        + "WHERE person.name = ?1 ")
    boolean existsWithName(String name);

    /**
     *
     * @param name
     * @param personId
     * @return
     */
    @Query("SELECT CASE WHEN COUNT(person) > 0 THEN 'true' ELSE 'false' END "
        + "FROM Person person "
        + "WHERE person.name = ?1 "
        + "AND person.id != ?2 ")
    boolean existsWithName(String name, Long personId);
}
