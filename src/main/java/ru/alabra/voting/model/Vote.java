package ru.alabra.voting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 28.08.2019
 */

@Entity
@Table(name = "vote")
public class Vote extends AbstractBaseEntity {

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER)
    private Menu menu;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public Vote() { }

    public Vote(Integer id, LocalDate date, Menu menu, User user) {
        super(id);
        this.date = date;
        this.menu = menu;
        this.user = user;
    }

    public Vote(Vote vote) {
        this(vote.getId(), vote.getDate(), vote.getMenu(), vote.getUser());
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "date=" + date +
                ", menu=" + menu +
                ", user=" + user +
                ", id=" + id +
                '}';
    }
}
