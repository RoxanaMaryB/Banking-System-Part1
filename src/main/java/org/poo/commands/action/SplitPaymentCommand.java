package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bank.*;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.List;

public class SplitPaymentCommand implements CommandStrategy, Search {
    List<String> accountsForSplit;
    String currency;
    double amount;
    int timestamp;

    public SplitPaymentCommand(List<String> accountsForSplit, String currency, double amount, int timestamp) {
        this.accountsForSplit = accountsForSplit;
        this.currency = currency;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        // calculate the amount for each account
        CurrencyConverter converter = new CurrencyConverter(Bank.getInstance().getExchangeRates());
        double amountPerAccount = amount / accountsForSplit.size();
        boolean notEnoughMoney = false;
        for (String accountIBAN : accountsForSplit) {
            Account account = findAccountByIBAN(accountIBAN);
            double amountForAccount = converter.convertCurrency(amountPerAccount, currency, account.getCurrency());
            if(account.getBalance() < amountForAccount) {
                notEnoughMoney = true;
                break;
            }
        }
        if(notEnoughMoney) {
            for (String accountIBAN : accountsForSplit) {
                Account account = findAccountByIBAN(accountIBAN);
                User user = account.getUser();
                user.logTransaction(Transaction.builder()
                        .description("Split payment failed")
                        .email(user.getEmail())
                        .timestamp(timestamp)
                        .build());
            }
            return;
        }

        for (String accountIBAN : accountsForSplit) {
            Account account = findAccountByIBAN(accountIBAN);
            User user = account.getUser();
            double amountForAccount = converter.convertCurrency(amountPerAccount, currency, account.getCurrency());
            String description = String.format("Split payment of %.2f %s", amount, currency);
            user.logTransaction(Transaction.builder()
                    .description(description)
                    .amountDouble(amountPerAccount)
                    .currency(currency)
                    .involvedAccounts(accountsForSplit)
                    .email(user.getEmail())
                    .timestamp(timestamp)
                    .build());
            account.setBalance(account.getBalance() - amountForAccount);
        }
    }
}
