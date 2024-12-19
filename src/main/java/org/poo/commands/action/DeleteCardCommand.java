package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.bank.Transaction;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class DeleteCardCommand implements CommandStrategy, Search {
    private String cardNumber;
    private int timestamp;

    public DeleteCardCommand(final String cardNumber, final int timestamp) {
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
    }

    /**
     * Get all users in the bank, used for search interface
     * @return List of users
     */
    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    /**
     * Implementation of strategy pattern execute method
     * @param output
     * @param objectMapper
     */
    @Override
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        Card card = findCardByNumber(cardNumber);
        if (card != null) {
            Account account = card.getAccount();
            User user = account.getUser();
            card.deleteCard();

            if (user == null) {
                return;
            }

            String email = user.getEmail();
            user.logTransaction(Transaction.builder()
                    .accountIBAN(account.getIban())
                    .card(cardNumber)
                    .cardHolder(card.getAccount().getUser().getEmail())
                    .description("The card has been destroyed")
                    .email(email)
                    .timestamp(timestamp)
                    .silentIBAN(account.getIban())
                    .build());
        }
    }
}
