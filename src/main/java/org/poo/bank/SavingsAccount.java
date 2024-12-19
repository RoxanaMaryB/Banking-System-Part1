package org.poo.bank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SavingsAccount extends Account {
    private double interestRate = 0;

    public SavingsAccount(final String currency, final User user, final int timestamp) {
        super(currency, "savings", user, timestamp);
        this.setType("savings");
    }
}
