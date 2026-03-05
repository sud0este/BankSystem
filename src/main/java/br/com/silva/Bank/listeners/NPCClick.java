package br.com.silva.Bank.listeners;

import br.com.silva.Bank.menus.BankMenu;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCClick implements Listener {

    @EventHandler
    public void onClick(NPCRightClickEvent event) {
        if (event.getNPC() == null) return;

        String npcName = event.getNPC().getName();

        if (!npcName.contains("Banqueiro")) return;

        new BankMenu().open(event.getClicker());
    }
}
