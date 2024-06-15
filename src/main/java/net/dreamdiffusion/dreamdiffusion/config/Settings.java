package net.dreamdiffusion.dreamdiffusion.config;

import net.dreamdiffusion.dreamdiffusion.DreamDiffusionPlugin;

import java.io.File;

public class Settings {

    public static String mode;
    public static String hordeUrl;
    public static String hordeApiKey;
    public static String imageModel;
    public static int steps;
    public static String sampler;
    public static double guidanceScale;
    public static int clipSkip;
    public static boolean karras;

    public static void addConfigDefaults() {
        mode = "StableHorde";
        hordeUrl = "https://aihorde.net/api";
        hordeApiKey = "0000000000";
        imageModel = "DreamShaper XL";
        steps = 6;
        sampler = "k_dpmpp_sde";
        guidanceScale = 2.0f;
        clipSkip = 2;
        karras = true;

        Configuration config = getConfiguration();

        setNewConfigValues(config);
    }

    private static Configuration getConfiguration() {
        Configuration config = new Configuration(
                DreamDiffusionPlugin.plugin.getDataFolder() + File.separator + "config.yml");

        config.add("General.GenerateSource", mode);
        config.add("StableHorde.Token", hordeApiKey);
        config.add("StableHorde.URL", hordeUrl);
        config.add("StableHorde.ImageModel", imageModel);
        config.add("StableHorde.Steps", steps);
        config.add("StableHorde.Sampler", sampler);
        config.add("StableHorde.GuidanceScale", guidanceScale);
        config.add("StableHorde.ClipSkip", clipSkip);
        config.add("StableHorde.Karras", karras);

        config.save();
        return config;
    }

    public static void setNewConfigValues(Configuration config) {
        mode = String.valueOf(config.getString("General.GenerateSource"));
        hordeApiKey = String.valueOf(config.getString("StableHorde.Token"));
        hordeUrl = String.valueOf(config.getString("StableHorde.URL"));
        imageModel = String.valueOf(config.getString("StableHorde.ImageModel"));
        steps = config.getInteger("StableHorde.Steps");
        sampler = String.valueOf(config.getString("StableHorde.Sampler"));
        guidanceScale = config.getDouble("StableHorde.GuidanceScale");
        clipSkip = config.getInteger("StableHorde.ClipSkip");
        karras = config.getBoolean("StableHorde.Karras");
    }

    public static void reload() {
        Configuration config = new Configuration(
                DreamDiffusionPlugin.plugin.getDataFolder() + File.separator + "config.yml");

        setNewConfigValues(config);
    }
}