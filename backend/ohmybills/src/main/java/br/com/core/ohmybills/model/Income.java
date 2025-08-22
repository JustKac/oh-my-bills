package br.com.core.ohmybills.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;

@Entity(name = "tb_income")
public class Income extends AbstractEntity{

    @Column(length = 100, nullable = false)
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate firstPayDate;

    @Column(nullable = false)
    private Boolean isRecurring;

    @Column(nullable = false)
    @Min(1)
    private Integer installments;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public String getDescription() {
        return description;
    }

    public Income setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Income setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public LocalDate getFirstPayDate() {
        return firstPayDate;
    }

    public Income setFirstPayDate(LocalDate firstPayDate) {
        this.firstPayDate = firstPayDate;
        return this;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public Income setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
        return this;
    }

    public Integer getInstallments() {
        return installments;
    }

    public Income setInstallments(Integer installments) {
        this.installments = installments;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Income setUser(User user) {
        this.user = user;
        return this;
    }

}
