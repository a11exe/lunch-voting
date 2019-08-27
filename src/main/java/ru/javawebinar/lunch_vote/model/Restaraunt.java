package ru.javawebinar.lunch_vote.model;

import javax.persistence.*;

@Entity
@Table(name = "restaurants")
public class Restaraunt extends AbstractNamedEntity {

    @Column(name = "description", nullable = false)
    private String description;

    public Restaraunt() {}

    public Restaraunt(Integer id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public Restaraunt(Restaraunt r) {
        this(r.getId(), r.getName(), r.getDescription());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Restaraunt{" +
                "description='" + description + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
