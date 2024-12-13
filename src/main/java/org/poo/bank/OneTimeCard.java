package org.poo.bank;

import lombok.Data;

@Data
public class OneTimeCard extends Card {
    boolean used;

    public OneTimeCard() {
        super();
        this.used = false;
    }
}
