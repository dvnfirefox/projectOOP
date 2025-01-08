package com.project.oop.controler;

import com.project.oop.repository.ClientRepository;
import com.project.oop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminControler {
    @Autowired
    private ClientService clientService;

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
}
