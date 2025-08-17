package br.com.core.ohmybills.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_invoice")
public class Invoice extends AbstractEntity {

    @Column(name = "name", length = 100)
    private String name;

    @JoinColumn(name = "credit_card", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CreditCard creditCard;

    @OneToMany(mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<Expense> expenses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

}
