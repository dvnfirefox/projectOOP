package com.project.oop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Transaction {
    @Id
    @GeneratedValue
    public Long id;
    public String amount;
    public String account;

    public Transaction(){}

    public Transaction(String amount, String account){
        this.amount = amount;
        this.account = account;
    }
}
