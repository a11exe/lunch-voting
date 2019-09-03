package ru.alabra.voting.repository;

import ru.alabra.voting.model.Menu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MenuRepository {

    Menu save(Menu menu);

    // false if not found
    boolean delete(int id);

    List<Menu> findByRestaurantId(int restaurant_id);

    Optional<Menu> findByRestaurantIdAndId(int restaurant_id, int id);

    List<Menu> findByDate(LocalDate date);

    List<Menu> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Menu> findByRestaurantIdAndDate(int restaurant_id, LocalDate date);

    List<Menu> findByRestaurantIdAndDateBetween(int restaurant_id, LocalDate startDate, LocalDate endDate);

}
