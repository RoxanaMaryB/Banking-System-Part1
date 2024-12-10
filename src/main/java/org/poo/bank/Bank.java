package org.poo.bank;

import lombok.Data;
import org.poo.commands.CommandFactory;
import org.poo.commands.CommandStrategy;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.ObjectInput;
import org.poo.fileio.UserInput;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.poo.utils.Utils.resetRandom;

@Data
public class Bank {
    private static Bank instance;
    private List<User> users;
    private List<Exchange> exchangeRates;

    private Bank() {
        this.users = new ArrayList<>();
        this.exchangeRates = new ArrayList<>();
    }

    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    public void reset() {
        this.users.clear();
        this.exchangeRates.clear();
        resetRandom();
    }

    public void startBank(final ObjectInput inputData, final ArrayNode out, final ObjectMapper objMapper) {
        reset();

        for (UserInput user : inputData.getUsers()) {
            this.users.add(new User(user));
        }

        if(inputData.getExchangeRates() != null) {
            for (ExchangeInput exchange : inputData.getExchangeRates()) {
                this.exchangeRates.add(new Exchange(exchange));
            }
        }

        // create a factory for the commands
        for (CommandInput commandInput : inputData.getCommands()) {
            CommandStrategy command = CommandFactory.createCommand(commandInput);
            command.execute(out, objMapper);
        }
    }
}
