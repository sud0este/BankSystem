package br.com.silva.Bank.listeners;

import br.com.silva.Bank.BankPlugin;
import br.com.silva.Bank.entities.objects.Bank;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final Bank bank;

    public PlayerJoin() {
        bank = BankPlugin.getInstance().getBank();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        bank.addAccount(player);
    }

}
