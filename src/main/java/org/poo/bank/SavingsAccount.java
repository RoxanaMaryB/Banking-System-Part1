package org.poo.bank;

import lombok.Data;

@Data
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String currency, int timestamp) {
        super(currency, "savings", timestamp);
        this.setType("savings");
    }
}
