package org.poo.commands.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;

public class DeleteCardCommand implements CommandStrategy {
    String cardNumber;
    int timestamp;

    public DeleteCardCommand(String cardNumber, int timestamp) {
        this.cardNumber = cardNumber;
        this.timestamp = timestamp;
    }

    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Bank bank = Bank.getInstance();
        // find card in bank cards list
        User correctUser = null;
        for (User user : bank.getUsers()) {
            for (int i = 0; i < user.getAccounts().size(); i++) {
                for (int j = 0; j < user.getAccounts().get(i).getCards().size(); j++) {
                    if (user.getAccounts().get(i).getCards().get(j).getCardNumber().equals(cardNumber)) {
                        correctUser = user;
                        user.getAccounts().get(i).getCards().remove(j);
                        break;
                    }
                }
                if(correctUser != null)
                    break;
            }
            if(correctUser != null)
                break;
        }
    }
}
