package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    public void changeIfOneTime(int timestamp) {
    }

    public static void cardNotFound(ArrayNode output, ObjectMapper objectMapper,
                                    ObjectNode commandOutput, int timestamp) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", timestamp);
        commandOutput.set("output", outputNode);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }
}
