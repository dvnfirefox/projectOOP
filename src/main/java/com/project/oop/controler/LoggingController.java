package com.project.oop.controler;

import com.project.oop.model.Client;
import com.project.oop.repository.ClientRepository;
import com.project.oop.service.AtmService;
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
    @Autowired
    private AtmService atmService;

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
        String adminString = "{\"code\":\"admin\",\"name\":\"admin\",\"lastName\":\"admin\",\"phoneNumber\":\"admin\",\"email\":\"admin\",\"nip\":\"1234\",\"admin\":true}";
        clientService.createClient(adminString);
        atmService.setup();
        String clientString = "{\"code\":\"first Client\",\"name\":\"Maxime\",\"lastName\":\"Laberge\",\"phoneNumber\":\"438-395-4369\",\"email\":\"test@gmail.com\",\"nip\":\"1234\",\"admin\":false}";
        return clientService.createClient(clientString);
    }
    @PostMapping("/unblock")
    public String unblock(@RequestBody String code ){
        return clientService.unblockClient(code);
    }



}
