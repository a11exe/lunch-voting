package ru.alabra.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote u WHERE u.id=:id")
    void delete(@Param("id") int id);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.menu m JOIN FETCH m.restaurant WHERE v.user.id=:userId AND v.date=:date")
    List<Vote> findByUserAndByDate(
            @Param("userId") int userId,
            @Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.menu m JOIN FETCH m.restaurant WHERE v.date=:date")
    List<Vote> findByDate(@Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.menu m JOIN FETCH m.restaurant WHERE v.date BETWEEN :startDate AND :endDate")
    List<Vote> findByDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.menu m JOIN FETCH m.restaurant WHERE v.id=:id")
    Optional<Vote> findById(@Param("id") int id);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user JOIN FETCH v.menu m JOIN FETCH m.restaurant")
    List<Vote> findAll();

}
