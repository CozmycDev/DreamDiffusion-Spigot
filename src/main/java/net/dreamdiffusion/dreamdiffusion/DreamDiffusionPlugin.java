package net.dreamdiffusion.dreamdiffusion;

import net.dreamdiffusion.dreamdiffusion.commands.DreamCommand;
import net.dreamdiffusion.dreamdiffusion.commands.ReloadCommand;
import net.dreamdiffusion.dreamdiffusion.config.Language;
import net.dreamdiffusion.dreamdiffusion.config.Settings;
import net.dreamdiffusion.dreamdiffusion.horde.HordeAPIBuilder;
import net.dreamdiffusion.dreamdiffusion.listeners.ItemListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public final class DreamDiffusionPlugin extends JavaPlugin implements Listener {

    public static JavaPlugin plugin;
    public static String prefix;

    @Override
    public void onEnable() {
        plugin = this;
        prefix = "§8[§bDreamDiffusion§8] ";

        if (!(Compatibility.checkHooks())) {
            colorLog("DreamMinecraft could not hook into a compatible image to map provider, disabling!");
            colorLog("Please install https://www.spigotmc.org/resources/106031/ then restart your server!");
            plugin = null;
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        Settings.addConfigDefaults();
        Settings.reload();

        Language.addConfigDefaults();
        Language.reload();

        PluginCommand dreamCommand = this.getCommand("dream");
        Objects.requireNonNull(dreamCommand).setExecutor(new DreamCommand(this));

        PluginCommand reloadCommand = this.getCommand("ddreload");
        Objects.requireNonNull(reloadCommand).setExecutor(new ReloadCommand(this));

        registerListeners();

        colorLog("DreamDiffusion has been loaded!");
    }

    @Override
    public void onDisable() {
        HordeAPIBuilder.stopFlag = true;
        colorLog("DreamDiffusion is no longer loaded!");
    }

    public static void colorLog(String message) {
        Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor(prefix + message));
    }

    private void registerListeners() {
        registerEvents(plugin, new ItemListener());
    }

    private static void registerEvents(Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
