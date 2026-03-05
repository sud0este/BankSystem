package br.com.silva.Bank.menus;

import br.com.silva.Bank.BankPlugin;
import br.com.silva.Bank.util.inventory.Menu;
import br.com.silva.Bank.util.builders.ItemBuilder;
import br.com.silva.Bank.economy.Economy;
import br.com.silva.Bank.entities.objects.Account;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class WithdrawMenu extends Menu {

    private final Account account;

    public WithdrawMenu(Account account) {
        super("Escolha um valor", 4);

        this.account = account;
    }

    @Override
    public void open(Player player) {
        Economy economy = BankPlugin.getInstance().getEconomy();

        if (economy == null) {
            player.sendMessage("§cSistema econômico não disponível!");
            return;
        }

        setItem(
                10,
                new ItemBuilder(Material.DISPENSER, 64)
                        .name("§aTudo")
                        .lore(Arrays.asList(
                                "§8Saque Bancário",
                                " ",
                                "§fSaldo atual: §a" + account.getBalance(),
                                "§fValor a retirar: §a" + account.getBalance(),
                                " ",
                                "§eClique para retirar!"))
                        .build(),
                event -> account.withdraw(account.getBalance()));

        setItem(
                12,
                new ItemBuilder(Material.DISPENSER, 32)
                        .name("§aMetade")
                        .lore(Arrays.asList(
                                "§8Saque Bancário",
                                " ",
                                "§fSaldo atual: §a" + account.getBalance(),
                                "§fValor a retirar: §a" + account.getBalance() / 2,
                                " ",
                                "§eClique para retirar!"))
                        .build(),
                event -> account.withdraw(account.getBalance() / 2));

        setItem(
                14,
                new ItemBuilder(Material.DISPENSER)
                        .name("§a20%")
                        .lore(Arrays.asList(
                                "§8Saque Bancário",
                                " ",
                                "§fSaldo atual: §a" + account.getBalance(),
                                "§fValor a retirar: §a" + (account.getBalance() * 20) / 100,
                                " ",
                                "§eClique para retirar!"))
                        .build(),
                event -> account.withdraw((account.getBalance() * 20) / 100));

        setItem(
                16,
                new ItemBuilder(Material.DISPENSER)
                        .name("§aDefinir valor")
                        .lore(Arrays.asList(
                                "§8Saque Bancário",
                                " ",
                                "§fSaldo atual: §a" + account.getBalance(),
                                " ",
                                "§eClique para definir o valor!"))
                        .build(),
                event -> {
                    BankPlugin.getInstance().getWithdrawExpecters().put(player.getUniqueId(), account);
                    player.sendMessage("§aDigite o valor que deseja sacar no chat:");
                    player.closeInventory();
                });

        setItem(
                31,
                new ItemBuilder(Material.ARROW)
                        .name("§aVoltar")
                        .build(),
                event -> new BankMenu().open(player));

        super.open(player);
    }

}
