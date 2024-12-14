package org.poo.bank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String currency, User user, int timestamp) {
        super(currency, "savings", user, timestamp);
        this.setType("savings");
    }
}
