package ru.alabra.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Vote u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT v FROM Vote v WHERE v.user.id=:user_id AND v.date=:date")
    List<Vote> findByUserAndByDate(
            @Param("user_id") int user_id,
            @Param("date") LocalDate date);

    List<Vote> findByDate(LocalDate date);

    List<Vote> findByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Vote> findById(int id);

    List<Vote> findAll();

}
