package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class AddFundsCommand implements CommandStrategy, Search {
    String accountIBAN;
    double amount;
    int timestamp;

    public AddFundsCommand(String accountIBAN, double amount, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Account account = findAccountByIBAN(accountIBAN);
        if (account == null) {
            System.err.println("Account not found: " + accountIBAN);
            return;
        }

        double oldBalance = account.getBalance();
        account.setBalance(oldBalance + amount);
    }
}
