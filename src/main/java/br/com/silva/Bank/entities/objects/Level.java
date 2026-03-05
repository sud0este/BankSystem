package br.com.silva.Bank.entities.objects;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Level {
    private String name;
    private double price;
    private double maxMoney;
    private double maxIncome;
    private double percentageIncome;

    public Level(String name, double price, double maxMoney, double maxIncome, double percentageIncome) {
        this.name = name;
        this.price = price;
        this.maxMoney = maxMoney;
        this.maxIncome = maxIncome;
        this.percentageIncome = percentageIncome;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getMaxMoney() {
        return maxMoney;
    }

    public double getMaxIncome() {
        return maxIncome;
    }

    public double getPercentageIncome() {
        return percentageIncome;
    }
}
