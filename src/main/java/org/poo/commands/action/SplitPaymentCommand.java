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

public class SplitPaymentCommand implements CommandStrategy, Search {
    private List<String> accountsForSplit;
    private String currency;
    private double amount;
    private int timestamp;

    public SplitPaymentCommand(final List<String> accountsForSplit, final String currency,
                               final double amount, final int timestamp) {
        this.accountsForSplit = accountsForSplit;
        this.currency = currency;
        this.amount = amount;
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
        CurrencyConverter converter = CurrencyConverter.getInstance(Bank.getInstance()
                .getExchangeRates());
        double amountPerAccount = amount / accountsForSplit.size();
        boolean notEnoughMoney = false;
        Account brokeAccount = null;
        for (String accountIBAN : accountsForSplit) {
            Account account = findAccountByIBAN(accountIBAN);
            double amountForAccount = converter.convertCurrency(amountPerAccount, currency,
                    account.getCurrency());
            if (account.getBalance() < amountForAccount) {
                notEnoughMoney = true;
                brokeAccount = account;
            }
        }
        if (notEnoughMoney) {
            for (String accountIBAN : accountsForSplit) {
                Account account = findAccountByIBAN(accountIBAN);
                User user = account.getUser();
                String description = String.format("Split payment of %.2f %s", amount, currency);
                user.logTransaction(Transaction.builder()
                        .error("Account " + brokeAccount.getIban()
                                + " has insufficient funds for a split payment.")
                        .description(description)
                        .amountDouble(amountPerAccount)
                        .currency(currency)
                        .involvedAccounts(accountsForSplit)
                        .email(user.getEmail())
                        .timestamp(timestamp)
                        .silentIBAN(accountIBAN)
                        .build());
            }
            return;
        }

        for (String accountIBAN : accountsForSplit) {
            Account account = findAccountByIBAN(accountIBAN);
            User user = account.getUser();
            double amountForAccount = converter.convertCurrency(amountPerAccount, currency,
                    account.getCurrency());
            String description = String.format("Split payment of %.2f %s", amount, currency);
            user.logTransaction(Transaction.builder()
                    .description(description)
                    .amountDouble(amountPerAccount)
                    .currency(currency)
                    .involvedAccounts(accountsForSplit)
                    .email(user.getEmail())
                    .timestamp(timestamp)
                    .silentIBAN(accountIBAN)
                    .build());
            account.setBalance(account.getBalance() - amountForAccount);
        }
    }
}
