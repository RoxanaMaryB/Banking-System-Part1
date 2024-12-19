package org.poo.commands;
import org.poo.commands.action.AddAccountCommand;
import org.poo.commands.action.AddFundsCommand;
import org.poo.commands.action.AddInterestCommand;
import org.poo.commands.action.ChangeInterestRateCommand;
import org.poo.commands.action.CheckCardStatus;
import org.poo.commands.action.CreateCardCommand;
import org.poo.commands.action.CreateOneTimeCardCommand;
import org.poo.commands.action.DeleteAccountCommand;
import org.poo.commands.action.DeleteCardCommand;
import org.poo.commands.action.PayOnlineCommand;
import org.poo.commands.action.SendMoneyCommand;
import org.poo.commands.action.SetAliasCommand;
import org.poo.commands.action.SetMinBalanceCommand;
import org.poo.commands.action.SplitPaymentCommand;
import org.poo.commands.debug.PrintTransactionsCommand;
import org.poo.commands.debug.PrintUsersCommand;
import org.poo.commands.debug.ReportCommand;
import org.poo.commands.debug.SpendingsReportCommand;
import org.poo.fileio.CommandInput;

public final class CommandFactory {

    /**
     * Private constructor to prevent instantiation of this class
     */
    private CommandFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Create a command based on the command input
     * @param commandInput
     * @return
     */
    public static CommandStrategy createCommand(final CommandInput commandInput) {
        return switch (commandInput.getCommand()) {
            case "printUsers" -> new PrintUsersCommand(commandInput.getTimestamp());
            case "printTransactions" -> new PrintTransactionsCommand(commandInput.getEmail(),
                    commandInput.getTimestamp());
            case "addAccount" -> new AddAccountCommand(commandInput.getEmail(),
                    commandInput.getCurrency(), commandInput.getAccountType(),
                    commandInput.getTimestamp());
            case "addFunds" -> new AddFundsCommand(commandInput.getAccount(),
                    commandInput.getAmount(), commandInput.getTimestamp());
            case "createCard" -> new CreateCardCommand(commandInput.getAccount(),
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "createOneTimeCard" -> new CreateOneTimeCardCommand(commandInput.getAccount(),
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "deleteCard" -> new DeleteCardCommand(commandInput.getCardNumber(),
                    commandInput.getTimestamp());
            case "deleteAccount" -> new DeleteAccountCommand(commandInput.getAccount(),
                    commandInput.getTimestamp());
            case "payOnline" -> new PayOnlineCommand(commandInput.getCardNumber(),
                    commandInput.getAmount(), commandInput.getCurrency(),
                    commandInput.getDescription(), commandInput.getCommerciant(),
                    commandInput.getEmail(), commandInput.getTimestamp());
            case "sendMoney" -> new SendMoneyCommand(commandInput.getAccount(),
                    commandInput.getReceiver(), commandInput.getAmount(),
                    commandInput.getDescription(), commandInput.getEmail(),
                    commandInput.getTimestamp());
            case "checkCardStatus" -> new CheckCardStatus(commandInput.getCardNumber(),
                    commandInput.getTimestamp());
            case "setMinBalance" -> new SetMinBalanceCommand(commandInput.getAccount(),
                    commandInput.getMinBalance(), commandInput.getTimestamp());
            case "setAlias" -> new SetAliasCommand(commandInput.getEmail(),
                    commandInput.getAccount(), commandInput.getAlias(),
                    commandInput.getTimestamp());
            case "splitPayment" -> new SplitPaymentCommand(commandInput.getAccounts(),
                    commandInput.getCurrency(), commandInput.getAmount(),
                    commandInput.getTimestamp());
            case "report" -> new ReportCommand(commandInput.getStartTimestamp(),
                    commandInput.getEndTimestamp(), commandInput.getAccount(),
                    commandInput.getTimestamp());
            case "spendingsReport" -> new SpendingsReportCommand(commandInput.getStartTimestamp(),
                    commandInput.getEndTimestamp(), commandInput.getAccount(),
                    commandInput.getTimestamp());
            case "addInterest" -> new AddInterestCommand(commandInput.getAccount(),
                    commandInput.getTimestamp());
            case "changeInterestRate" -> new ChangeInterestRateCommand(commandInput.getAccount(),
                    commandInput.getInterestRate(), commandInput.getTimestamp());
            default -> new NoCommand();
        };
    }
}
