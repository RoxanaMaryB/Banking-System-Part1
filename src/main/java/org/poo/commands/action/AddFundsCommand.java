package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;

public class AddFundsCommand implements CommandStrategy {
    String accountIBAN;
    double amount;
    int timestamp;

    public AddFundsCommand(String accountIBAN, double amount, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper){
        Bank bank = Bank.getInstance();
        // find IBAN in accounts list
        boolean found = false;
        for (int i = 0; i < bank.getUsers().size(); i++) {
            for (int j = 0; j < bank.getUsers().get(i).getAccounts().size(); j++) {
                if (bank.getUsers().get(i).getAccounts().get(j).getIBAN().equals(accountIBAN)) {
                    User user = bank.getUsers().get(i);
                    double oldBalance = user.getAccounts().get(j).getBalance();
                    user.getAccounts().get(j).setBalance(oldBalance + amount);
                    found = true;
                    break;
                }
            }
            if (found) {
                break;
            }
        }
        if (!found) {
            System.err.println("Account not found: " + accountIBAN);
        }
    }
}
