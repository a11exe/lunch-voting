package ru.alabra.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.alabra.voting.model.Menu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMenuRepository extends JpaRepository<Menu, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Menu u WHERE u.id=:id")
    int delete(@Param("id") int id);

    List<Menu> findByRestaurantId(int restaurant_id);

    Optional<Menu> findByRestaurantIdAndId(int restaurant_id, int id);

    List<Menu> findByDate(LocalDate date);

    List<Menu> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Menu> findByRestaurantIdAndDate(int restaurant_id, LocalDate date);

    List<Menu> findByRestaurantIdAndDateBetween(int restaurant_id, LocalDate startDate, LocalDate endDate);
}
