package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class DeleteAccountCommand implements CommandStrategy, Search {
    String accountIBAN;
    int timestamp;

    public DeleteAccountCommand(String accountIBAN, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Account account = findAccountByIBAN(accountIBAN);
        User correctUser = null;

        if (account != null) {
            for (User user : getUsers()) {
                if (user.getAccounts().remove(account)) {
                    correctUser = user;
                    break;
                }
            }
        }

        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "deleteAccount");

        ObjectNode deletionState = objectMapper.createObjectNode();
        if(correctUser == null)
            deletionState.put("fail", "Account not found");
        else
            deletionState.put("success", "Account deleted");
        deletionState.put("timestamp", timestamp);
        commandOutput.set("output", deletionState);

        commandOutput.put("timestamp", timestamp);

        output.add(commandOutput);
    }
}
