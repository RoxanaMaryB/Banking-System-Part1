package org.poo.bank;

import lombok.Data;

import static org.poo.utils.Utils.generateCardNumber;

@Data
public class Card {
    String cardNumber;
    String status;

    public Card() {
        this.cardNumber = generateCardNumber();
        this.status = "active";
    }
}
