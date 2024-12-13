package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.OneTimeCard;
import org.poo.bank.User;

public class CreateOneTimeCardCommand extends CreateCardCommand {
    public CreateOneTimeCardCommand(String accountIBAN, String email, int timestamp) {
        super(accountIBAN, email, timestamp);
    }

    //TODO: MODIFY!!! REPEATING CODE!!!
    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        User correctUser = findUserByEmail(email);
        if (correctUser == null) {
            System.err.println("User not found: " + email);
        } else {
            boolean found = false;
            for (int i = 0; i < correctUser.getAccounts().size(); i++) {
                if (correctUser.getAccounts().get(i).getIBAN().equals(accountIBAN)) {
                    Account account = correctUser.getAccounts().get(i);
                    account.getCards().add(new OneTimeCard());
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
