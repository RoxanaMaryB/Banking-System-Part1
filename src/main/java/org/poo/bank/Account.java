package org.poo.bank;

import lombok.Setter;
import lombok.Getter;

import java.util.List;
import java.util.ArrayList;

import static org.poo.utils.Utils.generateIBAN;

@Getter @Setter
public class Account {
    private static final double WARNING_THRESHOLD = 30.0;

    private double balance;
    private List<Card> cards;
    private String currency;
    private String iban;
    private String type;
    private int timestampCreated;
    private double minBalance = 0;
    private String alias;
    private User user;

    public Account(final String currency, final String type, final User user, final int timestamp) {
        this.balance = 0;
        this.cards = new ArrayList<>();
        this.currency = currency;
        this.type = type;
        this.iban = generateIBAN();
        this.user = user;
        this.timestampCreated = timestamp;
        this.alias = null;
    }

    /**
     * This method checks if the account balance is lower than the minimum balance or close
     * enough to it to send a warning to the user.
     * @param timestamp
     */
    public void updateCardStatus(final int timestamp) {
        if (this.getBalance() <= minBalance) {
            for (Card card : this.getCards()) {
                card.setStatus("frozen");
            }
            user.logTransaction(Transaction.builder()
                    .description("You have reached the minimum amount of funds, "
                            + "the card will be frozen")
                    .timestamp(timestamp)
                    .silentIBAN(iban)
                    .build());
        } else if (balance - minBalance <= WARNING_THRESHOLD) {
            for (Card card : this.getCards()) {
                card.setStatus("warning");
            }
            user.logTransaction(Transaction.builder()
                    .description("Warning! You almost reached the minimum amount of funds")
                    .timestamp(timestamp)
                    .silentIBAN(iban)
                    .build());
        }
    }

    /**
     * This method deletes the account from the user's account list.
     */
    public void deleteAccount() {
        user.getAccounts().remove(this);
    }

}
