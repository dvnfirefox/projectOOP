package com.project.oop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Atm {
    @Id
    private int id;
    private double paperMoney;
    public Atm(){}

    public Atm(int id, double paperMoney) {
        this.id = id;
        this.paperMoney = paperMoney;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPaperMoney() {
        return paperMoney;
    }

    public void setPaperMoney(double paperMoney) {
        this.paperMoney = paperMoney;
    }
}
