package best.azura.client.impl.config;

import best.azura.client.impl.Client;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ConfigManager {
    private final ArrayList<Config> configs = new ArrayList<>();

    public ConfigManager() {
        initConfigs();
    }

    public void initConfigs() {
        configs.clear();
        if (!getClientDirectory().exists() && getClientDirectory().mkdir())
            Client.INSTANCE.getLogger().info("Created client directory!");
        if (!getConfigDirectory().exists() && getConfigDirectory().mkdir())
            Client.INSTANCE.getLogger().info("Created config directory!");
        if (getConfigDirectory().listFiles() != null) {
            for (File f : Objects.requireNonNull(getConfigDirectory().listFiles()))
                if (f.isFile())
                    configs.add(createConfig(f.getName().replace(".json", "")));
        }
    }

    public void addConfig(Config config) {
        this.configs.add(config);
    }

    public ArrayList<Config> getConfigs() {
        return configs;
    }

    public Config getConfig(String name) {
        Config output = configs.stream().filter(c -> c.getName().replace(".json", "").equalsIgnoreCase(name.replace(".json", ""))).findFirst().orElse(null);
        if (output == null) {
            File temp;
            if ((temp = new File(getConfigDirectory(), name.replace(".json", "") + ".json")).exists())
                output = createConfigNoCheck(temp.getName().replace(".json", ""));
        }
        return output;
    }

    public File getClientDirectory() {
        return new File("Azura-X");
    }
    
    public File getDataDirectory() {
        return new File(getClientDirectory(), "data");
    }

    public File getConfigDirectory() {
        return new File(getClientDirectory(), "configs");
    }

    public File getScriptsDirectory() {
        return new File(getClientDirectory(), "scripts");
    }

    public File getCapeDirectory() {
        return new File(getClientDirectory(), "cape");
    }

    public File getFontDirectory() {
        return new File(getClientDirectory(), "fonts");
    }

    public Config createConfig(String name) {
        final Config config = getConfig(name);
        return config == null ? createConfigNoCheck(name) : config;
    }
    private Config createConfigNoCheck(String name) {
        return new Config(name, Client.INSTANCE.getUsername(), Client.INSTANCE.getCurrentDate(), false, false, false, false);
    }
}