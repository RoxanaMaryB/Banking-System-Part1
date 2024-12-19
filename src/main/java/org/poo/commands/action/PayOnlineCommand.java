package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.bank.Card;
import org.poo.bank.CurrencyConverter;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;
import java.util.List;

public class PayOnlineCommand implements CommandStrategy, Search {
    private String cardNumber;
    private double amount;
    private String currency;
    private int timestamp;
    private String description;
    private String commerciant;
    private String email;

    public PayOnlineCommand(final String cardNumber, final double amount,
                            final String currency, final String description,
                            final String commerciant, final String email, final int timestamp) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.currency = currency;
        this.timestamp = timestamp;
        this.description = description;
        this.commerciant = commerciant;
        this.email = email;
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
    @Override
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        User correctUser = findUserByEmail(email);
        if (correctUser == null) {
            return;
        }
        boolean found = false;
        Account correctAccount = null;
        Card correctCard = null;
        for (Account account : correctUser.getAccounts()) {
            for (int i = 0; i < account.getCards().size(); i++) {
                if (account.getCards().get(i).getCardNumber().equals(cardNumber)) {
                    correctCard = account.getCards().get(i);
                    found = true;
                    correctAccount = account;
                    break;
                }
            }
        }
        if (!found) {
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "payOnline");
            Card.cardNotFound(output, objectMapper, commandOutput, timestamp);
            return;
        }
        if (correctCard.getStatus().equals("frozen")) {
            correctUser.logTransaction(Transaction.builder()
                    .description("The card is frozen")
                    .timestamp(timestamp)
                    .email(email)
                    .build());
            return;
        }
        CurrencyConverter converter = CurrencyConverter.getInstance(Bank.getInstance()
                .getExchangeRates());
        boolean insufficientFunds = true;
        double convertedAmount = amount;
        if (!correctAccount.getCurrency().equals(currency)) {
            convertedAmount = converter.convertCurrency(amount, currency,
                    correctAccount.getCurrency());
        }
        if (correctAccount.getBalance() - convertedAmount >= 0) {
            correctAccount.setBalance(correctAccount.getBalance() - convertedAmount);
            insufficientFunds = false;
        }
        if (insufficientFunds) {
            correctUser.logTransaction(Transaction.builder()
                    .description("Insufficient funds")
                    .email(email)
                    .timestamp(timestamp)
                    .build());
            return;
        }
        correctUser.logTransaction(Transaction.builder()
                .description("Card payment")
                .commerciant(commerciant)
                .email(email)
                .silentIBAN(correctAccount.getIban())
                .amountDouble(convertedAmount)
                .timestamp(timestamp)
                .build());
        correctCard.changeIfOneTime(timestamp);
    }
}
