package br.com.silva.Bank.tasks;

import br.com.silva.Bank.BankPlugin;
import org.bukkit.Bukkit;

public class BankAutoSaveTask {

    private final long autosaveTime = BankPlugin.getInstance().getConfig().getLong("autosave") * 20;

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(BankPlugin.getInstance(), () -> {
            BankPlugin.getInstance().getBank().saveAll();
            BankPlugin.logger.info("Saved data in the database.");
        }, autosaveTime, autosaveTime);
    }

}
