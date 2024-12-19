package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class CheckCardStatus implements CommandStrategy, Search {
    private String cardNumber;
    private int timestamp;

    public CheckCardStatus(final String cardNumber, final int timestamp) {
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
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        Card card = findCardByNumber(cardNumber);
        if (card == null) {
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "checkCardStatus");
            Card.cardNotFound(output, objectMapper, commandOutput, timestamp);
            return;
        }
        if (!card.getStatus().equals("frozen")) {
            card.getAccount().updateCardStatus(timestamp);
            String description;
            if (card.getStatus().equals("frozen")) {
                description = "You have reached the minimum amount of funds, "
                        + "the card will be frozen";
                card.getAccount().getUser().logTransaction(Transaction.builder()
                        .description(description)
                        .timestamp(timestamp)
                        .email(card.getAccount().getUser().getEmail())
                        .silentIBAN(card.getAccount().getIban())
                        .build());
            }
        }
    }
}
