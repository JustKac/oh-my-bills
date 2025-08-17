package br.com.core.ohmybills.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_user")
public class User extends AuditableEntity {

    @Column(name = "name", length = 150)
    private String name;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "keycloak_id", unique = true)
    private UUID keycloakId;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Income> incomes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Expense> expenses;
    
    // private String pictureUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(UUID keycloakId) {
        this.keycloakId = keycloakId;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(List<Income> incomes) {
        this.incomes = incomes;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    
}
