package net.dreamdiffusion.dreamdiffusion.listeners;

import com.loohp.imageframe.ImageFrame;
import com.loohp.imageframe.objectholders.ImageMap;
import net.dreamdiffusion.dreamdiffusion.DreamDiffusionPlugin;
import net.dreamdiffusion.dreamdiffusion.config.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemListener implements Listener {

    public static void checkItemStack(ItemStack itemStack) {
        if (itemStack.hasItemMeta() && Objects.requireNonNull(itemStack.getItemMeta()).hasDisplayName()) {
            String itemName = itemStack.getItemMeta().getDisplayName();
            String mapName = extractUUID(itemName);
            deleteMap(mapName);
        }
    }

    public static String extractUUID(String input) {
        String uuidPattern = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
        Pattern pattern = Pattern.compile(uuidPattern);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static void deleteMap(String mapName) {
        if (mapName != null) {
            for (ImageMap map : ImageFrame.imageMapManager.getMaps()) {
                if (Objects.equals(map.getName(), mapName)) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(map.getCreator());
                    ImageFrame.imageMapManager.deleteMap(map.getImageIndex());
                    if (offlinePlayer.getPlayer() != null && offlinePlayer.isOnline()) {
                        offlinePlayer.getPlayer().sendMessage(
                                DreamDiffusionPlugin.prefix + "§c" + Language.deletedDream + ": §7" + mapName);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        ItemStack itemStack = event.getEntity().getItemStack();
        checkItemStack(itemStack);
    }

    @EventHandler
    public void onItemRemove(EntityRemoveEvent event) {
        if (!(event.getEntity() instanceof Item)) { return; }
        Item item = (Item) event.getEntity();
        ItemStack itemStack = item.getItemStack();
        checkItemStack(itemStack);
    }
}
