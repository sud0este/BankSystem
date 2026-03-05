package br.com.silva.Bank.menus;

import br.com.silva.Bank.BankPlugin;
import br.com.silva.Bank.economy.Economy;
import br.com.silva.Bank.entities.managers.LevelManager;
import br.com.silva.Bank.entities.objects.Account;
import br.com.silva.Bank.entities.objects.Level;
import br.com.silva.Bank.util.builders.ItemBuilder;
import br.com.silva.Bank.util.inventory.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class UpgradeMenu extends Menu {

    private final Account account;
    private final LevelManager levelManager;

    public UpgradeMenu(Account account) {
        super("Melhorias de Conta", 4);

        this.account = account;
        this.levelManager = BankPlugin.getInstance().getLevelManager();
    }

    @Override
    public void open(Player player) {
        Economy economy = BankPlugin.getInstance().getEconomy();

        if (economy == null) {
            player.sendMessage("§cSistema econômico não disponível!");
            return;
        }

        int slot = 11;
        for (Level level : levelManager.getLevels()) {
            setItem(
                    slot,
                    new ItemBuilder(getStatusIcon(account.getCurrentLevel(), level))
                            .name("§aConta " + level.getName())
                            .lore(Arrays.asList(
                                    "§7Muda o tipo de conta para §aConta " + level.getName(),
                                    " ",
                                    "§fRendimento máximo: §6" + level.getMaxIncome(),
                                    "§fSaldo máximo: §6" + level.getMaxMoney(),
                                    " ",
                                    "§6Custo:",
                                    "§7" + level.getPrice(),
                                    " ",
                                    getStatusMessage(account.getCurrentLevel(), level)))
                            .build(),
                    event -> {
                        account.upLevel(level);
                        new UpgradeMenu(account).open(player);
                    });

            slot++;
        }

        setItem(31, new ItemBuilder(Material.ARROW).name("§aVoltar").build(), event -> new BankMenu().open(player));

        super.open(player);
    }

    private String getStatusMessage(Level currentLevel, Level hoverLevel) {
        int currentLevelIndex = levelManager.getLevels().indexOf(currentLevel);
        int hoverLevelIndex = levelManager.getLevels().indexOf(hoverLevel);

        if (currentLevelIndex == hoverLevelIndex) {
            return "§eEste é seu nível atual.";
        }

        if (currentLevelIndex > hoverLevelIndex) {
            return "§cVocê já desbloqueou esse nível!";
        }

        return "§aClique para desbloquear!";
    }

    private ItemStack getStatusIcon(Level currentLevel, Level hoverLevel) {
        int currentLevelIndex = levelManager.getLevels().indexOf(currentLevel);
        int hoverLevelIndex = levelManager.getLevels().indexOf(hoverLevel);

        if (currentLevelIndex == hoverLevelIndex) {
            return new ItemBuilder(Material.YELLOW_STAINED_GLASS_PANE).build();
        }

        if (currentLevelIndex > hoverLevelIndex) {
            return new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).build();
        }

        return new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).build();
    }
}
