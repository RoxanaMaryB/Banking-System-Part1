package org.poo.commands.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;
import org.poo.utils.TransactionsUtils;

import java.util.Comparator;
import java.util.List;

public class PrintTransactionsCommand implements CommandStrategy, Search {
    private final String email;
    private final int timestamp;

    public PrintTransactionsCommand(String email, int timestamp) {
        this.email = email;
        this.timestamp = timestamp;
    }

    @Override
    public List<User> getUsers() {
        return Bank.getInstance().getUsers();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        User user = findUserByEmail(email);
        List<Transaction> transactions = user.getTransactions();
        transactions.sort(Comparator.comparingInt(Transaction::getTimestamp));
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (Transaction transaction : transactions) {
            if(transaction.getEmail() == null || !transaction.getEmail().equals(email)) {
                continue;
            }
            ObjectNode transactionNode = TransactionsUtils.createTransactionNode(objectMapper, transaction);
            transactionsArray.add(transactionNode);
        }
        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "printTransactions");
        commandOutput.set("output", transactionsArray);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }
}
