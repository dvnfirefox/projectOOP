package com.project.oop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.oop.model.Account;
import com.project.oop.model.Client;
import com.project.oop.repository.AccountRepository;
import com.project.oop.tools.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Optional;

import static javax.swing.text.html.parser.DTDConstants.ID;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private HandlerMapping resourceHandlerMapping;
    @Autowired
    private TransactionService transactionService;

    public void createAccount(String type, String clientCode, String name) {
        accountRepository.save(new Account(type, 0, clientCode, name));
    }

    public String accountList(String code) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();

        Iterable<Account> accountListing = accountRepository.findAllByClientCode(code);

        accountListing.forEach(account -> {
            ObjectNode accountNode = mapper.createObjectNode();
            accountNode.put("type", account.getType());
            accountNode.put("id", account.getId());
            accountNode.put("name", account.getName());
            response.set(account.getId().toString(), accountNode);
        });

        return response.toString();
    }
    public String getAccount(String accountId) {
        Optional<Account> account = accountRepository.findById(Long.valueOf(accountId));
        ObjectNode response = Json.createNode();
        response.put("id", account.get().getId());
        response.put("type", account.get().getType());
        response.put("clientCode", account.get().getClientCode());
        response.put("name", account.get().getName());
        response.put("balance", account.get().getBalance());
        return response.toString();
    }
    public String deposit(String json){
        ObjectNode response = Json.createNode();
        try{
            JsonNode node = Json.toJson(json);
            int amount = (int) node.get("amount").asLong();
            String accountId = node.get("accountId").asText();
            if(amount < 0){
                response.put("statut", false);
                response.put("message", "cannot deposit number less than zero");
                return response.toString();
            }
            Optional<Account> account = accountRepository.findById(Long.valueOf(accountId));
            account.get().setBalance((amount + account.get().getBalance()));
            accountRepository.save(account.get());
            response.put("balance", account.get().getBalance());
            response.put("statut", true);
            String message = "deposit to " + account.get().getName() + " successful new balance: " + account.get().getBalance();
            response.put("message", message);
            transactionService.createTransaction(String.valueOf(amount), String.valueOf(account.get().getBalance()), "deposit", node.get("accountId").asText());
        }catch (Exception e){
            response.put("statut", false);
            response.put("message", "deposit failed:" + e.getMessage());
        }
        return response.toString();
    }
    public String withdraw(String json){
        ObjectNode response = Json.createNode();
        try{
            JsonNode node = Json.toJson(json);
            int amount = (int) node.get("amount").asLong();
            if(amount > 1000){
                response.put("message", "the maximum that can be withdrawn is 1000");
                return response.toString();
            }
            if(amount%10 != 0){
                response.put("message", "the amount to withdraw must be by multiple of 10");
                return response.toString();
            }
            String accountId = node.get("accountId").asText();
            if(amount < 0){
                response.put("statut", false);
                response.put("message", "cannot deposit number less than zero");
                return response.toString();
            }
            Optional<Account> account = accountRepository.findById(Long.valueOf(accountId));
            account.get().setBalance((account.get().getBalance() - amount ));
            accountRepository.save(account.get());
            response.put("balance", account.get().getBalance());
            response.put("statut", true);
            String message = "deposit to " + account.get().getName() + " successful new balance: " + account.get().getBalance();
            response.put("message", message);
            transactionService.createTransaction(String.valueOf(-amount), String.valueOf(account.get().getBalance()), "withdraw", node.get("accountId").asText());
        }catch (Exception e){
            response.put("statut", false);
            response.put("message", "deposit failed:" + e.getMessage());
        }
        return response.toString();
    }



}