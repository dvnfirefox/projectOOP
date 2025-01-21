package com.project.oop.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.oop.tools.Json;
import com.project.oop.model.Client;
import com.project.oop.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    public AccountService accountService;


    public String clientList() {
        ObjectNode response = Json.createNode();
        clientRepository.findAll().forEach(client ->
                response.put(client.getCode(), client.getCode())
        );
        return response.toString();
    }


    public String createClient(String json) {
        ObjectNode response = Json.createNode();
        try{
            JsonNode node = Json.toJson(json);
            String code = node.get("code").asText();
            String firstName = node.get("name").asText();
            String lastName = node.get("lastName").asText();
            String phoneNumber = node.get("phoneNumber").asText();
            String email = node.get("email").asText();
            String pin = node.get("nip").asText();
            boolean admin = node.get("admin").asBoolean();
            if(clientRepository.findByCode(code).isPresent()){
                response.put("message", "Client already exists");
                response.put("created", Boolean.FALSE);
            } else if (pin.length() != 4) {
                response.put("message", "pin need to be 4 digits");
                response.put("created", Boolean.FALSE);
            } else {
                clientRepository.save(new Client(code, firstName, lastName, phoneNumber, email, pin, admin));
                String message =  accountService.createAccount("1", code, "Main checking");
                response.put("message", message);
                response.put("created", Boolean.TRUE);
            }

        } catch (Exception E){
            response.put("message", "Error parsing input: " + E.getMessage());
            response.put("created", Boolean.FALSE);
        }

        return response.toString();
    }

    public String loggingClient(String json){
        String code;
        String nip;
        ObjectNode response = Json.createNode();
        try {
            JsonNode node = Json.toJson(json);
            code = node.get("Code").asText();
            nip = node.get("NIP").asText();
            Optional<Client> clientConnect = clientRepository.findByCode(code);
            if (clientConnect.isEmpty()) {
                response.put("message", "Client not found");
            } else if (Objects.equals(clientConnect.get().getPin(), nip)) {
                int attempts = clientConnect.get().getAttempts();
                if(attempts > 3){
                    response.put("message", "too many attempts youre now blocked, please contact your administrator");
                    return response.toString();
                }
                clientConnect.get().setAttempts(0);
                clientRepository.save(clientConnect.get());
                response.put("message", "Sucessfully logged in");
                response.put("id", clientConnect.get().getCode());
                response.put("admin", clientConnect.get().getAdmin());
            } else {
                int attempts = clientConnect.get().getAttempts();
                attempts += 1;
                if(attempts > 3){
                    clientConnect.get().setAttempts(attempts);
                    clientRepository.save(clientConnect.get());
                    response.put("message", "too many attempts youre now blocked, please contact your administrator");
                }else{
                    clientConnect.get().setAttempts(attempts);
                    clientRepository.save(clientConnect.get());
                    int attemptsRemaining = 4 - attempts;
                    response.put("message", "pin incorrect at youre third attempt youre blocked. attemp remaining : " + attemptsRemaining);
                }

            }
            return response.toString();
        } catch (Exception e) {
            // Handle parsing errors
            return "Error parsing input: " + e.getMessage();
        }
    }
    public String unblockClient(String code){
        try{
            Optional<Client> clientConnect = clientRepository.findByCode(code);
            clientConnect.get().setAttempts(0);
            clientRepository.save(clientConnect.get());
            return "sucessfully unblocked";
        }catch (Exception e) {
            return "Error parsing input: " + e.getMessage();
        }
    }
}
