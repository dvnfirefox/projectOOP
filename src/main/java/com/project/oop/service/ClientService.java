package com.project.oop.service;

import com.project.oop.model.Client;
import com.project.oop.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public String createClient(
            String firstName,
            String lastName,
            String phoneNumber,
            String email,
            String pin) {
        clientRepository.save(new Client(firstName, lastName, phoneNumber, email, pin));
        return "success";
    }
}
