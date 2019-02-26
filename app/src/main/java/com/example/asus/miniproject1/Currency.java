package com.example.asus.miniproject1;

/**
 * .
 */
public class Currency {

    String name;
    String country;
    double perUnit;
    double totalAmount;

    public Currency(String name, double perUnit, double amount) {
        this.name = name;
        this.country = country;
        this.perUnit = perUnit;
        this.totalAmount = amount * perUnit;
    }

    public String getName() {
        return name;
    }
    public String getCountry(){ return country;}

    public double getPerUnit() {
        return perUnit;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

}
