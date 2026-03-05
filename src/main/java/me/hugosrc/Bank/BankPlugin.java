package me.hugosrc.Bank;

import lombok.Getter;
import me.hugosrc.Bank.commands.CreateNpcCommand;
import me.hugosrc.Bank.storage.mysql.MySQLDatabase;
import me.hugosrc.Bank.storage.mysql.MySQLDatabase.Credentials;
import me.hugosrc.Bank.entities.managers.LevelManager;
import me.hugosrc.Bank.listeners.NPCClick;
import me.hugosrc.Bank.listeners.PlayerQuit;
import me.hugosrc.Bank.listeners.ChatListener;
import me.hugosrc.Bank.tasks.BankAutoSaveTask;
import me.hugosrc.Bank.tasks.BankIncomeTask;
import me.hugosrc.Bank.util.inventory.MenuListener;
import me.hugosrc.Bank.economy.Economy;
import me.hugosrc.Bank.economy.EconomyProvider;
import me.hugosrc.Bank.economy.VaultEconomyProvider;
import me.hugosrc.Bank.entities.objects.Bank;
import me.hugosrc.Bank.entities.objects.Account;
import me.hugosrc.Bank.listeners.PlayerJoin;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@Getter
public class BankPlugin extends JavaPlugin {

    public static BankPlugin instance;
    public static Logger logger;

    private final EconomyProvider economyProvider = new VaultEconomyProvider();
    private Economy economy;
    private LevelManager levelManager;
    private MySQLDatabase mySQLDatabase;
    private Bank bank;

    private final Credentials credentials = new Credentials(
            getConfig().getString("database.host"),
            getConfig().getInt("database.port"),
            getConfig().getString("database.user"),
            getConfig().getString("database.password"),
            getConfig().getString("database.name")
    );

    private final Map<UUID, Account> depositExpecters = new HashMap<>();
    private final Map<UUID, Account> withdrawExpecters = new HashMap<>();

    @Override
    public void onEnable() {
        loadConfig();
        loadInstances();

        if (economy == null) {
            getLogger().severe("Vault não encontrado! Tentando novamente em 5 segundos...");
            getServer().getScheduler().runTaskLater(this, () -> {
                economy = economyProvider.getEconomy();
                if (economy != null) {
                    getLogger().info("Vault encontrado na segunda tentativa!");
                } else {
                    getLogger().severe("Vault ainda não encontrado! O plugin funcionará sem recursos econômicos.");
                }
            }, 100L); // 5 segundos
        }

        registerCommands();
    }

    @Override
    public void onDisable() {
        bank.saveAll();
        mySQLDatabase.disconnect();
    }

    private void loadInstances() {
        instance = this;
        logger = getLogger();

        levelManager = new LevelManager();
        economy = economyProvider.getEconomy();
        mySQLDatabase = new MySQLDatabase(credentials);
        bank = new Bank();

        registerListeners(
                new MenuListener(),
                new PlayerJoin(),
                new PlayerQuit(),
                new NPCClick(),
                new ChatListener());

        new BankAutoSaveTask().start();
        new BankIncomeTask().start();
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    private void registerCommands() {
        getCommand("bank").setExecutor(new CreateNpcCommand());
    }

    public static BankPlugin getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return economy;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public Bank getBank() {
        return bank;
    }

    public Map<UUID, Account> getDepositExpecters() {
        return depositExpecters;
    }

    public Map<UUID, Account> getWithdrawExpecters() {
        return withdrawExpecters;
    }
}
