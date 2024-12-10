package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;

public class CreateCardCommand implements CommandStrategy {
    String accountIBAN;
    String email;
    int timestamp;

    public CreateCardCommand(String accountIBAN, String email, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.email = email;
        this.timestamp = timestamp;
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper){
        Bank bank = Bank.getInstance();
        // find email in users list
        User correctUser = null;
        for (User user : bank.getUsers()) {
            if (user.getEmail().equals(email)) {
                correctUser = user;
                break;
            }
        }
        if (correctUser == null) {
            System.err.println("User not found: " + email);
        } else {
            // find IBAN in correctUser accounts list
            boolean found = false;
            for (int i = 0; i < correctUser.getAccounts().size(); i++) {
                if (correctUser.getAccounts().get(i).getIBAN().equals(accountIBAN)) {
                    Account account = correctUser.getAccounts().get(i);
                    account.getCards().add(new Card());
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.err.println("Account not found: " + accountIBAN);
            }
        }
    }
}
