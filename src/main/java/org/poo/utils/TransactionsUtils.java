package org.poo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bank.Transaction;

public final class TransactionsUtils {
    private TransactionsUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Create a JSON object with the transaction information
     * @param objectMapper
     * @param transaction
     * @return
     */
    public static ObjectNode createTransactionNode(final ObjectMapper objectMapper,
                                                   final Transaction transaction) {
        ObjectNode transactionNode = objectMapper.createObjectNode();
        transactionNode.put("timestamp", transaction.getTimestamp());
        transactionNode.put("description", transaction.getDescription());
        transaction.addField(transactionNode, "account", transaction.getAccountIBAN());
        transaction.addField(transactionNode, "senderIBAN", transaction.getSenderIBAN());
        transaction.addField(transactionNode, "receiverIBAN", transaction.getReceiverIBAN());
        transaction.addField(transactionNode, "card", transaction.getCard());
        transaction.addField(transactionNode, "cardHolder", transaction.getCardHolder());
        transaction.addField(transactionNode, "amount", transaction.getAmount());
        transaction.addDoubleField(transactionNode, "amount", transaction.getAmountDouble());
        transaction.addField(transactionNode, "currency", transaction.getCurrency());
        transaction.addField(transactionNode, "transferType", transaction.getTransferType());
        transaction.addField(transactionNode, "commerciant", transaction.getCommerciant());
        transaction.addListOfStrings(transactionNode, "involvedAccounts",
                transaction.getInvolvedAccounts());
        transaction.addField(transactionNode, "error", transaction.getError());
        return transactionNode;
    }
}
