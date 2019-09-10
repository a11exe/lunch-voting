package ru.alabra.voting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "menu")
public class Menu extends AbstractBaseEntity {

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private Restaurant restaurant;

    public Menu() { }

    public Menu(Integer id, LocalDate date, String description, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.description = description;
        this.restaurant = restaurant;
    }

    public Menu(Menu m) {
        this(m.getId(), m.getDate(), m.getDescription(), m.getRestaurant());
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
