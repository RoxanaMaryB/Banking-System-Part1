package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Card;
import org.poo.bank.OneTimeCard;
import org.poo.bank.User;
import org.poo.bank.Transaction;

public class CreateOneTimeCardCommand extends CreateCardCommand {
    public CreateOneTimeCardCommand(final String accountIBAN, final String email,
                                    final int timestamp) {
        super(accountIBAN, email, timestamp);
    }

    /**
     * Implementation of strategy pattern execute method
     * @param output
     * @param objectMapper
     */
    @Override
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        User correctUser = findUserByEmail(super.getEmail());
        Card newCard = null;
        if (correctUser == null) {
            return;
        }
        boolean found = false;
        for (int i = 0; i < correctUser.getAccounts().size(); i++) {
            if (correctUser.getAccounts().get(i).getIban().equals(super.getAccountIBAN())) {
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
                    .email(super.getEmail())
                    .timestamp(super.getTimestamp())
                    .build());
            return;
        }
        correctUser.logTransaction(Transaction.builder()
                .description("New card created")
                .accountIBAN(super.getAccountIBAN())
                .card(newCard.getCardNumber())
                .cardHolder(newCard.getAccount().getUser().getEmail())
                .email(super.getEmail())
                .timestamp(super.getTimestamp())
                .silentIBAN(super.getAccountIBAN())
                .build());
    }
}
