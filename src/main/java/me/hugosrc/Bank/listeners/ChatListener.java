package me.hugosrc.Bank.listeners;

import me.hugosrc.Bank.BankPlugin;
import me.hugosrc.Bank.entities.objects.Account;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Account depositAccount = BankPlugin.getInstance().getDepositExpecters().get(uuid);
        Account withdrawAccount = BankPlugin.getInstance().getWithdrawExpecters().get(uuid);

        if (depositAccount != null) {
            event.setCancelled(true);
            // Executar na thread principal
            Bukkit.getScheduler().runTask(BankPlugin.getInstance(), () ->
                handleDeposit(player, depositAccount, event.getMessage())
            );
            BankPlugin.getInstance().getDepositExpecters().remove(uuid);
        } else if (withdrawAccount != null) {
            event.setCancelled(true);
            // Executar na thread principal
            Bukkit.getScheduler().runTask(BankPlugin.getInstance(), () ->
                handleWithdraw(player, withdrawAccount, event.getMessage())
            );
            BankPlugin.getInstance().getWithdrawExpecters().remove(uuid);
        }
    }

    private void handleDeposit(Player player, Account account, String message) {
        try {
            double amount = Double.parseDouble(message);
            if (amount <= 0) {
                player.sendMessage("§cO valor deve ser maior que zero!");
                return;
            }
            account.deposit(amount);
            player.sendMessage("§aValor depositado com sucesso!");
        } catch (NumberFormatException e) {
            player.sendMessage("§cValor inválido! Use apenas números (ex: 100.50)");
        }
    }

    private void handleWithdraw(Player player, Account account, String message) {
        try {
            double amount = Double.parseDouble(message);
            if (amount <= 0) {
                player.sendMessage("§cO valor deve ser maior que zero!");
                return;
            }
            account.withdraw(amount);
            player.sendMessage("§aValor sacado com sucesso!");
        } catch (NumberFormatException e) {
            player.sendMessage("§cValor inválido! Use apenas números (ex: 100.50)");
        }
    }
}
