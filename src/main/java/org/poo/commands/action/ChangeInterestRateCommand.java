package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.*;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class ChangeInterestRateCommand implements CommandStrategy, Search {
    String accountIBAN;
    double interestRate;
    int timestamp;

    public ChangeInterestRateCommand(String accountIBAN, double interestRate, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.interestRate = interestRate;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
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
