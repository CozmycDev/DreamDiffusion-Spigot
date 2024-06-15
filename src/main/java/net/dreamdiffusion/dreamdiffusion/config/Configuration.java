package net.dreamdiffusion.dreamdiffusion.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Configuration {

    private File file;
    private YamlConfiguration yaml = new YamlConfiguration();

    public Configuration(String path) {
        file = new File(path);
        if (!file.exists() || file == null) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        load();
    }

    private void load() {
        try {
            yaml.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            yaml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getInteger(String s) {
        return yaml.getInt(s);
    }

    public String getString(String s) {
        return yaml.getString(s);
    }

    public Object get(String s) {
        return yaml.get(s);
    }

    public Map<String, Object> getMap(String s) {
        return yaml.getConfigurationSection(s).getValues(false);
    }

    public YamlConfiguration getYaml() {
        return this.yaml;
    }

    public boolean getBoolean(String s) {
        return yaml.getBoolean(s);
    }

    public void add(String s, Object o) {
        if (!contains(s)) {
            set(s, o);
        }
    }

    public Set<String> getKeys(boolean deep) {
        return yaml.getKeys(deep);
    }

    public void addToStringList(String s, String o) {
        yaml.getStringList(s).add(o);
    }

    public void removeFromStringList(String s, String o) {
        yaml.getStringList(s).remove(o);
    }

    public List<String> getStringList(String s) {
        return yaml.getStringList(s);
    }

    public void addToIntegerList(String s, int o) {
        yaml.getIntegerList(s).add(o);
    }

    public void removeFromIntegerList(String s, int o) {
        yaml.getIntegerList(s).remove(o);
    }

    public List<Integer> getIntegerList(String s) {
        return yaml.getIntegerList(s);
    }

    public void createNewStringList(String s, List<String> list) {
        yaml.set(s, list);
    }

    public void createNewIntegerList(String s, List<Integer> list) {
        yaml.set(s, list);
    }

    public void remove(String s) {
        set(s, null);
    }

    public boolean contains(String s) {
        return yaml.contains(s);
    }

    public double getDouble(String s) {
        return yaml.getDouble(s);
    }

    public void set(String s, Object o) {
        yaml.set(s, o);
    }

    public void increment(String s) {
        yaml.set(s, getInteger(s) + 1);
    }

    public void decrement(String s) {
        yaml.set(s, getInteger(s) - 1);
    }

    public void increment(String s, int i) {
        yaml.set(s, getInteger(s) + i);
    }

    public void decrement(String s, int i) {
        yaml.set(s, getInteger(s) - i);
    }

    public YamlConfigurationOptions options() {
        return yaml.options();
    }
}