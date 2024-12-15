package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
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
        User user;
        boolean fails = false;
        if(account == null) {
            return;
        } else  {
            user = account.getUser();
            if(user == null) {
                return;
            }
            if(account.getBalance() != 0) {
                fails = true;
            }
        }
        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "deleteAccount");
        ObjectNode deletionState = objectMapper.createObjectNode();
        if(fails) {
            deletionState.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
            user.logTransaction(Transaction.builder()
                    .description("Account couldn't be deleted - there are funds remaining")
                    .email(user.getEmail())
                    .timestamp(timestamp)
                    .silentIBAN(accountIBAN)
                    .build());
        } else {
            account.deleteAccount();
            deletionState.put("success", "Account deleted");
        }
        deletionState.put("timestamp", timestamp);
        commandOutput.set("output", deletionState);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }
}
