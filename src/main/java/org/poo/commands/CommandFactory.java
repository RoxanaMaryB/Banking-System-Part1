package org.poo.commands;

import org.poo.commands.action.AddAccountCommand;
import org.poo.commands.action.AddFundsCommand;
import org.poo.commands.action.CreateCardCommand;
import org.poo.commands.debug.PrintUsersCommand;
import org.poo.fileio.CommandInput;

public class CommandFactory {
    public static CommandStrategy createCommand(CommandInput commandInput) {
        switch (commandInput.getCommand()) {
            case "printUsers":
                return new PrintUsersCommand(commandInput.getTimestamp());
            case "addAccount":
                return new AddAccountCommand(commandInput.getEmail(), commandInput.getCurrency(),
                        commandInput.getAccountType(), commandInput.getTimestamp());
            case "addFunds":
                return new AddFundsCommand(commandInput.getAccount(), commandInput.getAmount(),
                        commandInput.getTimestamp());
            case "createCard":
                return new CreateCardCommand(commandInput.getAccount(), commandInput.getEmail(),
                        commandInput.getTimestamp());
            default:
                return new NoCommand();
        }
    }
}
