package org.poo.commands.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Bank;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.commands.CommandStrategy;
import org.poo.utils.Search;

import java.util.Comparator;
import java.util.List;

public class PrintTransactionsCommand implements CommandStrategy, Search {
    private final String email;
    private final int timestamp;

    public PrintTransactionsCommand(String email, int timestamp) {
        this.email = email;
        this.timestamp = timestamp;
    }

    private void addField(ObjectNode node, String fieldName, String value) {
        if (value != null) {
            node.put(fieldName, value);
        }
    }

    // who added this is evil
    private void addDoubleField(ObjectNode node, String fieldName, double value) {
        if(value != 0) {
            node.put(fieldName, value);
        }
    }

    private void addListOfStrings(ObjectNode node, String fieldName, List<String> values) {
        if (values != null) {
            ArrayNode arrayNode = node.putArray(fieldName);
            for (String value : values) {
                arrayNode.add(value);
            }
        }
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
            ObjectNode transactionNode = objectMapper.createObjectNode();
            transactionNode.put("timestamp", transaction.getTimestamp());
            transactionNode.put("description", transaction.getDescription());
            addField(transactionNode, "account", transaction.getAccountIBAN());
            addField(transactionNode, "senderIBAN", transaction.getSenderIBAN());
            addField(transactionNode, "receiverIBAN", transaction.getReceiverIBAN());
            addField(transactionNode, "card", transaction.getCard());
            addField(transactionNode, "cardHolder", transaction.getCardHolder());
            addField(transactionNode, "amount", transaction.getAmount());
            addDoubleField(transactionNode, "amount", transaction.getAmountDouble());
            addField(transactionNode, "currency", transaction.getCurrency());
            addField(transactionNode, "transferType", transaction.getTransferType());
            addField(transactionNode, "commerciant", transaction.getCommerciant());
            addListOfStrings(transactionNode, "involvedAccounts", transaction.getInvolvedAccounts());
            transactionsArray.add(transactionNode);
        }
        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "printTransactions");
        commandOutput.set("output", transactionsArray);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }
}
