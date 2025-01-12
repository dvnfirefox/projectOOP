package com.project.oop.controler;

import com.project.oop.model.Account;
import com.project.oop.repository.AccountRepository;
import com.project.oop.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class clientControler {
    @Autowired
    private AccountService accountService;


    @GetMapping("/accountlisting")
    @ResponseBody
    public String accountlisting(@RequestParam String clientCode) {
        return accountService.accountList(clientCode);
    }
}
