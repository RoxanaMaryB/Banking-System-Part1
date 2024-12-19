package org.poo.bank;

import lombok.Data;
import org.poo.fileio.UserInput;


import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts;
    private List<Transaction> transactions;

    public User(final UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    /**
     * Add a transaction to the user's transaction list
     * @param transaction
     */
    public void logTransaction(final Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
     * Empty the user's transaction list
     */
    public void clearTransactions() {
        this.transactions.clear();
    }
}
