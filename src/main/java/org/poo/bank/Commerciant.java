package org.poo.bank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Commerciant {
    private String name;
    private double amount;

    public Commerciant(final String name, final double amount) {
        this.name = name;
        this.amount = amount;
    }
}
