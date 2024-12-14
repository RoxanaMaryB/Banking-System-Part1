package org.poo.bank;

import lombok.Builder;
import lombok.Data;

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
    private int timestamp;
}
