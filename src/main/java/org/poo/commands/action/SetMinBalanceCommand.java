package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class SetMinBalanceCommand implements CommandStrategy, Search {
    private String accountIBAN;
    private double minBalance;
    private int timestamp;

    public SetMinBalanceCommand(final String accountIBAN, final double minBalance,
                                final int timestamp) {
        this.accountIBAN = accountIBAN;
        this.minBalance = minBalance;
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
        if (account != null) {
            account.setMinBalance(minBalance);
            account.getUser().logTransaction(Transaction.builder()
                    .description("Set minimum balance")
                    .email(account.getUser().getEmail())
                    .timestamp(timestamp)
                    .silentIBAN(accountIBAN)
                    .build());
        }
    }
}
