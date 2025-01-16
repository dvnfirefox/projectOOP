package com.project.oop.service;

import com.fasterxml.jackson.databind.JsonNode;
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
}
