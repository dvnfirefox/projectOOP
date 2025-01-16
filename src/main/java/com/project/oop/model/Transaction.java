package com.project.oop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.Instant;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue
    public Long id;

    public String amount;
    public String newAmount;
    public String account;
    public Date date;
    public String info;

    public Transaction(){}

    public Transaction(String amount,String newAmount, String account, String info) {
        this.amount = amount;
        this.newAmount = newAmount;
        this.account = account;
        this.date = Date.from(Instant.now());
        this.info = info;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
