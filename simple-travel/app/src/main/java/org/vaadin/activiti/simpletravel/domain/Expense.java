package org.vaadin.activiti.simpletravel.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Expense extends AbstractValueObject {

    @NotNull(message = "Please enter a description for your expense")
    protected String description;
    @NotNull(message = "Please enter a quantity")
    protected Integer quantity = 1;
    @NotNull(message = "Please enter a price")
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotal() {
        return price.multiply(new BigDecimal(getQuantity()));
    }
}
