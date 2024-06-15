package net.dreamdiffusion.dreamdiffusion.config;

import net.dreamdiffusion.dreamdiffusion.DreamDiffusionPlugin;

import java.io.File;

public class Language {

    public static String reloadedPlugin;
    public static String generatingDream;
    public static String finishedDream;
    public static String deletedDream;

    public static String noConsole;
    public static String noPermission;
    public static String alreadyGenerating;
    public static String mapRequired;
    public static String dreamLimit;
    public static String promptRequired;
    public static String dreamError;
    public static String fileTooLarge;
    public static String invalidMode;

    public static String barAddingQueue;
    public static String barGenerating;
    public static String barQueuePosition;

    public static void addConfigDefaults() {
        reloadedPlugin = "DreamDiffusion has been reloaded!";
        generatingDream = "Your Dream has been queued, one moment";
        finishedDream = "Dream complete! Place it into an item frame!";
        deletedDream = "Deleted Dream";

        noConsole = "Console cant have dreams :(";
        noPermission = "No permission!";
        alreadyGenerating = "You are already generating a Dream!";
        mapRequired = "You need an empty map item!";
        dreamLimit = "You cannot create any more Dreams!";
        promptRequired = "You need to provide a prompt!";
        dreamError = "There was an error processing this Dream!";
        fileTooLarge = "The image result was too large to process, try again!";
        invalidMode = "The configured generation source is not implemented!";

        barAddingQueue = "Adding Dream to queue...";
        barGenerating = "Generating Dream...";
        barQueuePosition = "Dream Queue Position";

        Configuration config = getConfiguration();

        setNewConfigValues(config);
    }

    private static Configuration getConfiguration() {
        Configuration config = new Configuration(
                DreamDiffusionPlugin.plugin.getDataFolder() + File.separator + "lang.yml");

        config.add("General.ReloadedPlugin", reloadedPlugin);
        config.add("General.GeneratingDream", generatingDream);
        config.add("General.FinishedDream", finishedDream);
        config.add("General.DeletedDream", deletedDream);

        config.add("Errors.NoConsole", noConsole);
        config.add("Errors.NoPermission", noPermission);
        config.add("Errors.AlreadyGenerating", alreadyGenerating);
        config.add("Errors.MapItemRequired", mapRequired);
        config.add("Errors.NoMoreDreams", dreamLimit);
        config.add("Errors.PromptRequired", promptRequired);
        config.add("Errors.DreamError", dreamError);
        config.add("Errors.FileTooLarge", fileTooLarge);
        config.add("Errors.InvalidSource", invalidMode);

        config.add("BossBar.AddingToQueue", barAddingQueue);
        config.add("BossBar.Generating", barGenerating);
        config.add("BossBar.QueuePosition", barQueuePosition);

        config.save();
        return config;
    }

    public static void setNewConfigValues(Configuration config) {
        reloadedPlugin = String.valueOf(config.getString("General.ReloadedPlugin"));
        generatingDream = String.valueOf(config.getString("General.GeneratingDream"));
        finishedDream = String.valueOf(config.getString("General.FinishedDream"));
        deletedDream = String.valueOf(config.getString("General.DeletedDream"));

        noConsole = String.valueOf(config.getString("Errors.NoConsole"));
        noPermission = String.valueOf(config.getString("Errors.NoPermission"));
        alreadyGenerating = String.valueOf(config.getString("Errors.AlreadyGenerating"));
        mapRequired = String.valueOf(config.getString("Errors.MapItemRequired"));
        dreamLimit = String.valueOf(config.getString("Errors.NoMoreDreams"));
        promptRequired = String.valueOf(config.getString("Errors.PromptRequired"));
        dreamError = String.valueOf(config.getString("Errors.DreamError"));
        fileTooLarge = String.valueOf(config.getString("Errors.FileTooLarge"));
        invalidMode = String.valueOf(config.getString("Errors.InvalidSource"));

        barAddingQueue = String.valueOf(config.getString("BossBar.AddingToQueue"));
        barGenerating = String.valueOf(config.getString("BossBar.Generating"));
        barQueuePosition = String.valueOf(config.getString("BossBar.QueuePosition"));
    }

    public static void reload() {
        Configuration config = new Configuration(
                DreamDiffusionPlugin.plugin.getDataFolder() + File.separator + "lang.yml");

        setNewConfigValues(config);
    }
}