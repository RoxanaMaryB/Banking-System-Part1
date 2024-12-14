package org.poo.commands;

import org.poo.commands.action.*;
import org.poo.commands.debug.PrintTransactionsCommand;
import org.poo.commands.debug.PrintUsersCommand;
import org.poo.commands.debug.ReportCommand;
import org.poo.commands.debug.SpendingsReportCommand;
import org.poo.fileio.CommandInput;

public class CommandFactory {
    public static CommandStrategy createCommand(CommandInput commandInput) {
        return switch (commandInput.getCommand()) {
            case "printUsers" -> new PrintUsersCommand(commandInput.getTimestamp());
            case "printTransactions" -> new PrintTransactionsCommand(commandInput.getEmail(),
                    commandInput.getTimestamp());
            case "addAccount" -> new AddAccountCommand(commandInput.getEmail(), commandInput.getCurrency(),
                    commandInput.getAccountType(), commandInput.getTimestamp());
            case "addFunds" -> new AddFundsCommand(commandInput.getAccount(), commandInput.getAmount(),
                    commandInput.getTimestamp());
            case "createCard" -> new CreateCardCommand(commandInput.getAccount(), commandInput.getEmail(),
                    commandInput.getTimestamp());
            case "createOneTimeCard" -> new CreateOneTimeCardCommand(commandInput.getAccount(), commandInput.getEmail(),
                    commandInput.getTimestamp());
            case "deleteCard" -> new DeleteCardCommand(commandInput.getCardNumber(), commandInput.getTimestamp());
            case "deleteAccount" -> new DeleteAccountCommand(commandInput.getAccount(), commandInput.getTimestamp());
            case "payOnline" -> new PayOnlineCommand(commandInput.getCardNumber(), commandInput.getAmount(),
                    commandInput.getCurrency(), commandInput.getDescription(), commandInput.getCommerciant(),
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "sendMoney" -> new SendMoneyCommand(commandInput.getAccount(), commandInput.getReceiver(),
                    commandInput.getAmount(), commandInput.getDescription(), commandInput.getEmail(),
                    commandInput.getTimestamp());
            case "checkCardStatus" -> new CheckCardStatus(commandInput.getCardNumber(), commandInput.getTimestamp());
            case "setMinBalance" -> new SetMinBalanceCommand(commandInput.getAccount(), commandInput.getMinBalance(),
                    commandInput.getTimestamp());
            case "setAlias" -> new SetAliasCommand(commandInput.getEmail(), commandInput.getAccount(),
                    commandInput.getAlias(), commandInput.getTimestamp());
            case "splitPayment" -> new SplitPaymentCommand(commandInput.getAccounts(), commandInput.getCurrency(),
                    commandInput.getAmount(),
                    commandInput.getTimestamp());
            case "report" -> new ReportCommand(commandInput.getStartTimestamp(), commandInput.getEndTimestamp(),
                    commandInput.getAccount(), commandInput.getTimestamp());
            case "spendingsReport" -> new SpendingsReportCommand(commandInput.getStartTimestamp(),
                    commandInput.getEndTimestamp(), commandInput.getAccount(), commandInput.getTimestamp());
            default -> new NoCommand();
        };
    }
}
