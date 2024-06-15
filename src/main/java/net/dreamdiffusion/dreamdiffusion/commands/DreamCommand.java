package net.dreamdiffusion.dreamdiffusion.commands;

import com.loohp.imageframe.ImageFrame;
import com.loohp.imageframe.objectholders.ImageMap;
import com.loohp.imageframe.objectholders.Scheduler;
import com.loohp.imageframe.objectholders.URLStaticImageMap;
import com.loohp.imageframe.utils.HTTPRequestUtils;
import com.loohp.imageframe.utils.MapUtils;
import net.dreamdiffusion.dreamdiffusion.DreamDiffusionPlugin;
import net.dreamdiffusion.dreamdiffusion.config.Language;
import net.dreamdiffusion.dreamdiffusion.config.Settings;
import net.dreamdiffusion.dreamdiffusion.horde.HordeAPIBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;


public class DreamCommand implements CommandExecutor {

    private final DreamDiffusionPlugin plugin;
    private final Set<UUID> generating = new HashSet<>();

    public DreamCommand(DreamDiffusionPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(label.equalsIgnoreCase("dream"))) { return false; }

        if (!(sender instanceof Player)) {
            sender.sendMessage(Language.noConsole);
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("dreamdiffusion.use")) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
            player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.noPermission);
            return false;
        }

        if (generating.contains(player.getUniqueId())) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
            player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.alreadyGenerating);
            return false;
        }

        int limit = player.isOp() ? -1 : ImageFrame.getPlayerCreationLimit(player);
        Set<ImageMap> existingMaps = ImageFrame.imageMapManager.getFromCreator(player.getUniqueId());
        if (limit >= 0 && existingMaps.size() >= limit) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
            player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.dreamLimit);
            return false;
        }

        int size = 1;

        int takenMaps;
        if (!(player.isOp())) {
            if ((takenMaps = MapUtils.removeEmptyMaps(player, size * size, true)) < 0) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
                sender.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.mapRequired);
                return true;
            }
        } else {
            takenMaps = 0;
        }

        String prompt = String.join(" ", args);
        if (prompt.isEmpty() || prompt.isBlank()) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
            player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.promptRequired);
            return false;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.6f);
        player.sendMessage(DreamDiffusionPlugin.prefix + "§7" + Language.generatingDream + ": §f" + prompt);

        final int width = size;
        final int height = size;

        generating.add(player.getUniqueId());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            BossBar bossBar = Bukkit.createBossBar("§3" + Language.barAddingQueue, BarColor.PURPLE, BarStyle.SOLID);
            bossBar.addPlayer(player);
            try {
                String name = UUID.randomUUID().toString();

                if (!Objects.equals(Settings.mode, "StableHorde")) {
                    player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.invalidMode);
                    returnMaps(player, takenMaps);
                    return;
                }

                HordeAPIBuilder builder = new HordeAPIBuilder()
                        .setPrompt(prompt)
                        .setModel(Settings.imageModel)
                        .setSteps(Settings.steps)
                        .setSampler(Settings.sampler)
                        .setGuidanceScale(Settings.guidanceScale)
                        .setClipSkip(Settings.clipSkip)
                        .setKarras(Settings.karras);
                String imageUrl = builder.generateImageUrl(bossBar);

                continueGeneration(imageUrl, player, name, width, height);
            } catch (Exception e) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
                player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.dreamError);
                returnMaps(player, takenMaps);
                e.printStackTrace();
            } finally {
                generating.remove(player.getUniqueId());
                bossBar.removeAll();
            }
        });

        return true;
    }

    private void returnMaps(Player player, int takenMaps) {
        if (takenMaps > 0) {
            Scheduler.runTask(ImageFrame.plugin, () -> {
                HashMap<Integer, ItemStack> result = player.getInventory().addItem(
                        new ItemStack(Material.MAP, takenMaps));
                for (ItemStack stack : result.values()) {
                    player.getWorld().dropItem(player.getEyeLocation(), stack).setVelocity(
                            new Vector(0, 0, 0));
                }
            }, player);
        }
    }

    private void continueGeneration(String imageUrl, CommandSender sender, String name, Integer width, Integer height) {
        Player player = (Player) sender;
        try {
            if (HTTPRequestUtils.getContentSize(imageUrl) > ImageFrame.maxImageFileSize) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
                player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.fileTooLarge);
                return;
            }

            ImageMap imageMap = URLStaticImageMap.create(
                    ImageFrame.imageMapManager, name, imageUrl, width, height, player.getUniqueId()).get();

            ImageFrame.imageMapManager.addMap(imageMap);
            ImageFrame.combinedMapItemHandler.giveCombinedMap(imageMap, player);

            player.playSound(player.getLocation(), Sound.ENTITY_ALLAY_ITEM_GIVEN, 1.0f, 1.7f);
            player.sendMessage(DreamDiffusionPlugin.prefix + "§a" + Language.finishedDream);
            player.sendMessage(DreamDiffusionPlugin.prefix + "§b" + imageUrl);
        } catch (Exception e) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.7f);
            player.sendMessage(DreamDiffusionPlugin.prefix + "§c" + Language.dreamError);
            e.printStackTrace();
        }
    }
}
