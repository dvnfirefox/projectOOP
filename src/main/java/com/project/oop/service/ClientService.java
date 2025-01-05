package com.project.oop.service;

import com.project.oop.model.Client;
import com.project.oop.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        try {
            Map<String, String> fields = Stream.of(json.split("&"))
                    .map(entry -> entry.split("="))
                    .collect(Collectors.toMap(
                            entry -> entry[0],  // Key
                            entry -> entry[1]   // Value
                    ));
            code = fields.get("Code");
            nip = fields.get("NIP");
            Optional<Client> clientConnect = clientRepository.findByCode(code);
            System.out.println(clientConnect.get().getCode());
            System.out.println(nip);
            if (Objects.equals(clientConnect.get().getPin(), nip)) {
                return "success";
            } else {
                return"pin doesnt match code";
            }


        } catch (Exception e) {
            // Handle parsing errors
            return "Error parsing input: " + e.getMessage();
        }

    }
}
