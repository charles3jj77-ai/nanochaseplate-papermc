package com.example.nanochaseplate;

import io.papermc.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Donne le vol illimité (comme en créatif) tant que le Nano Chaseplate
 * est équipé dans le slot plastron, et le retire proprement quand
 * il est enlevé — sans jamais s'user ni consommer de "carburant".
 */
public final class FlightListener implements Listener {

    private final NanoChaseplate plugin;
    // Joueurs à qui l'on a accordé le vol *grâce au Nano Chaseplate*.
    // Sert à ne jamais retirer le vol à un joueur qui l'aurait déjà
    // pour une autre raison (créatif, spectateur, autre plugin, etc.).
    private final Set<UUID> grantedByItem = new HashSet<>();

    public FlightListener(NanoChaseplate plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ItemStack chestplate = player.getInventory().getChestplate();
        if (NanoChaseItem.isNanoChaseplate(plugin, chestplate)) {
            grantFlight(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        grantedByItem.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onArmorChange(PlayerArmorChangeEvent event) {
        if (event.getSlotType() != PlayerArmorChangeEvent.SlotType.CHEST) {
            return;
        }

        Player player = event.getPlayer();
        boolean wasNano = NanoChaseItem.isNanoChaseplate(plugin, event.getOldItem());
        boolean isNano = NanoChaseItem.isNanoChaseplate(plugin, event.getNewItem());

        if (isNano && !wasNano) {
            grantFlight(player);
        } else if (!isNano && wasNano) {
            revokeFlight(player);
        }
    }

    // Filet de sécurité : annule les dégâts de chute pour un joueur qui
    // vole grâce au Nano Chaseplate (cohérent avec un vol "indéfini").
    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (grantedByItem.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    private void grantFlight(Player player) {
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            // Ces modes ont déjà le vol nativement : rien à "accorder" nous-mêmes.
            return;
        }
        grantedByItem.add(player.getUniqueId());
        player.setAllowFlight(true);
    }

    private void revokeFlight(Player player) {
        UUID uuid = player.getUniqueId();
        if (!grantedByItem.remove(uuid)) {
            return;
        }
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        player.setAllowFlight(false);
        if (player.isFlying()) {
            player.setFlying(false);
        }
    }
}
