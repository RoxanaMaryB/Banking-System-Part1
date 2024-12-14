package org.poo.bank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static org.poo.utils.Utils.generateCardNumber;

@Getter @Setter
public class OneTimeCard extends Card {

    public OneTimeCard(Account account) {
        super(account);
    }

    public void changeIfOneTime() {
        this.setCardNumber(generateCardNumber());

    }
}
