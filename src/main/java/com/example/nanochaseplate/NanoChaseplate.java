package com.example.nanochaseplate;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class NanoChaseplate extends JavaPlugin {

    private NamespacedKey nanoChaseKey;

    @Override
    public void onEnable() {
        // Clé unique utilisée pour "marquer" l'item dans ses données persistantes.
        // C'est ce qui permet de reconnaître le Nano Chaseplate et d'empêcher
        // toute duplication via craft/four/enclume.
        this.nanoChaseKey = new NamespacedKey(this, "nano_chaseplate");

        getCommand("nanochase").setExecutor(new NanoChaseCommand(this));

        getServer().getPluginManager().registerEvents(new FlightListener(this), this);
        getServer().getPluginManager().registerEvents(new AntiDupeListener(this), this);

        getLogger().info("NanoChaseplate activé. Item obtenable uniquement via /nanochase (OP).");
    }

    @Override
    public void onDisable() {
        getLogger().info("NanoChaseplate désactivé.");
    }

    public NamespacedKey getNanoChaseKey() {
        return nanoChaseKey;
    }
}
