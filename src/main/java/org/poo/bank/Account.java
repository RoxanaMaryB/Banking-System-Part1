package org.poo.bank;

import lombok.Setter;
import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

import static org.poo.utils.Utils.generateIBAN;

@Getter @Setter
public class Account {
    double balance;
    List<Card> cards;
    String currency;
    String IBAN;
    String type;
    int timestampCreated;
    double minBalance = Double.NEGATIVE_INFINITY;
    User user;

    public Account(String currency, String type, User user, int timestamp) {
        this.balance = 0;
        this.cards = new ArrayList<>();
        this.currency = currency;
        this.type = type;
        this.IBAN = generateIBAN();
        this.user = user;
        this.timestampCreated = timestamp;
    }

    public void updateCardStatus() {
        if (this.getBalance() <= this.getMinBalance()) {
            for (Card card : this.getCards()) {
                card.setStatus("frozen");
            }
        } else if (this.getBalance() - this.getMinBalance() <= 30) {
            for (Card card : this.getCards()) {
                card.setStatus("warning");
            }
        }
    }

    public void deleteAccount() {
        user.getAccounts().remove(this);
    }
}
