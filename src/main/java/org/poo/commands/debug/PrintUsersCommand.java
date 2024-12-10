package org.poo.commands.debug;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;

public class PrintUsersCommand implements CommandStrategy {
    private int timestamp;

    public PrintUsersCommand(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Bank bank = Bank.getInstance();
        ArrayNode usersArray = objectMapper.createArrayNode();

        for (User user : bank.getUsers()) {
            ObjectNode userNode = objectMapper.createObjectNode();
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
