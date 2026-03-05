package me.hugosrc.Bank.entities.managers;

import me.hugosrc.Bank.BankPlugin;
import me.hugosrc.Bank.entities.objects.Level;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private final ConfigurationSection section;
    private final List<Level> levels;

    public LevelManager() {
        this.section = BankPlugin.getInstance().getConfig().getConfigurationSection("bank_account_levels");
        this.levels = new ArrayList<>();

        loadLevels();
    }

    private void loadLevels() {
        section.getKeys(false).forEach(level -> {
            String name = section.getString(level + ".name");
            double price = section.getDouble(level + ".price");
            double max_money = section.getDouble(level + ".max_money");
            double max_income = section.getDouble(level + ".max_income");
            double percentage_income = section.getDouble(level + ".percentage_income");

            levels.add(new Level(
                    name,
                    price,
                    max_money,
                    max_income,
                    percentage_income
            ));
        });

        BankPlugin.logger.info(levels.size() + " levels were loaded for bank accounts.");
    }

    public List<Level> getLevels() {
        return levels;
    }
}
