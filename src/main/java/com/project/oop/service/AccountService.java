package com.project.oop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.Objects;
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
    @Autowired
    private AtmService atmService;

    public String createAccount(String type, String clientCode, String name) {
        if(Objects.equals(type, "4")){
          try{
              Optional<Account> account = accountRepository.findByClientCodeAndType(clientCode, type);
              if(account.isPresent()){
                  return "only one credit line is permitted per client";
              }
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
        }

        accountRepository.save(new Account(type, 0, clientCode, name));
        String response = "success." + name + " created";
        return response;
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
            double amount = node.get("amount").asDouble();
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
            long amount = node.get("amount").asLong();
            String accountId = node.get("accountId").asText();
            if(amount > 1000){
                response.put("message", "the maximum that can be withdrawn is 1000");
                return response.toString();
            }else if(amount%10 != 0){
                response.put("message", "the amount to withdraw must be by multiple of 10");
                return response.toString();
            }else if(amount < 0){
                response.put("statut", false);
                response.put("message", "cannot withdraw less than 10$");
                return response.toString();
            }else if(atmService.checkMoney((int) amount)){
                response.put("statut", false);
                response.put("message", "Atm does not have enough paper money");
                return response.toString();
            }
            Optional<Account> account = accountRepository.findById(Long.valueOf(accountId));
            double balance = account.get().getBalance() - amount;
            if(balance < 0){
                Optional<Account> credit = accountRepository.findByClientCodeAndType(account.get().clientCode, "4");
                if(account.isPresent()){
                    account.get().setBalance(0);
                    balance = -balance;
                    credit.get().setBalance(balance + credit.get().getBalance());
                    accountRepository.save(account.get());
                    accountRepository.save(credit.get());
                    response.put("balance", account.get().getBalance());
                    response.put("statut", true);
                    String message = "witdraw from " + account.get().getName() + " successful new balance: " + account.get().getBalance() + "credit balance: " + credit.get().getBalance();
                    response.put("message", message);
                    atmService.withdrawPaper((int) amount);
                    transactionService.createTransaction(String.valueOf(account.get().getBalance()), "0", "withdraw", node.get("accountId").asText());
                    transactionService.createTransaction(String.valueOf(-balance), String.valueOf(credit.get().getBalance()), "withdraw from insuficiant fund", credit.get().id.toString());



                }else{
                    response.put("statut", false);
                    response.put("message", "not enough fund");
                }
            }else{
                account.get().setBalance(balance);
                accountRepository.save(account.get());
                response.put("balance", balance);
                response.put("statut", true);
                String message = "withdraw from " + account.get().getName() + " successful new balance: " + balance;
                response.put("message", message);
                atmService.withdrawPaper((int) amount);
                transactionService.createTransaction(String.valueOf(-amount), String.valueOf(account.get().getBalance()), "withdraw", node.get("accountId").asText());
            }


        }catch (Exception e){
            response.put("statut", false);
            response.put("message", "transaction failed:" + e.getMessage());
        }
        return response.toString();
    }
    public String pay(String json){
        ObjectNode response = Json.createNode();
        try{
            JsonNode node = Json.toJson(json);
            double amount = node.get("amount").asLong() + 1.25;
            String accountId = node.get("accountId").asText();
            if(amount < 0){
                response.put("statut", false);
                response.put("message", "cannot pay a negative amount");
                return response.toString();
            }
            Optional<Account> account = accountRepository.findById(Long.valueOf(accountId));
            double balance = account.get().getBalance() - amount;
            if(balance < 0){
                Optional<Account> credit = accountRepository.findByClientCodeAndType(account.get().clientCode, "4");
                if(account.isPresent()){
                    account.get().setBalance(0);
                    balance = -balance;
                    credit.get().setBalance(balance + credit.get().getBalance());
                    accountRepository.save(account.get());
                    accountRepository.save(credit.get());
                    response.put("balance", account.get().getBalance());
                    response.put("statut", true);
                    String message = "pay to " + node.get("info") + " successful new balance: " + account.get().getBalance() + " credit balance: " + credit.get().getBalance();
                    response.put("message", message);
                    transactionService.createTransaction(String.valueOf(account.get().getBalance()), "0", String.valueOf(node.get("info")), node.get("accountId").asText());
                    transactionService.createTransaction(String.valueOf(-balance), String.valueOf(credit.get().getBalance()), "withdraw from insufficient fund", credit.get().id.toString());



                }else{
                    response.put("statut", false);
                    response.put("message", "not enough fund");
                }
            }else{
                account.get().setBalance(balance);
                accountRepository.save(account.get());
                response.put("balance", balance);
                response.put("statut", true);
                String message = "pay to " + node.get("info") + " successful new balance: " + balance;
                response.put("message", message);
                transactionService.createTransaction(String.valueOf(-amount), String.valueOf(account.get().getBalance()), String.valueOf(node.get("info")), node.get("accountId").asText());
            }


        }catch (Exception e){
            response.put("statut", false);
            response.put("message", "transaction failed:" + e.getMessage());
        }
        return response.toString();
    }
    public String Interest(String type){
        Iterable<Account> accountListing = accountRepository.findAllByType(type);
        double interest;
        if(Objects.equals(type, "2")){
            interest = 1.01;
        }else{
            interest = 1.05;
        }
        accountListing.forEach(account -> {
            double newBalance = Math.round((account.getBalance() * interest) * 100) / 100.0;
            double amount = Math.round( (newBalance - account.getBalance()) * 100) / 100.0;
            account.setBalance(newBalance);
            accountRepository.save(account);
            transactionService.createTransaction(String.valueOf(amount), String.valueOf(newBalance), "interest",account.getId().toString() );
        });
        return "success";
    }

    public String transfer(String json){
        ObjectNode response = Json.createNode();
        try{
            JsonNode node = Json.toJson(json);
            String accountoutId = node.get("accountOut").asText();
            String accountInId = node.get("accountIn").asText();
            double amount = node.get("amount").asDouble();
            Optional<Account> accountOut = accountRepository.findById(Long.valueOf(accountoutId));
            Optional<Account> accountIn = accountRepository.findById(Long.valueOf(accountInId));
            if(accountOut.isPresent() && accountIn.isPresent()){
                if(accountOut.get().getBalance() < amount){
                    response.put("message", "cannot transfer insufficient fund");
                    return response.toString();
                }
                accountOut.get().setBalance(accountOut.get().getBalance() - amount);
                accountRepository.save(accountOut.get());
                transactionService.createTransaction(String.valueOf(-amount), String.valueOf(accountOut.get().getBalance()), "transfer", accountOut.get().id.toString());
                if(Objects.equals(accountIn.get().getType(), "4")){
                    accountIn.get().setBalance(accountIn.get().getBalance() - amount);
                }else{
                    accountIn.get().setBalance(accountIn.get().getBalance() + amount);
                }

                accountRepository.save(accountIn.get());
                transactionService.createTransaction(String.valueOf(amount), String.valueOf(accountIn.get().getBalance()), "transfer", accountIn.get().id.toString());
                String message = "checking New balance : " + accountOut.get().getBalance() + " " + accountIn.get().getName() + " new balance: " + accountIn.get().getBalance();
                response.put("message", message);
            }

        }catch (Exception e){
            response.put("statut", false);
            response.put("message", "transaction failed:" + e.getMessage());
        }
        return response.toString();
    }



}