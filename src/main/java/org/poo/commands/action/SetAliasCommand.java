package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class SetAliasCommand implements CommandStrategy, Search {
    String email;
    String accountIBAN;
    String alias;
    int timestamp;

    public SetAliasCommand(String email, String accountIBAN, String alias, int timestamp) {
        this.email = email;
        this.accountIBAN = accountIBAN;
        this.alias = alias;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Account account = findAccountByIBAN(accountIBAN);
        account.setAlias(alias);
    }
}
