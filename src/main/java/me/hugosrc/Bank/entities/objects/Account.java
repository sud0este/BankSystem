package me.hugosrc.Bank.entities.objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.hugosrc.Bank.BankPlugin;
import me.hugosrc.Bank.economy.Economy;
import me.hugosrc.Bank.entities.managers.LevelManager;
import me.hugosrc.Bank.menus.DepositMenu;
import me.hugosrc.Bank.menus.UpgradeMenu;
import me.hugosrc.Bank.menus.WithdrawMenu;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@Setter
@Builder
public class Account {

    private Player owner;
    private double balance;
    private Level currentLevel;
    private List<Transaction> lastTransactions;
    private long timeToIncome;

    public Account(Player owner, double balance, long timeToIncome, Level currentLevel, List<Transaction> lastTransactions) {
        this.owner = owner;
        this.balance = balance;
        this.timeToIncome = timeToIncome;
        this.currentLevel = currentLevel;
        this.lastTransactions = lastTransactions;
    }

    // Getters
    public Player getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public List<Transaction> getLastTransactions() {
        return lastTransactions;
    }

    public long getTimeToIncome() {
        return timeToIncome;
    }

    // Setters
    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setLastTransactions(List<Transaction> lastTransactions) {
        this.lastTransactions = lastTransactions;
    }

    public void setTimeToIncome(long timeToIncome) {
        this.timeToIncome = timeToIncome;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void deposit(double amount) {
        Economy economy = BankPlugin.getInstance().getEconomy();

        if (economy.getBalance(owner) == 0) {
            owner.sendMessage("§cVocê não possui saldo em sua carteira.");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        if (balance + amount > currentLevel.getMaxMoney()) {
            owner.sendMessage("§cSua conta tem o limite máximo de " + currentLevel.getMaxMoney() + " coins!");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        if (amount > economy.getBalance(owner)) {
            owner.sendMessage("§cVocê não possui esse saldo, escolha outro valor.");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        economy.withdrawPlayer(owner, amount);
        balance += amount;

        playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        owner.sendMessage("§aVocê depositou " + amount + " coins no banco.");
        registerTransaction(TransactionType.DEPOSIT, amount);
        new DepositMenu(this).open(owner);
    }

    public void withdraw(double amount) {
        Economy economy = BankPlugin.getInstance().getEconomy();

        if (balance == 0) {
            owner.sendMessage("§cVocê não possui saldo para retirar.");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        if (amount > balance) {
            owner.sendMessage("§cVocê não possui saldo suficiente para realizar este saque.");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        balance -= amount;
        economy.depositPlayer(owner, amount);

        playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        owner.sendMessage("§aVocê retirou " + amount + " coins do banco.");
        registerTransaction(TransactionType.WITHDRAW, amount);
        new WithdrawMenu(this).open(owner);
    }

    public void upLevel(Level level) {
        Economy economy = BankPlugin.getInstance().getEconomy();
        LevelManager levelManager = BankPlugin.getInstance().getLevelManager();

        int currentLevelIndex = levelManager.getLevels().indexOf(currentLevel);
        int nextLevelIndex = currentLevelIndex + 1;
        int chosenLevelIndex = levelManager.getLevels().indexOf(level);

        if (chosenLevelIndex <= currentLevelIndex) {
            owner.sendMessage("§cVocê já havia desbloqueado este nível.");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        if (nextLevelIndex == levelManager.getLevels().size()) {
            owner.sendMessage("§cVocê já atingiu o nível máximo.");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        Level nextLevel = levelManager.getLevels().get(nextLevelIndex);

        if (!level.equals(nextLevel)) {
            owner.sendMessage("§cVocê não desbloqueou o nível anterior.");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        if (economy.getBalance(owner) < nextLevel.getPrice()) {
            owner.sendMessage("§cVocê não possui coins suficientes");
            playSound(Sound.ENTITY_VILLAGER_NO);
            return;
        }

        currentLevel = nextLevel;
        economy.withdrawPlayer(owner, nextLevel.getPrice());

        playSound(Sound.ENTITY_PLAYER_LEVELUP);
        owner.sendMessage("§aVocê melhorou sua conta para o nível: " + currentLevel.getName());
        new UpgradeMenu(this).open(owner);
    }

    public double getProjection() {
        double projection = (currentLevel.getPercentageIncome() * balance) / 100;

        return Math.min(projection, currentLevel.getMaxIncome());
    }

    private void registerTransaction(TransactionType type, double amount) {
        Transaction transaction = new Transaction(
                type,
                amount,
                System.currentTimeMillis()
        );

        if (lastTransactions.size() == 10) {
            lastTransactions.remove(lastTransactions.size() - 1);
            lastTransactions.add(0, transaction);
            return;
        }

        lastTransactions.add(0, transaction);
    }

    private void playSound(Sound sound) {
        owner.playSound(owner.getLocation(), sound, 1f, 1f);
    }

}
