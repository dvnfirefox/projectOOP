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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @GetMapping("/clientcreation")
    public String clientcreation(){
        return clientService.createClient("1234", "maxime","laberge", "438", "email", "1234", true);

    }
    @PostMapping("/logging")
    public String logging(@RequestBody String jsonLogging ){
         String clientConnected = clientService.loggingClient(jsonLogging);
         return clientConnected;
    }


}
