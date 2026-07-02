package com.example.nanochaseplate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Fabrique l'item "Nano Chaseplate" :
 * - Non craftable (aucune recette n'est enregistrée, il n'existe que via /nanochase)
 * - Ne s'use jamais (incassable)
 * - Design custom : nom coloré, lore, CustomModelData + effet "enchanté" pour le rendre
 *   visuellement unique (une resource pack peut ensuite exploiter le CustomModelData
 *   pour lui donner une vraie texture personnalisée).
 * - Marqué avec une donnée persistante pour être identifiable de façon fiable.
 */
public final class NanoChaseItem {

    // Choisissez la valeur que vous voulez pour votre resource pack custom.
    public static final int CUSTOM_MODEL_DATA = 20260702;

    private NanoChaseItem() {
    }

    public static ItemStack create(NanoChaseplate plugin) {
        ItemStack item = new ItemStack(Material.ELYTRA);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(
                Component.text("Nano Chaseplate", NamedTextColor.AQUA, TextDecoration.BOLD)
        );

        meta.lore(List.of(
                Component.text("Prototype expérimental de propulsion nanotechnologique.", NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, true),
                Component.empty(),
                Component.text("✦ Vol illimité tant qu'il est équipé", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("✦ Indestructible", NamedTextColor.LIGHT_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("✦ Impossible à fabriquer", NamedTextColor.DARK_GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        ));

        // Incassable : ne perd jamais de durabilité et ne se casse jamais
        meta.setUnbreakable(true);
        meta.addItemFlags(
                ItemFlag.HIDE_UNBREAKABLE,
                ItemFlag.HIDE_ATTRIBUTES,
                ItemFlag.HIDE_ENCHANTS
        );

        // Design custom : identifiant de modèle personnalisé pour une resource pack,
        // + léger effet "enchanté" (brillance) pour le distinguer visuellement dès maintenant.
        meta.setCustomModelData(CUSTOM_MODEL_DATA);
        meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        // (UNBREAKING est déjà inutile ici puisque l'item est incassable, il ne sert
        // qu'à donner l'effet visuel de brillance ; il est masqué par HIDE_ENCHANTS.)

        // Marqueur invisible dans les données persistantes : c'est la vraie preuve
        // "d'authenticité" utilisée par le plugin, indépendante du nom/lore.
        meta.getPersistentDataContainer().set(
                plugin.getNanoChaseKey(),
                PersistentDataType.BYTE,
                (byte) 1
        );

        item.setItemMeta(meta);
        return item;
    }

    public static boolean isNanoChaseplate(NanoChaseplate plugin, ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer()
                .has(plugin.getNanoChaseKey(), PersistentDataType.BYTE);
    }
}
