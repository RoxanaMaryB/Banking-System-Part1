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

    public User(UserInput userInput) {
        this.firstName = userInput.getFirstName();
        this.lastName = userInput.getLastName();
        this.email = userInput.getEmail();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public void logTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void clearTransactions() {
        this.transactions.clear();
    }
}
