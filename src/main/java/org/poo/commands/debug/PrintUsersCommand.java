package org.poo.commands.debug;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;

public class PrintUsersCommand implements CommandStrategy {
    private final int timestamp;

    public PrintUsersCommand(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Bank bank = Bank.getInstance();
        ArrayNode usersArray = objectMapper.createArrayNode();
        for (User user : bank.getUsers()) {
            ObjectNode userNode = objectMapper.createObjectNode();
            ArrayNode accountsArray = objectMapper.createArrayNode();
            for (int i = 0; i < user.getAccounts().size(); i++) {
                ObjectNode accountNode = objectMapper.createObjectNode();
                accountNode.put("balance", user.getAccounts().get(i).getBalance());
                ArrayNode cardsArray = objectMapper.createArrayNode();
                for (int j = 0; j < user.getAccounts().get(i).getCards().size(); j++) {
                    ObjectNode cardNode = objectMapper.createObjectNode();
                    cardNode.put("cardNumber", user.getAccounts().get(i).getCards().get(j).getCardNumber());
                    cardNode.put("status", user.getAccounts().get(i).getCards().get(j).getStatus());
                    cardsArray.add(cardNode);
                }
                accountNode.set("cards", cardsArray);
                accountNode.put("currency", user.getAccounts().get(i).getCurrency());
                accountNode.put("IBAN", user.getAccounts().get(i).getIBAN());
                accountNode.put("type", user.getAccounts().get(i).getType());
                accountsArray.add(accountNode);
            }
            userNode.set("accounts", accountsArray);
            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());
            usersArray.add(userNode);
        }
        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "printUsers");
        commandOutput.set("output", usersArray);
        commandOutput.put("timestamp", timestamp);

        output.add(commandOutput);
    }
}
