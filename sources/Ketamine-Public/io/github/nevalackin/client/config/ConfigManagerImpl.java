package io.github.nevalackin.client.config;

import com.google.gson.JsonObject;
import io.github.nevalackin.client.config.ConfigManager;
import io.github.nevalackin.client.notification.NotificationType;
import io.github.nevalackin.client.core.KetamineClient;
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
        final File configsDir = KetamineClient.getInstance().getFileManager().getFile("configs");

        final File[] files = configsDir.listFiles();

        if (files != null) {
            int loadedConfigs = 0;

            for (final File file : files) {
                if (FilenameUtils.getExtension(file.getName()).equals(EXTENSION.substring(1))) {
                    final String config = FilenameUtils.removeExtension(file.getName());

                    this.configsMap.put(config, new Config(config));
                    ++loadedConfigs;
                }
            }

            KetamineClient.getInstance().getNotificationManager().add(
                NotificationType.SUCCESS, "Loaded Configs",
                String.format("Successfully loaded %s config(s).", loadedConfigs), 2000L);
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
            KetamineClient.getInstance().getNotificationManager().add(
                NotificationType.ERROR, "Non-Existent Config",
                "Attempted to load config which does not exist.", 2000L);
            return false;
        }

        final JsonObject object = KetamineClient.getInstance().getFileManager().parse(config.getFile()).getAsJsonObject();

        if (object == null) {
            KetamineClient.getInstance().getNotificationManager().add(
                NotificationType.ERROR, "Config Format Error",
                "Config file format is incorrect, unable to load.", 2000L);
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
            KetamineClient.getInstance().getNotificationManager().add(
                NotificationType.ERROR, "Non-Existent Config",
                "Attempted to save config which does not exist.", 2000L);
            return false;
        }
        final JsonObject base = new JsonObject();
        config.save(base);
        KetamineClient.getInstance().getFileManager().writeJson(config.getFile(), base);

        KetamineClient.getInstance().getNotificationManager().add(
            NotificationType.SUCCESS, "Saved Config",
            String.format("Successfully saved %s", config.getName()), 1000L);
        return true;
    }

    @Override
    public Config find(final String config) {
        if (this.configsMap.containsKey(config))
            return this.configsMap.get(config);

        final File configsDir = KetamineClient.getInstance().getFileManager().getFile("configs");

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
            KetamineClient.getInstance().getNotificationManager().add(
                NotificationType.WARNING, "Non-Existent Config",
                "Config has either already been deleted or didn't exist.", 3000L);
            return false;
        }

        final File f = config.getFile();
        this.configsMap.remove(config.getName());
        final boolean success = f.exists() && f.delete();

        if (success)
            KetamineClient.getInstance().getNotificationManager().add(
                NotificationType.SUCCESS, "Deleted Config",
                "Successfully deleted config and cleaned up file.", 1000L);
        else
            KetamineClient.getInstance().getNotificationManager().add(
                NotificationType.SUCCESS, "Delete Config Error",
                "Config file does not exist or insufficient permissions to delete file.", 3000L);

        return success;
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
