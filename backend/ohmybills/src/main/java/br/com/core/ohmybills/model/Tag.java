package br.com.core.ohmybills.model;

import java.util.List;

import jakarta.persistence.*;

@Entity(name = "tb_tag")
public class Tag extends AbstractEntity {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "is_person", nullable = false)
    private Boolean isPerson;

    @Column(name = "color", length = 7)
    private String color;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Expense> expenses;

    public String getName() {
        return name;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean isPerson() {
        return isPerson;
    }

    public Tag setIsPerson(Boolean isPerson) {
        this.isPerson = isPerson;
        return this;
    }

    public String getColor() {
        return color;
    }

    public Tag setColor(String color) {
        this.color = color;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Tag setUser(User user) {
        this.user = user;
        return this;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }
}
