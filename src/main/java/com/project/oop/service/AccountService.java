package com.project.oop.service;

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

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private HandlerMapping resourceHandlerMapping;

    public void createAccount(String type, String clientCode) {
        accountRepository.save(new Account(type, "0", clientCode));
    }

    public String accountList(String code) {
        ObjectNode response = Json.createNode();
        Optional<Account> accountListing = accountRepository.findAllByClientCode(code);
        for (accountListing.get().id:
             ) {
            response.put(String.valueOf(accountListing.get().id), accountListing.get().type);
        }
        response.put(String.valueOf(accountListing.get().id), accountListing.get().type);

        return response.toString();
    }
}