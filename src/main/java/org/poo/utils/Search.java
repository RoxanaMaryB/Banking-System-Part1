package org.poo.utils;

import org.poo.bank.Account;
import org.poo.bank.User;
import org.poo.bank.Card;
import java.util.List;

public interface Search {
    /**
     * Get all users in the bank
     * @return List of users
     */
    List<User> getUsers();

    /**
     * Find a user by email
     * @param email
     * @return
     */
    default User findUserByEmail(String email) {
        for (User user : getUsers()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Find an account by IBAN
     * @param accountIBAN
     * @return
     */
    default Account findAccountByIBAN(String accountIBAN) {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIban().equals(accountIBAN)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * Find an account by alias
     * @param alias
     * @return
     */
    default Account findAccountByAlias(String alias) {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getAlias() != null && account.getAlias().equals(alias)) {
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * Find a card by card number
     * @param cardNumber
     * @return
     */
    default Card findCardByNumber(String cardNumber) {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        return card;
                    }
                }
            }
        }
        return null;
    }
}
