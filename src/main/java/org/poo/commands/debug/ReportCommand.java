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

    public ReportCommand(int start, int end, String accountIBAN, int timestamp) {
        this.start = start;
        this.end = end;
        this.accountIBAN = accountIBAN;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
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

    public void noAccountFound(ArrayNode output, ObjectMapper objectMapper, ObjectNode commandOutput) {
        ObjectNode noAccount = objectMapper.createObjectNode();
        noAccount.put("description", "Account not found");
        noAccount.put("timestamp", timestamp);
        commandOutput.set("output", noAccount);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }

    public void noUserFound(ArrayNode output, ObjectMapper objectMapper, ObjectNode commandOutput) {
        ObjectNode noUser = objectMapper.createObjectNode();
        noUser.put("description", "User not found");
        noUser.put("timestamp", timestamp);
        commandOutput.set("output", noUser);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }

    public boolean checkValidTransaction(Transaction transaction) {
        return transaction.getTimestamp() >= start && transaction.getTimestamp() <= end;
    }

    public ObjectNode formReport(ObjectMapper objectMapper, Account account, List<Transaction> transactions) {
        transactions.sort(Comparator.comparingInt(Transaction::getTimestamp));
        ObjectNode report = objectMapper.createObjectNode();
        report.put("IBAN", account.getIBAN());
        report.put("balance", account.getBalance());
        report.put("currency", account.getCurrency());
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (Transaction transaction : transactions) {
            if (checkValidTransaction(transaction)) {
                ObjectNode transactionNode = TransactionsUtils.createTransactionNode(objectMapper, transaction);
                transactionsArray.add(transactionNode);
            }
        }
        report.set("transactions", transactionsArray);
        return report;
    }

}
