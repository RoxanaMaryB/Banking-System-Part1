package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.*;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class SendMoneyCommand implements CommandStrategy, Search {
    String senderIBAN;
    String receiverIBAN;
    double amount;
    String description;
    String email;
    int timestamp;

    public SendMoneyCommand(String senderIBAN, String receiverIBAN, double amount, String description, String email, int timestamp) {
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.description = description;
        this.email = email;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Account sender = findAccountByIBAN(senderIBAN);
        Account receiver = findAccountByIBAN(receiverIBAN);

        if (sender == null) {
            System.err.println("Sender account not found: " + senderIBAN);
            return;
        }
        if (receiver == null) {
            System.err.println("Receiver account not found: " + receiverIBAN);
            return;
        }
        if (sender.getBalance() < amount) {
            sender.getUser().logTransaction(Transaction.builder()
                    .description("Insufficient funds")
                    .email(email)
                    .timestamp(timestamp)
                    .build());
            return;
        }

        sender.setBalance(sender.getBalance() - amount);
        CurrencyConverter converter = new CurrencyConverter(Bank.getInstance().getExchangeRates());
        double amountInReceiverCurrency = converter.convertCurrency(amount, sender.getCurrency(), receiver.getCurrency());
        receiver.setBalance(receiver.getBalance() + amountInReceiverCurrency);
        sender.updateCardStatus();

        String amountWithCurrencySender = String.format("%.1f %s", amount, sender.getCurrency());
        String amountWithCurrencyReceiver = String.format("%.1f %s", amountInReceiverCurrency, receiver.getCurrency());

        sender.getUser().logTransaction(Transaction.builder()
                .description(description)
                .email(email)
                .senderIBAN(senderIBAN)
                .receiverIBAN(receiverIBAN)
                .amount(amountWithCurrencySender)
                .transferType("sent")
                .timestamp(timestamp)
                .build());


        receiver.getUser().logTransaction(Transaction.builder()
                .description(description)
                .email(receiver.getUser().getEmail())
                .senderIBAN(senderIBAN)
                .receiverIBAN(receiverIBAN)
                .amount(amountWithCurrencyReceiver)
                .transferType("received")
                .timestamp(timestamp)
                .build());
    }
}
