package org.poo.bank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OneTimeCard extends Card {
    boolean used;

    public OneTimeCard(Account account) {
        super(account);
        this.used = false;
    }
}
