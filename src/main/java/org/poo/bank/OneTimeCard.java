package org.poo.bank;

import lombok.Getter;
import lombok.Setter;

import static org.poo.utils.Utils.generateCardNumber;

@Getter @Setter
public class OneTimeCard extends Card {

    public OneTimeCard(final Account account) {
        super(account);
    }

    /**
     * Change the card number if the card is one time
     * @param timestamp
     */
    public void changeIfOneTime(final int timestamp) {
        // add transaction of card delete and create new card
        User user = this.getAccount().getUser();
        user.logTransaction(Transaction.builder()
                .accountIBAN(this.getAccount().getIban())
                .card(this.getCardNumber())
                .cardHolder(user.getEmail())
                .description("The card has been destroyed")
                .email(user.getEmail())
                .timestamp(timestamp)
                .build());

        this.setCardNumber(generateCardNumber());

        user.logTransaction(Transaction.builder()
                .description("New card created")
                .accountIBAN(this.getAccount().getIban())
                .card(this.getCardNumber())
                .cardHolder(user.getEmail())
                .email(user.getEmail())
                .timestamp(timestamp)
                .build());
    }
}
