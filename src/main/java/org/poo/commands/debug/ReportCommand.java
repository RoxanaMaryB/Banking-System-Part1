package org.poo.commands.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;
import org.poo.utils.TransactionsUtils;

import java.util.Comparator;
import java.util.List;

public class ReportCommand implements CommandStrategy, Search {
    protected final int start;
    protected final int end;
    protected final String accountIBAN;
    protected final int timestamp;

    public ReportCommand(final int start, final int end, final String accountIBAN,
                         final int timestamp) {
        this.start = start;
        this.end = end;
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
    }

    /**
     * Get all users in the bank, used for search interface
     * @return List of users
     */
    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    /**
     * Implementation of strategy pattern execute method
     * @param output
     * @param objectMapper
     */
    @Override
    public void execute(final ArrayNode output, final ObjectMapper objectMapper) {
        Account account = findAccountByIBAN(accountIBAN);
        if (account == null) {
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "report");
            noAccountFound(output, objectMapper, commandOutput);
            return;
        }
        User user = account.getUser();
        if (user == null) {
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "report");
            noUserFound(output, objectMapper, commandOutput);
            return;
        }
        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "report");
        commandOutput.set("output", formReport(objectMapper, account, user.getTransactions()));
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }

    /**
     * Method to add a no account found message to the output
     * @param output
     * @param objectMapper
     * @param commandOutput
     */
    public void noAccountFound(final ArrayNode output, final ObjectMapper objectMapper,
                               final ObjectNode commandOutput) {
        ObjectNode noAccount = objectMapper.createObjectNode();
        noAccount.put("description", "Account not found");
        noAccount.put("timestamp", timestamp);
        commandOutput.set("output", noAccount);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }

    /**
     * Method to add a no user found message to the output
     * @param output
     * @param objectMapper
     * @param commandOutput
     */
    public void noUserFound(final ArrayNode output, final ObjectMapper objectMapper,
                            final ObjectNode commandOutput) {
        ObjectNode noUser = objectMapper.createObjectNode();
        noUser.put("description", "User not found");
        noUser.put("timestamp", timestamp);
        commandOutput.set("output", noUser);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }

    /**
     * Method to check if a transaction is valid for the report
     * @param transaction
     * @return
     */
    public boolean checkValidTransaction(final Transaction transaction) {
        return transaction.getSilentIBAN() != null && transaction.getSilentIBAN().
                equals(accountIBAN) && transaction.getTimestamp() >= start
                && transaction.getTimestamp() <= end;
    }

    /**
     * Method to form the report and add it to the output
     * @param objectMapper
     * @param account
     * @param transactions
     * @return
     */
    public ObjectNode formReport(final ObjectMapper objectMapper, final Account account,
                                 final List<Transaction> transactions) {
        transactions.sort(Comparator.comparingInt(Transaction::getTimestamp));
        ObjectNode report = objectMapper.createObjectNode();
        report.put("IBAN", account.getIban());
        report.put("balance", account.getBalance());
        report.put("currency", account.getCurrency());
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (Transaction transaction : transactions) {
            if (checkValidTransaction(transaction)) {
                ObjectNode transactionNode = TransactionsUtils.createTransactionNode(objectMapper,
                        transaction);
                transactionsArray.add(transactionNode);
            }
        }
        report.set("transactions", transactionsArray);
        return report;
    }

}
