package ru.alabra.voting.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "menu")
public class Menu extends AbstractBaseEntity {

    @Column(name = "date", columnDefinition = "timestamp default now()")
    private LocalDateTime date;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private Restaurant restaurant;

    public Menu() {}

    public Menu(Integer id, LocalDateTime date, String description, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.description = description;
        this.restaurant = restaurant;
    }

    public Menu(Menu m) {
        this(m.getId(), m.getDate(), m.getDescription(), m.getRestaurant());
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", restaurant=" + restaurant +
                ", id=" + id +
                '}';
    }
}
