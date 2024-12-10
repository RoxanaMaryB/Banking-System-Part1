package org.poo.commands;

import org.poo.commands.debug.PrintUsersCommand;
import org.poo.fileio.CommandInput;

public class CommandFactory {
    public static CommandStrategy createCommand(CommandInput commandInput) {
        switch (commandInput.getCommand()) {
            case "printUsers":
                return new PrintUsersCommand(commandInput.getTimestamp());
            default:
                return new NoCommand();
        }
    }
}
