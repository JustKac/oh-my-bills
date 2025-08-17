package br.com.core.ohmybills.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "tb_expense")
public class Expense extends AbstractEntity{

    @Column(length = 100)
    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column
    private LocalDate date;

    @Column
    private Boolean isRecurring;

    @Column
    private Boolean isAchived;

    @Column
    private Integer installments;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Invoice invoice;

    @JoinColumn(name = "person_tag_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PersonTag personTag;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public Boolean getIsAchived() {
        return isAchived;
    }

    public void setIsAchived(Boolean isAchived) {
        this.isAchived = isAchived;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public PersonTag getPersonTag() {
        return personTag;
    }

    public void setPersonTag(PersonTag personTag) {
        this.personTag = personTag;
    }

}
