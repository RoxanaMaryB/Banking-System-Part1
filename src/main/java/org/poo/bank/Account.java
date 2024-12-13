package org.poo.bank;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

import static org.poo.utils.Utils.generateIBAN;

@Data
public class Account {
    double balance;
    List<Card> cards;
    String currency;
    String IBAN;
    String type;
    int timestampCreated;
    double minBalance = Double.NEGATIVE_INFINITY;

    public Account(String currency, String type, int timestamp) {
        this.balance = 0;
        this.cards = new ArrayList<>();
        this.currency = currency;
        this.type = type;
        this.IBAN = generateIBAN();
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
}
