package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class checkCardStatus implements CommandStrategy, Search {
    String cardNumber;
    int timestamp;

    public checkCardStatus(String cardNumber, int timestamp) {
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Card card = findCardByNumber(cardNumber);

        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "checkCardStatus");

        ObjectNode outputNode = objectMapper.createObjectNode();
        if (card == null) {
            outputNode.put("description", "Card not found");
        } else {
            outputNode.put("description", card.getStatus());
        }
        outputNode.put("timestamp", timestamp);
        commandOutput.set("output", outputNode);

        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);

    }
}
