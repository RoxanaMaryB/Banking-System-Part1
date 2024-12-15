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
    double minBalance = 0;
    String alias;
    User user;

    public Account(String currency, String type, User user, int timestamp) {
        this.balance = 0;
        this.cards = new ArrayList<>();
        this.currency = currency;
        this.type = type;
        this.IBAN = generateIBAN();
        this.user = user;
        this.timestampCreated = timestamp;
        this.alias = null;
    }

    public void updateCardStatus(int timestamp) {
        if (this.getBalance() <= minBalance) {
            for (Card card : this.getCards()) {
                card.setStatus("frozen");
            }
            user.logTransaction(Transaction.builder()
                    .description("You have reached the minimum amount of funds, the card will be frozen")
                    .timestamp(timestamp)
                    .silentIBAN(IBAN)
                    .build());
        } else if (balance - minBalance <= 30) {
            for (Card card : this.getCards()) {
                card.setStatus("warning");
            }
            user.logTransaction(Transaction.builder()
                    .description("Warning! You almost reached the minimum amount of funds")
                    .timestamp(timestamp)
                    .silentIBAN(IBAN)
                    .build());
        }
    }

    public void deleteAccount() {
        user.getAccounts().remove(this);
    }

}
