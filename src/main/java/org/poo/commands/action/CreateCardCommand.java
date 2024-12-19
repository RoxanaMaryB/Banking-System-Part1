package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.bank.Account;
import org.poo.bank.Card;
import org.poo.bank.Transaction;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

@Getter @Setter
public class CreateCardCommand implements CommandStrategy, Search {
    private String accountIBAN;
    private String email;
    private int timestamp;

    public CreateCardCommand(final String accountIBAN, final String email, final int timestamp) {
        this.accountIBAN = accountIBAN;
        this.email = email;
        this.timestamp = timestamp;
    }

    /**
     * Get all users in the bank, used for search interface
     * @return List of users
     */
    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    /**
     * Implementation of strategy pattern execute method
     * @param output
     * @param objectMapper
     */
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        User correctUser = findUserByEmail(email);
        Card newCard = null;
        if (correctUser == null) {
            return;
        } else {
            boolean found = false;
            for (int i = 0; i < correctUser.getAccounts().size(); i++) {
                if (correctUser.getAccounts().get(i).getIban().equals(accountIBAN)) {
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
