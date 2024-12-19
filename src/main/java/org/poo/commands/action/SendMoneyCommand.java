package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.bank.CurrencyConverter;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class SendMoneyCommand implements CommandStrategy, Search {
    private String senderIBAN;
    private String receiverIBAN;
    private double amount;
    private String description;
    private String email;
    private int timestamp;

    public SendMoneyCommand(final String senderIBAN, final String receiverIBAN,
                            final double amount, final String description, final String email,
                            final int timestamp) {
        this.senderIBAN = senderIBAN;
        this.receiverIBAN = receiverIBAN;
        this.amount = amount;
        this.description = description;
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
    @Override
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        Account sender = findAccountByIBAN(senderIBAN);
        Account receiver = findAccountByIBAN(receiverIBAN);
        if (receiver == null) {
            receiver = findAccountByAlias(receiverIBAN);
        }
        if (sender == null) {
            return;
        }
        if (receiver == null) {
            return;
        }

        if (sender.getBalance() < amount) {
            sender.getUser().logTransaction(Transaction.builder()
                    .description("Insufficient funds")
                    .email(email)
                    .timestamp(timestamp)
                    .silentIBAN(senderIBAN)
                    .build());
            return;
        }

        sender.setBalance(sender.getBalance() - amount);
        CurrencyConverter converter = CurrencyConverter.getInstance(Bank.getInstance().
                getExchangeRates());
        double amountInReceiverCurrency = converter.convertCurrency(amount, sender.getCurrency(),
                receiver.getCurrency());
        receiver.setBalance(receiver.getBalance() + amountInReceiverCurrency);

        String amountWithCurrencySender = amount + " " + sender.getCurrency();
        String amountWithCurrencyReceiver = amountInReceiverCurrency + " "
                + receiver.getCurrency();

        sender.getUser().logTransaction(Transaction.builder()
                .description(description)
                .email(email)
                .senderIBAN(senderIBAN)
                .receiverIBAN(receiverIBAN)
                .amount(amountWithCurrencySender)
                .transferType("sent")
                .timestamp(timestamp)
                .silentIBAN(senderIBAN)
                .build());


        receiver.getUser().logTransaction(Transaction.builder()
                .description(description)
                .email(receiver.getUser().getEmail())
                .senderIBAN(senderIBAN)
                .receiverIBAN(receiverIBAN)
                .amount(amountWithCurrencyReceiver)
                .transferType("received")
                .timestamp(timestamp)
                .silentIBAN(receiverIBAN)
                .build());
    }
}
