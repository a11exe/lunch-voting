package ru.alabra.voting.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "menus")
public class Menu extends AbstractBaseEntity {

    @Column(name = "date", columnDefinition = "timestamp default now()")
    @NotNull
    private Date date;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Restaraunt restaraunt;

    public Menu() {}

    public Menu(Integer id, Date date, String description, Restaraunt restaraunt) {
        super(id);
        this.date = date;
        this.description = description;
        this.restaraunt = restaraunt;
    }

    public Menu(Menu m) {
        this(m.getId(), m.getDate(), m.getDescription(), m.getRestaraunt());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Restaraunt getRestaraunt() {
        return restaraunt;
    }

    public void setRestaraunt(Restaraunt restaraunt) {
        this.restaraunt = restaraunt;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", restaraunt=" + restaraunt +
                ", id=" + id +
                '}';
    }
}
