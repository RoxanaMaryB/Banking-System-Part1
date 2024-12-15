package org.poo.commands.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Account;
import org.poo.bank.Commerciant;
import org.poo.bank.Transaction;
import org.poo.bank.User;

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
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "spendingsReport");
            noAccountFound(output, objectMapper, commandOutput);
            return;
        }
        User user = account.getUser();
        if (user == null) {
            ObjectNode commandOutput = objectMapper.createObjectNode();
            commandOutput.put("command", "spendingsReport");
            noUserFound(output, objectMapper, commandOutput);
            return;
        }
        ObjectNode commandOutput = objectMapper.createObjectNode();
        commandOutput.put("command", "spendingsReport");
        if(account.getType().equals("savings")) {
            ObjectNode result = objectMapper.createObjectNode();
            result.put("error", "This kind of report is not supported for a saving account");
            commandOutput.set("output", result);
        } else {
            commandOutput.set("output", formReport(objectMapper, account, user.getTransactions()));
        }
        commandOutput.put("timestamp", timestamp);
        output.add(commandOutput);
    }

    public boolean checkValidTransaction(Transaction transaction) {
        return super.checkValidTransaction(transaction) &&
                transaction.getDescription().equals("Card payment");
    }

    @Override
    public ObjectNode formReport(ObjectMapper objectMapper, Account account, List<Transaction> transactions) {
        ObjectNode report = super.formReport(objectMapper, account, transactions);
        for (Transaction transaction : transactions) {
            if (checkValidTransaction(transaction)) {
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
        ArrayNode commerciantsArray = objectMapper.createArrayNode();
        commerciants.sort(Comparator.comparing(Commerciant::getName));
        for (Commerciant commerciant : commerciants) {
            ObjectNode commerciantNode = objectMapper.createObjectNode();
            commerciantNode.put("commerciant", commerciant.getName());
            commerciantNode.put("total", commerciant.getAmount());
            commerciantsArray.add(commerciantNode);
        }
        report.set("commerciants", commerciantsArray);
        return report;
    }
}
