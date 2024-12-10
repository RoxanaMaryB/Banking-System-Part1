package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;

public class DeleteAccountCommand implements CommandStrategy {
    String accountIBAN;
    int timestamp;

    public DeleteAccountCommand(String accountIBAN, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Bank bank = Bank.getInstance();
        // find IBAN in bank accounts list
        User correctUser = null;
        for (User user : bank.getUsers()) {
            for (int i = 0; i < user.getAccounts().size(); i++) {
                if (user.getAccounts().get(i).getIBAN().equals(accountIBAN)) {
                    correctUser = user;
                    user.getAccounts().remove(i);
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
