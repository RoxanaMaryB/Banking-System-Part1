package org.poo.bank;

import lombok.Getter;
import lombok.Setter;

import static org.poo.utils.Utils.generateCardNumber;

@Getter @Setter
public class Card {
    String cardNumber;
    String status;
    Account account;

    public Card(Account account) {
        this.cardNumber = generateCardNumber();
        this.account = account;
        this.status = "active";
    }

    public void deleteCard() {
        account.getCards().remove(this);
    }

    public void changeIfOneTime() {
    }
}
