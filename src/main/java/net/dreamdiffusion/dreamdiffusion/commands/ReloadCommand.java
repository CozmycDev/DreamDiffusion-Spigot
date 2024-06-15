package net.dreamdiffusion.dreamdiffusion.commands;

import net.dreamdiffusion.dreamdiffusion.DreamDiffusionPlugin;
import net.dreamdiffusion.dreamdiffusion.config.Language;
import net.dreamdiffusion.dreamdiffusion.config.Settings;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    private final DreamDiffusionPlugin plugin;

    public ReloadCommand(DreamDiffusionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(label.equalsIgnoreCase("ddreload"))) {
            return false;
        }

        if (!(sender instanceof Player)) {
            Settings.reload();
            Language.reload();
            sender.sendMessage(DreamDiffusionPlugin.prefix + Language.reloadedPlugin);
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("dreamdiffusion.reload")) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
            player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.noPermission);
            return false;
        }

        Settings.reload();
        Language.reload();
        player.sendMessage(DreamDiffusionPlugin.prefix + "§a" + Language.reloadedPlugin);
        return true;
    }
}
