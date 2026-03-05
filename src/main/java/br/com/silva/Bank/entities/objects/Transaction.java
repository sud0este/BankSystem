package br.com.silva.Bank.entities.objects;

import lombok.Builder;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
public class Transaction {
    private TransactionType type;
    private Double amount;
    private long createdTime;

    public Transaction(TransactionType type, Double amount, long createdTime) {
        this.type = type;
        this.amount = amount;
        this.createdTime = createdTime;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public String calculateTime() {
        long currentSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - createdTime);

        int day = (int)TimeUnit.SECONDS.toDays(currentSeconds);
        long hours = TimeUnit.SECONDS.toHours(currentSeconds) - (day *24);
        long minutes = TimeUnit.SECONDS.toMinutes(currentSeconds) - (TimeUnit.SECONDS.toHours(currentSeconds)* 60);
        long seconds = TimeUnit.SECONDS.toSeconds(currentSeconds) - (TimeUnit.SECONDS.toMinutes(currentSeconds) *60);

        if (day != 0) return day + " dias atrás";
        if (hours != 0) return hours + " horas atrás";
        if (minutes != 0) return minutes + " minutos atrás";
        if (seconds != 0) return seconds + " segundos atrás";

        return "0 segundos atrás";
    }

    public String serialize() {
        if (type == TransactionType.WITHDRAW) {
            return "§c- " + amount + "§e, " + calculateTime();
        } else {
            return "§a+ " + amount + "§e, " + calculateTime();
        }
    }
}
