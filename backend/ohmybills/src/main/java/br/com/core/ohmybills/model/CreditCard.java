package br.com.core.ohmybills.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

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

    @Column(name = "last_four_digits", length = 4)
    private String lastFourDigits;

    @Column(name = "brand")
    private String brand;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "creditCard", fetch = FetchType.LAZY)
    private List<Expense> expenses;

    @OneToMany(mappedBy = "creditCard", fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public CreditCard setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
        return this;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public CreditCard setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public LocalDate getBestShoppingDay() {
        return bestShoppingDay;
    }

    public CreditCard setBestShoppingDay(LocalDate bestShoppingDay) {
        this.bestShoppingDay = bestShoppingDay;
        return this;
    }

    public String getName() {
        return name;
    }

    public CreditCard setName(String name) {
        this.name = name;
        return this;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public CreditCard setLastFourDigits(String lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public CreditCard setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public User getUser() {
        return user;
    }

    public CreditCard setUser(User user) {
        this.user = user;
        return this;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public CreditCard setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        return this;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public CreditCard setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
        return this;
    }

}
