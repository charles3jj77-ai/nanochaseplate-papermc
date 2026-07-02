package com.example.nanochaseplate;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

/**
 * S'assure que le Nano Chaseplate reste strictement obtenable
 * uniquement via /nanochase :
 * - Aucune recette n'est enregistrée par ce plugin (donc pas de craft "normal").
 * - Par sécurité, on bloque quand même toute table à craft qui produirait
 *   un résultat marqué comme Nano Chaseplate (au cas où un autre plugin/mod
 *   ou une resource pack de triche tenterait de le faire apparaître).
 * - On empêche aussi de le renommer/dupliquer via une enclume pour en
 *   forger une copie "propre" (l'original garde toujours sa donnée persistante,
 *   donc ce n'est pas strictement nécessaire, mais ça évite toute confusion).
 */
public final class AntiDupeListener implements Listener {

    private final NanoChaseplate plugin;

    public AntiDupeListener(NanoChaseplate plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        ItemStack result = event.getInventory().getResult();
        if (NanoChaseItem.isNanoChaseplate(plugin, result)) {
            event.getInventory().setResult(null);
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();
        if (NanoChaseItem.isNanoChaseplate(plugin, result)) {
            event.setCancelled(true);
            HumanEntity who = event.getWhoClicked();
            who.closeInventory();
        }
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        ItemStack left = event.getInventory().getItem(0);
        if (NanoChaseItem.isNanoChaseplate(plugin, left)) {
            // Autorisé à être renommé si besoin, mais on empêche toute
            // combinaison avec un autre item qui pourrait produire une copie.
            ItemStack right = event.getInventory().getItem(1);
            if (right != null) {
                event.setResult(null);
            }
        }
    }
}
