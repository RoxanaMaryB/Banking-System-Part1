package org.poo.bank;

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
}
