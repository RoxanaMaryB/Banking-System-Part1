package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.*;

public class CreateOneTimeCardCommand extends CreateCardCommand {
    public CreateOneTimeCardCommand(String accountIBAN, String email, int timestamp) {
        super(accountIBAN, email, timestamp);
    }

    //TODO: MODIFY!!! REPEATING CODE!!!
    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        User correctUser = findUserByEmail(email);
        Card newCard = null;
        if (correctUser == null) {
            System.err.println("User not found: " + email);
        } else {
            boolean found = false;
            for (int i = 0; i < correctUser.getAccounts().size(); i++) {
                if (correctUser.getAccounts().get(i).getIBAN().equals(accountIBAN)) {
                    Account account = correctUser.getAccounts().get(i);
                    newCard = new OneTimeCard(account);
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
            correctUser.logTransaction(Transaction.builder()
                    .description("New card created")
                    .accountIBAN(accountIBAN)
                    .card(newCard.getCardNumber())
                    .cardHolder(newCard.getAccount().getUser().getEmail())
                    .email(email)
                    .timestamp(timestamp)
                    .build());
        }
    }
}
