package net.dreamdiffusion.dreamdiffusion;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class Compatibility {

    private static final Map<String, Plugin> hookedPlugins = new HashMap<>();

    public static boolean checkHooks() {
        // Add Vault economy support
        return hookPlugin("ImageFrame", "1.7.8.2", "1.7.8.2");
    }

    private static boolean hookPlugin(String name, String minVersion, String maxVersion) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);

        if (plugin == null) {
            return false;
        }

        String version = plugin.getDescription().getVersion().split("-")[0];

        if (isVersionSupported(version, minVersion, maxVersion)) {
            return hookPlugin(name, plugin);
        } else {
            DreamDiffusionPlugin.colorLog(String.format("%s v%s is unknown or unsupported. Attempting to hook anyway. There may be errors.", name, version));
            return hookPlugin(name, plugin);
        }
    }

    private static boolean hookPlugin(String name, Plugin plugin) {
        if (hookedPlugins.containsKey(name)) {
            return false;
        }
        hookedPlugins.put(name, plugin);
        DreamDiffusionPlugin.colorLog(String.format("Hooked into %s successfully!", name));
        return true;
    }

    private static boolean isVersionSupported(String version, String minVersion, String maxVersion) {
        try {
            return compareVersions(version, minVersion) >= 0 && compareVersions(version, maxVersion) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static int compareVersions(String version, String otherVersion) {
        String[] versionParts = version.split("\\.");
        String[] otherVersionParts = otherVersion.split("\\.");

        int length = Math.max(versionParts.length, otherVersionParts.length);
        for (int i = 0; i < length; i++) {
            int vPart = i < versionParts.length ? Integer.parseInt(versionParts[i]) : 0;
            int oPart = i < otherVersionParts.length ? Integer.parseInt(otherVersionParts[i]) : 0;
            if (vPart != oPart) {
                return Integer.compare(vPart, oPart);
            }
        }
        return 0;
    }
}
