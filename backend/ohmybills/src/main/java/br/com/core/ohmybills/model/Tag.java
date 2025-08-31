package br.com.core.ohmybills.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_tag")
public class Tag extends AbstractEntity {

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "is_person", nullable = false)
    private Boolean isPerson;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<Expense> expenses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

}
