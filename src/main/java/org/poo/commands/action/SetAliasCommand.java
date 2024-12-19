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
    private String email;
    private String accountIBAN;
    private String alias;
    private int timestamp;

    public SetAliasCommand(final String email, final String accountIBAN, final String alias,
                           final int timestamp) {
        this.email = email;
        this.accountIBAN = accountIBAN;
        this.alias = alias;
        this.timestamp = timestamp;
    }

    /**
     * Get all users in the bank, used for search interface
     * @return List of users
     */
    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    /**
     * Implementation of strategy pattern execute method
     * @param output
     * @param objectMapper
     */
    @Override
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        Account account = findAccountByIBAN(accountIBAN);
        account.setAlias(alias);
    }
}
