package br.com.core.ohmybills.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_person_tag")
public class PersonTag extends AbstractEntity {

    @Column(name = "name", length = 100)
    private String name;

    @OneToMany(mappedBy = "personTag", fetch = FetchType.LAZY)
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
