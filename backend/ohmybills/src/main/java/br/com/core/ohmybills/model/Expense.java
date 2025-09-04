package br.com.core.ohmybills.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

@Entity(name = "tb_expense")
public class Expense extends AbstractEntity{

    @Column(length = 100, nullable = false)
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate firstPayDate;

    @Column(nullable = false)
    private Boolean isRecurring;

    @Column(nullable = false)
    private Boolean isArchived;

    @Column(nullable = false)
    private Integer installments;

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Invoice invoice;

    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private CreditCard creditCard;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_expense_tag",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    public String getDescription() {
        return description;
    }

    public Expense setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Expense setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public LocalDate getFirstPayDate() {
        return firstPayDate;
    }

    public Expense setFirstPayDate(LocalDate firstPayDate) {
        this.firstPayDate = firstPayDate;
        return this;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public Expense setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
        return this;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public Expense setIsArchived(Boolean isAchived) {
        this.isArchived = isAchived;
        return this;
    }

    public Integer getInstallments() {
        return installments;
    }

    public Expense setInstallments(Integer installments) {
        this.installments = installments;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Expense setUser(User user) {
        this.user = user;
        return this;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public Expense setInvoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public Expense setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public Expense setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

}
