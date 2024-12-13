package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
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
        // add account to user
        Account newAccount = new Account(currency, accountType, timestamp);
        correctUser.getAccounts().add(newAccount);
    }
}
