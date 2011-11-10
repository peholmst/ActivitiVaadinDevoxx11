package org.vaadin.activiti.simpletravel.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class Expense extends AbstractValueObject {

    @NotNull(message = "Please enter a description for your expense")
    protected String description;
    @Min(value = 1, message = "Please enter a quantity")
    protected int quantity = 1;
    @Min(value = 0, message = "Please enter a price")
    @Column(precision = 8, scale = 2)
    protected BigDecimal price = BigDecimal.ZERO;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return price.multiply(new BigDecimal(getQuantity()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Expense other = (Expense) obj;
        if ((this.description == null) ? (other.description != null) : !this.description.equals(other.description)) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        if (this.price != other.price && (this.price == null || !this.price.equals(other.price))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 29 * hash + this.quantity;
        hash = 29 * hash + (this.price != null ? this.price.hashCode() : 0);
        return hash;
    }
}
