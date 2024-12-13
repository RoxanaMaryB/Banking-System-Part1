package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Card;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class CreateCardCommand implements CommandStrategy, Search {
    String accountIBAN;
    String email;
    int timestamp;

    public CreateCardCommand(String accountIBAN, String email, int timestamp) {
        this.accountIBAN = accountIBAN;
        this.email = email;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper){
        User correctUser = findUserByEmail(email);
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
