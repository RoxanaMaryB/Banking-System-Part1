package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.*;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class AddAccountCommand implements CommandStrategy, Search {
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

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        User correctUser = findUserByEmail(email);
        if (correctUser == null) {
            System.err.println("User not found: " + email);
            return;
        }
        Account newAccount;
        if(accountType.equals("savings"))
            newAccount = new SavingsAccount(currency, correctUser, timestamp);
        else newAccount = new Account(currency, accountType, correctUser, timestamp);
        correctUser.getAccounts().add(newAccount);
        correctUser.logTransaction(Transaction.builder()
                .description("New account created")
                .email(email)
                .timestamp(timestamp)
                .build());
    }
}
