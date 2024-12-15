package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.*;
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
        Card newCard = null;
        if (correctUser == null) {
            System.err.println("User not found: " + email);
            return;
        } else {
            // find IBAN in correctUser accounts list
            boolean found = false;
            for (int i = 0; i < correctUser.getAccounts().size(); i++) {
                if (correctUser.getAccounts().get(i).getIBAN().equals(accountIBAN)) {
                    Account account = correctUser.getAccounts().get(i);
                    newCard = new Card(account);
                    account.getCards().add(newCard);
                    found = true;
                    break;
                }
            }
            if (!found) {
                correctUser.logTransaction(Transaction.builder()
                        .description("Account not found")
                        .email(email)
                        .timestamp(timestamp)
                        .build());
                return;
            }
        }
        correctUser.logTransaction(Transaction.builder()
                .description("New card created")
                .accountIBAN(accountIBAN)
                .card(newCard.getCardNumber())
                .cardHolder(newCard.getAccount().getUser().getEmail())
                .email(email)
                .timestamp(timestamp)
                .silentIBAN(accountIBAN)
                .build());
    }
}
