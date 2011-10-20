package org.vaadin.activiti.simpletravel.domain;

import com.github.peholmst.stuff4vaadin.clone.CloneThis;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
public class TravelInvoice extends AbstractEntity {

    @OneToOne(optional = false)
    protected TravelRequest request;
    @ElementCollection
    @Valid
    @CloneThis(deepClone = true)
    protected Set<Expense> expenses = new HashSet<Expense>();
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @CloneThis
    protected TravelInvoiceDecision decision;
    protected boolean paid = false;

    protected TravelInvoice() {
    }

    public TravelInvoice(TravelRequest request) {
        this.request = request;
    }

    public TravelRequest getRequest() {
        return request;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isPaid() {
        return paid;
    }

    /**
     * Returns a clone of the travel invoice decision.
     */
    public TravelInvoiceDecision getDecision() {
        return decision != null ? (TravelInvoiceDecision) decision.clone() : null;
    }

    public void setDecision(TravelInvoiceDecision decision) {
        this.decision = decision != null ? (TravelInvoiceDecision) decision.clone() : null;
    }

    /**
     * Returns a set of clones of the <code>Expense</code> objects.
     * Any changes made to these objects will not be propagated to
     * the invoice. Use {@link #addExpence(org.vaadin.activiti.simpletravel.domain.Expence) } and
     * {@link #removeExpence(org.vaadin.activiti.simpletravel.domain.Expence) } to make changes
     * to the set of expenses.
     */
    public Set<Expense> getExpenses() {
        HashSet<Expense> expenseClones = new HashSet<Expense>();
        for (Expense expense : expenses) {
            expenseClones.add((Expense) expense.clone());
        }
        return expenseClones;
    }

    public void addExpense(Expense expenseToAdd) {
        expenses.add((Expense) expenseToAdd.clone());
    }

    public void removeExpense(Expense expenseToRemove) {
        expenses.remove(expenseToRemove);
    }

    public BigDecimal getTotalExpenses() {
        BigDecimal total = BigDecimal.ZERO;

        for (Expense expense : expenses) {
            total = total.add(expense.getTotal());
        }

        return total;
    }

    public boolean isDecisionPending() {
        return this.decision == null;
    }
}
