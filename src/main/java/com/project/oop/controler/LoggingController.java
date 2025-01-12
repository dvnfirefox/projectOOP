package com.project.oop.controler;

import com.project.oop.model.Client;
import com.project.oop.repository.ClientRepository;
import com.project.oop.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoggingController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/test")
    public String test() {
        return "test";
    }
    @GetMapping("/getAllTask")
    public List<Client> listAll (){
        return clientRepository.findAll();
    }
    @PostMapping("/logging")
    public String logging(@RequestBody String json ){
         String clientConnected = clientService.loggingClient(json);
         return clientConnected;
    }
    @GetMapping("/setup")
    public String setup() {
        String jsonString = "{\"code\":\"admin\",\"name\":\"admin\",\"lastName\":\"admin\",\"phoneNumber\":\"admin\",\"email\":\"admin\",\"nip\":\"admin\",\"admin\":true}";
        return clientService.createClient(jsonString);
    }



}
