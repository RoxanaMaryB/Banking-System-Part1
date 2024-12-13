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
    int timestamp;

    public SendMoneyCommand(String senderIBAN, String receiverIBAN, double amount, String description, int timestamp) {
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.description = description;
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
            System.err.println("Insufficient funds in sender account: " + senderIBAN);
            // add error transaction to sender's transactions
            return;
        }

        sender.setBalance(sender.getBalance() - amount);
        CurrencyConverter converter = new CurrencyConverter(Bank.getInstance().getExchangeRates());
        double amountInReceiverCurrency = converter.convertCurrency(amount, sender.getCurrency(), receiver.getCurrency());
        receiver.setBalance(receiver.getBalance() + amountInReceiverCurrency);

        sender.updateCardStatus();
    }
}
