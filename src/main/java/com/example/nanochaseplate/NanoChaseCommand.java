package com.example.nanochaseplate;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class NanoChaseCommand implements CommandExecutor {

    private final NanoChaseplate plugin;

    public NanoChaseCommand(NanoChaseplate plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                              @NotNull String label, @NotNull String[] args) {

        // Sécurité supplémentaire : même si plugin.yml restreint déjà la commande
        // via la permission "nanochaseplate.give" (default: op), on revérifie ici
        // explicitement que l'exécutant est OP.
        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(Component.text(
                    "Utilisation depuis la console : /nanochase <joueur>",
                    NamedTextColor.RED));
            return true;
        }

        Player target;
        if (args.length >= 1) {
            target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("Joueur introuvable : " + args[0], NamedTextColor.RED));
                return true;
            }
            if (!sender.isOp()) {
                sender.sendMessage(Component.text("Seul un opérateur peut faire cela.", NamedTextColor.RED));
                return true;
            }
        } else {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Précisez un joueur : /nanochase <joueur>", NamedTextColor.RED));
                return true;
            }
            if (!player.isOp()) {
                player.sendMessage(Component.text("Seul un opérateur peut utiliser cette commande.", NamedTextColor.RED));
                return true;
            }
            target = player;
        }

        ItemStack nanoChaseplate = NanoChaseItem.create(plugin);
        target.getInventory().addItem(nanoChaseplate);

        target.sendMessage(Component.text("Vous avez reçu le ", NamedTextColor.GREEN)
                .append(Component.text("Nano Chaseplate", NamedTextColor.AQUA))
                .append(Component.text(" !", NamedTextColor.GREEN)));

        if (sender != target) {
            sender.sendMessage(Component.text("Nano Chaseplate donné à " + target.getName(), NamedTextColor.GREEN));
        }

        return true;
    }
}
