package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.SavingsAccount;
import org.poo.bank.User;
import org.poo.bank.Transaction;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class AddAccountCommand implements CommandStrategy, Search {
    private String email;
    private String currency;
    private String accountType;
    private int timestamp;

    public AddAccountCommand(final String email, final String currency, final String accountType,
                             final int timestamp) {
        this.email = email;
        this.currency = currency;
        this.accountType = accountType;
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
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        User correctUser = findUserByEmail(email);
        if (correctUser == null) {
            return;
        }
        Account newAccount;
        if (accountType.equals("savings")) {
            newAccount = new SavingsAccount(currency, correctUser, timestamp);
        } else {
            newAccount = new Account(currency, accountType, correctUser, timestamp);
        }
        correctUser.getAccounts().add(newAccount);
        correctUser.logTransaction(Transaction.builder()
                .description("New account created")
                .email(email)
                .timestamp(timestamp)
                .silentIBAN(newAccount.getIban())
                .build());
    }
}
