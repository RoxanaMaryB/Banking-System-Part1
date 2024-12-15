package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.SavingsAccount;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class AddInterestCommand implements CommandStrategy, Search {
    String accountIBAN;
    int timestamp;

    public AddInterestCommand(String accountIBAN, int timestamp) {
        this.accountIBAN = accountIBAN;
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
            double oldBalance = account.getBalance();
            double interestRate = ((SavingsAccount) account).getInterestRate();
            account.setBalance(oldBalance + oldBalance * interestRate);
        } else {
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "addInterest");
            ObjectNode result = objectMapper.createObjectNode();
            result.put("timestamp", timestamp);
            result.put("description", "This is not a savings account");
            commandOutput.set("output", result);
            commandOutput.put("timestamp", timestamp);
            output.add(commandOutput);
        }
    }
}
