package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.SavingsAccount;
import org.poo.bank.User;
import org.poo.bank.Transaction;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class ChangeInterestRateCommand implements CommandStrategy, Search {
    private String accountIBAN;
    private double interestRate;
    private int timestamp;

    public ChangeInterestRateCommand(final String accountIBAN, final double interestRate,
                                     final int timestamp) {
        this.accountIBAN = accountIBAN;
        this.interestRate = interestRate;
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
        if (account.getType().equals("savings")) {
            ((SavingsAccount) account).setInterestRate(interestRate);
        } else {
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "changeInterestRate");
            ObjectNode result = objectMapper.createObjectNode();
            result.put("timestamp", timestamp);
            result.put("description", "This is not a savings account");
            commandOutput.set("output", result);
            commandOutput.put("timestamp", timestamp);
            output.add(commandOutput);
        }
        User user = account.getUser();
        user.logTransaction(Transaction.builder()
                .description("Interest rate of the account changed to " + interestRate)
                .email(user.getEmail())
                .timestamp(timestamp)
                .silentIBAN(accountIBAN)
                .build());
    }
}
