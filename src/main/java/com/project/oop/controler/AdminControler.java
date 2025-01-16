package com.project.oop.controler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.oop.repository.ClientRepository;
import com.project.oop.service.AccountService;
import com.project.oop.service.ClientService;
import com.project.oop.tools.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminControler {
    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clientcreation")
    public String clientcreation(@RequestBody String json){
        return clientService.createClient(json);
    }

    @GetMapping("/clientsearch")
    public String clientSearch(){
        return clientService.clientList();
    }

    @PostMapping("/accountcreation")
    public String AccountCreation(@RequestBody String json){
        ObjectNode response = Json.createNode();
        try{
            JsonNode node = Json.toJson(json);
            String type = node.get("type").asText();
            String id = node.get("code").asText();
            String name = node.get("name").asText();
            accountService.createAccount(type, id, name);
            response.put("message", "account created");
        }catch (Exception e){
            response.put("message", "account creation failed:" + e.getMessage());
        }
        return response.toString();
    }
}
