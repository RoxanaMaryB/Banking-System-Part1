package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;

public class AddAccountCommand implements CommandStrategy {
    String email;
    String currency;
    String accountType;
    int timestamp;

    public AddAccountCommand(String email, String currency, String accountType, int timestamp) {
        this.email = email;
        this.currency = currency;
        this.accountType = accountType;
        this.timestamp = timestamp;
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Bank bank = Bank.getInstance();
        // find email in users list
        User correctUser = null;
        for (User user : bank.getUsers()) {
            if (user.getEmail().equals(email)) {
                correctUser = user;
                break;
            }
        }
        if (correctUser == null) {
            System.err.println("User not found: " + email);
            return;
        }

        // add account to user
        Account newAccount = new Account(currency, accountType, timestamp);
        correctUser.getAccounts().add(newAccount);
    }
}
