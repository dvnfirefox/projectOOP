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

    public String createClient(
            String code,
            String firstName,
            String lastName,
            String phoneNumber,
            String email,
            String pin,
            boolean admin) {
        clientRepository.save(new Client(code, firstName, lastName, phoneNumber, email, pin, admin));
        return "success";
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
                response.put("Message", "Client not found");
            } else if (Objects.equals(clientConnect.get().getPin(), nip)) {
                response.put("Message", "Sucessfully logged in");
                response.put("id", clientConnect.get().getId());
                response.put("admin", clientConnect.get().getAdmin());
            } else {
                response.put("Message", "Pin Incorrect");
            }
            return response.toString();
        } catch (Exception e) {
            // Handle parsing errors
            return "Error parsing input: " + e.getMessage();
        }
    }
}
