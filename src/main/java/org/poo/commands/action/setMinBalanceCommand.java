package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class setMinBalanceCommand implements CommandStrategy, Search {
    String accountIBAN;
    double minBalance;
    int timestamp;

    public setMinBalanceCommand(String accountIBAN, double minBalance, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.minBalance = minBalance;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Account account = findAccountByIBAN(accountIBAN);
        if (account != null) {
            account.setMinBalance(minBalance);
        }
    }
}
