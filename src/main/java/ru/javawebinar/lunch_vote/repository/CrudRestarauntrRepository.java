package ru.javawebinar.lunch_vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.lunch_vote.model.Restaraunt;

@Transactional(readOnly = true)
public interface CrudRestarauntrRepository extends JpaRepository<Restaraunt, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Restaraunt u WHERE u.id=:id")
    int delete(@Param("id") int id);
}
