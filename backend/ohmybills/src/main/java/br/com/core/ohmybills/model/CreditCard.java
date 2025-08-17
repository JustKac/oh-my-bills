package br.com.core.ohmybills.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_credit_card")
public class CreditCard extends AbstractEntity {

    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "best_shopping_day")
    private LocalDate bestShoppingDay;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "creditCard", fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getBestShoppingDay() {
        return bestShoppingDay;
    }

    public void setBestShoppingDay(LocalDate bestShoppingDay) {
        this.bestShoppingDay = bestShoppingDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

}
