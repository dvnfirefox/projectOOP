package com.project.oop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Account {
    @Id
    @GeneratedValue
    public Long id;

    public String type;
    public String balance;
    public String clientCode;

    public Account(){}

    public Account(String type, String balance, String clientCode) {
        this.type = type;
        this.balance = balance;
        this.clientCode = clientCode;
    }
}
