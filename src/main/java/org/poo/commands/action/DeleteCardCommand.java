package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class DeleteCardCommand implements CommandStrategy, Search {
    String cardNumber;
    int timestamp;

    public DeleteCardCommand(String cardNumber, int timestamp) {
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Card card = findCardByNumber(cardNumber);
        if (card != null) {
            Account account = card.getAccount();
            User user = account.getUser();
            card.deleteCard();

            if (user == null) {
                System.err.println("User not found for card: " + cardNumber);
                return;
            }

            String email = user.getEmail();
            user.logTransaction(Transaction.builder()
                    .accountIBAN(account.getIBAN())
                    .card(cardNumber)
                    .cardHolder(card.getAccount().getUser().getEmail())
                    .description("The card has been destroyed")
                    .email(email)
                    .timestamp(timestamp)
                    .build());
        } else {
            System.err.println("Card not found: " + cardNumber);
        }
    }
}
