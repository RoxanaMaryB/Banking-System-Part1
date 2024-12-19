package org.poo.bank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;

import static org.poo.utils.Utils.generateCardNumber;

@Getter @Setter
public class Card {
    private String cardNumber;
    private String status;
    private Account account;

    public Card(final Account account) {
        this.cardNumber = generateCardNumber();
        this.account = account;
        this.status = "active";
    }

    /**
     * This method deletes the card from the account's list of cards.
     */
    public void deleteCard() {
        account.getCards().remove(this);
    }

    /**
     * This method deletes the card and creates a new card in case the card is one-time.
     * @param timestamp
     */
    public void changeIfOneTime(final int timestamp) {
    }

    /**
     * This method prints to the output if the card is not found.
     * @param output
     * @param objectMapper
     * @param commandOutput
     * @param timestamp
     */
    public static void cardNotFound(final ArrayNode output, final ObjectMapper objectMapper,
                                    final ObjectNode commandOutput, final int timestamp) {
        ObjectNode outputNode = objectMapper.createObjectNode();
        outputNode.put("description", "Card not found");
        outputNode.put("timestamp", timestamp);
        commandOutput.set("output", outputNode);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }
}
