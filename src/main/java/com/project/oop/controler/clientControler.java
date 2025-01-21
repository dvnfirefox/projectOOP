package com.project.oop.controler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.oop.model.Account;
import com.project.oop.model.Transaction;
import com.project.oop.repository.AccountRepository;
import com.project.oop.service.AccountService;
import com.project.oop.service.TransactionService;
import com.project.oop.tools.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class clientControler {
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/accountlisting")
    @ResponseBody
    public String accountlisting(@RequestParam String clientCode) {
        return accountService.accountList(clientCode);
    }
    @PostMapping("/accountinfo")
    @ResponseBody
    public String accountInfo(@RequestBody String accountId) {
        return accountService.getAccount(accountId);
    }
    @PostMapping("/deposit")
    @ResponseBody
    public String deposit(@RequestBody String json) {return accountService.deposit(json);}
    @PostMapping("/withdraw")
    @ResponseBody
    public String withdraw(@RequestBody String json) {return accountService.withdraw(json);}
    @PostMapping("/pay")
    @ResponseBody
    public String pay(@RequestBody String json) {return accountService.pay(json);}
    @PostMapping("/transactionListing")
    @ResponseBody
    public String transactionListing(@RequestBody String accountId) {
        return transactionService.ListTransactions(accountId);
    }
    @PostMapping("/transfer")
    @ResponseBody
    public String transfer(@RequestBody String json) {return accountService.transfer(json);}
}
