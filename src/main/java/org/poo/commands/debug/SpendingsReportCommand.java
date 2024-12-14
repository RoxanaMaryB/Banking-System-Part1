package org.poo.commands.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Commerciant;
import org.poo.bank.Transaction;
import org.poo.bank.User;
import org.poo.utils.TransactionsUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SpendingsReportCommand extends ReportCommand {
    List<Commerciant> commerciants;
    public SpendingsReportCommand(int start, int end, String accountIBAN, int timestamp) {
        super(start, end, accountIBAN, timestamp);
        commerciants = new ArrayList<>();
    }

    @Override
    public void execute(ArrayNode output, ObjectMapper objectMapper) {
        Account account = findAccountByIBAN(accountIBAN);
        if (account == null) {
            System.out.println("Account not found");
            return;
        }
        User user = account.getUser();
        if (user == null) {
            System.out.println("User not found");
            return;
        }
        List<Transaction> transactions = user.getTransactions();
        transactions.sort(Comparator.comparingInt(Transaction::getTimestamp));
        ObjectNode report = objectMapper.createObjectNode();
        report.put("IBAN", account.getIBAN());
        report.put("balance", account.getBalance());
        report.put("currency", account.getCurrency());
        ArrayNode transactionsArray = objectMapper.createArrayNode();
        for (Transaction transaction : transactions) {
            if (transaction.getSilentIBAN() != null && transaction.getSilentIBAN().equals(accountIBAN) &&
                    transaction.getTimestamp() >= start && transaction.getTimestamp() <= end &&
                    transaction.getDescription().equals("Card payment")) {
                ObjectNode transactionNode = TransactionsUtils.createTransactionNode(objectMapper, transaction);
                transactionsArray.add(transactionNode);
                boolean found = false;
                for (Commerciant c : commerciants) {
                    if (c.getName().equals(transaction.getCommerciant())) {
                        c.setAmount(c.getAmount() + transaction.getAmountDouble());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Commerciant commerciant = new Commerciant(transaction.getCommerciant(), transaction.getAmountDouble());
                    commerciants.add(commerciant);
                }
            }
        }
        report.set("transactions", transactionsArray);
        ArrayNode commerciantsArray = objectMapper.createArrayNode();
        commerciants.sort(Comparator.comparing(Commerciant::getName));
        for (Commerciant commerciant : commerciants) {
            ObjectNode commerciantNode = objectMapper.createObjectNode();
            commerciantNode.put("commerciant", commerciant.getName());
            commerciantNode.put("total", commerciant.getAmount());
            commerciantsArray.add(commerciantNode);
        }
        report.set("commerciants", commerciantsArray);
        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "spendingsReport");
        commandOutput.set("output", report);
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }
}
