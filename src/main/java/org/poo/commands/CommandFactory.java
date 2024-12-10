package org.poo.commands;

import org.poo.commands.action.AddAccountCommand;
import org.poo.commands.action.AddFundsCommand;
import org.poo.commands.action.CreateCardCommand;
import org.poo.commands.debug.PrintUsersCommand;
import org.poo.fileio.CommandInput;

public class CommandFactory {
    public static CommandStrategy createCommand(CommandInput commandInput) {
        return switch (commandInput.getCommand()) {
            case "printUsers" -> new PrintUsersCommand(commandInput.getTimestamp());
            case "addAccount" -> new AddAccountCommand(commandInput.getEmail(), commandInput.getCurrency(),
                    commandInput.getAccountType(), commandInput.getTimestamp());
            case "addFunds" -> new AddFundsCommand(commandInput.getAccount(), commandInput.getAmount(),
                    commandInput.getTimestamp());
            case "createCard" -> new CreateCardCommand(commandInput.getAccount(), commandInput.getEmail(),
                    commandInput.getTimestamp());
            default -> new NoCommand();
        };
    }
}
