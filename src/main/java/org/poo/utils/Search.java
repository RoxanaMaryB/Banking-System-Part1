package org.poo.utils;

import org.poo.bank.*;

import java.util.List;

public interface Search {
    List<User> getUsers();

    default User findUserByEmail(String email) {
        for (User user : getUsers()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    default Account findAccountByIBAN(String accountIBAN) {
        for (User user : getUsers()) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(accountIBAN)) {
                    return account;
                }
            }
        }
        return null;
    }

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
