package com.project.oop.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.oop.model.Atm;
import com.project.oop.repository.AtmRepository;
import com.project.oop.tools.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AtmService {
    @Autowired
    private AtmRepository atmRepository;

    public void setup(){
        atmRepository.save(new Atm(1,1000));
    }
    public boolean checkMoney(int amount){
        Optional<Atm> atm = atmRepository.findById("1");
        boolean result = false;
        if(atm.isPresent()){
           if(atm.get().getPaperMoney()<amount){
              result = true;
           }
        }
        return result;
    }
    public void withdrawPaper(int amount){
        Optional<Atm> atm = atmRepository.findById("1");
        if(atm.isPresent()){
            atm.get().setPaperMoney(atm.get().getPaperMoney()-amount);
            atmRepository.save(atm.get());
        }
    }
    public String AddPaper(int amount){
        Optional<Atm> atm = atmRepository.findById("1");
        ObjectNode response = Json.createNode();

        if(atm.isPresent()){
            if(atm.get().getPaperMoney() + amount >= 20000){
                response.put("statut", "Paper amount exceeds 20000");
            }else if(amount <= 0){
                response.put("statut", "need to deposit a least one paper money");
            } else if(amount%10 != 0){
                response.put("statut", "the amount to deposit must be by multiple of 10");
            } else{
                atm.get().setPaperMoney(atm.get().getPaperMoney() + amount);
                atmRepository.save(atm.get());
                String responseString = "Paper added successfully new total : " + atm.get().getPaperMoney();
                response.put("statut", responseString);


            }
        }
        return response.toString();
    }
}
