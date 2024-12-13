package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class PayOnlineCommand implements CommandStrategy, Search {
    String cardNumber;
    double amount;
    String currency;
    int timestamp;
    String description;
    String commerciant;
    String email;

    public PayOnlineCommand(String cardNumber, double amount, String currency, String description, String commerciant,
                            String email, int timestamp) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        User correctUser = findUserByEmail(email);
        if (correctUser == null) {
            System.err.println("User not found: " + email);
            return;
        }

        // find cardNumber in correctUser's accounts
        boolean found = false;
        Account correctAccount = null;
        for (Account account : correctUser.getAccounts()) {
            // find cardNumber in account's cards
            for (int i = 0; i < account.getCards().size(); i++) {
                if (account.getCards().get(i).getCardNumber().equals(cardNumber)) {
                    found = true;
                    correctAccount = account;
                    break;
                }
            }
        }

        if (!found) { // add error transaction to correctUser's transactions
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "payOnline");

            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("description", "Card not found");
            outputNode.put("timestamp", timestamp);
            commandOutput.set("output", outputNode);

            commandOutput.put("timestamp", timestamp);
            output.add(commandOutput);
            return;
        }

        CurrencyConverter converter = new CurrencyConverter(Bank.getInstance().getExchangeRates());

        //check if the currency is the same as the account's currency
        if (!correctAccount.getCurrency().equals(currency)) {
            double convertedAmount = converter.convertCurrency(amount, currency, correctAccount.getCurrency());
            if (correctAccount.getBalance() - convertedAmount >= 0) {
                correctAccount.setBalance(correctAccount.getBalance() - convertedAmount);
            } else {
                //TODO: add error transaction to correctUser's transactions
            }
        } else {
            if (correctAccount.getBalance() - amount >= 0) {
                correctAccount.setBalance(correctAccount.getBalance() - amount);
            } else {
                //TODO: add error transaction to correctUser's transactions
            }
        }

        correctAccount.updateCardStatus();
    }
}
