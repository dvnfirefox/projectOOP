package com.project.oop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.oop.model.Transaction;
import com.project.oop.repository.TransactionRepository;
import com.project.oop.tools.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void createTransaction(String amount,String newAmount, String description, String accountId) {
        transactionRepository.save(new Transaction(amount,newAmount, accountId, description ));
    }
    public String ListTransactions(String accountId) {
        Iterable<Transaction> transactions = transactionRepository.findAllByAccount(accountId);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();

        transactions.forEach(transaction -> {
            ObjectNode transactionNode = mapper.createObjectNode();
            transactionNode.put("amount", transaction.getAmount());
            transactionNode.put("info",transaction.getInfo());
            transactionNode.put("newAmount", transaction.getNewAmount());
            transactionNode.put("date", transaction.getDate().toString());

            response.set(transaction.getId().toString(), transactionNode);
        });
        return response.toString();
    }
}
