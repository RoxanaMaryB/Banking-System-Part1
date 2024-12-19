package org.poo.bank;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class Transaction {
    private String card;
    private String cardHolder;
    private String accountIBAN;
    private String senderIBAN;
    private String receiverIBAN;
    private String amount;
    private double amountDouble;
    private String transferType;
    private String description;
    private String commerciant;
    private String email;
    private String currency;
    private List<String> involvedAccounts;
    private int timestamp;
    private String silentIBAN;
    private String error;

    /**
     * Add a string field to the JSON object if the value is not null
     * @param node
     * @param fieldName
     * @param value
     */
    public void addField(final ObjectNode node, final String fieldName, final String value) {
        if (value != null) {
            node.put(fieldName, value);
        }
    }

    // who added this is evil
    /**
     * Add a double field to the JSON object if the value is not null
     * @param node
     * @param fieldName
     * @param value
     */
    public void addDoubleField(final ObjectNode node, final String fieldName, final double value) {
        if (value != 0) {
            node.put(fieldName, value);
        }
    }

    /**
     * Add a string list field to the JSON object if the value is not null
     * @param node
     * @param fieldName
     * @param values
     */
    public void addListOfStrings(final ObjectNode node, final String fieldName,
                                 final List<String> values) {
        if (values != null) {
            ArrayNode arrayNode = node.putArray(fieldName);
            for (String value : values) {
                arrayNode.add(value);
            }
        }
    }
}
