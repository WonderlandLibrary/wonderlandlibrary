package graphite.api.management.config;

import com.google.gson.JsonObject;
import graphite.api.config.Config;
import graphite.impl.Graphite;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ConfigManagerImpl implements ConfigManager {

    private final Map<String, Config> configsMap = new HashMap<>();

    private Config currentlyLoadedConfig;

    public ConfigManagerImpl() {
        this.refresh();
    }

    @Override
    public void refresh() {
        final File configsDir = Graphite.INSTANCE.getFileManager().getFile("configs");

        final File[] files = configsDir.listFiles();

        if (files != null) {
            for (final File file : files) {
                if (FilenameUtils.getExtension(file.getName()).equals(EXTENSION.substring(1))) {
                    final String config = FilenameUtils.removeExtension(file.getName());

                    this.configsMap.put(config, new Config(config));
                }
            }
        }
    }

    @Override
    public boolean load(final String config) {
        final Config configObj = this.find(config);
        return this.load(configObj);
    }

    @Override
    public boolean load(final Config config) {
        if (config == null) {
            return false;
        }

        final JsonObject object = Graphite.INSTANCE.getFileManager().parse(config.getFile()).getAsJsonObject();

        if (object == null) {
            return false;
        }

        config.load(object);
        this.currentlyLoadedConfig = config;
        return true;
    }

    @Override
    public void save(final String config) {
        Config saved;

        if ((saved = find(config)) == null) {
            saved = new Config(config);
            this.configsMap.put(config, saved);
        }

        this.save(saved);
    }

    @Override
    public boolean save(final Config config) {
        if (config == null) {
            return false;
        }
        final JsonObject base = new JsonObject();
        config.save(base);
        Graphite.INSTANCE.getFileManager().writeJson(config.getFile(), base);

        return true;
    }

    @Override
    public Config find(final String config) {
        if (this.configsMap.containsKey(config))
            return this.configsMap.get(config);

        final File configsDir = Graphite.INSTANCE.getFileManager().getFile("configs");

        if (new File(configsDir, config + EXTENSION).exists())
            return new Config(config);

        return null;
    }

    @Override
    public boolean delete(final String config) {
        Config configObj;
        return (configObj = this.find(config)) != null && this.delete(configObj);
    }

    @Override
    public boolean delete(final Config config) {
        if (config == null) {
            return false;
        }

        final File f = config.getFile();
        this.configsMap.remove(config.getName());

        return f.exists() && f.delete();
    }

    @Override
    public boolean saveCurrent() {
        return this.save(this.currentlyLoadedConfig);
    }

    @Override
    public boolean reloadCurrent() {
        return this.load(this.currentlyLoadedConfig);
    }

    @Override
    public Collection<Config> getConfigs() {
        return this.configsMap.values();
    }
}