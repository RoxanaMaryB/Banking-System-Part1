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

    public void addField(ObjectNode node, String fieldName, String value) {
        if (value != null) {
            node.put(fieldName, value);
        }
    }

    // who added this is evil
    public void addDoubleField(ObjectNode node, String fieldName, double value) {
        if(value != 0) {
            node.put(fieldName, value);
        }
    }

    public void addListOfStrings(ObjectNode node, String fieldName, List<String> values) {
        if (values != null) {
            ArrayNode arrayNode = node.putArray(fieldName);
            for (String value : values) {
                arrayNode.add(value);
            }
        }
    }
}
