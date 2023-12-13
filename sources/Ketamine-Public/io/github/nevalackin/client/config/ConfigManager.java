package io.github.nevalackin.client.config;

import io.github.nevalackin.client.config.Config;

import java.util.Collection;

public interface ConfigManager {

    String EXTENSION = ".keta";

    boolean load(final String config);
    boolean load(final Config config);

    void save(final String config);
    boolean save(final Config config);

    Config find(final String config);

    boolean delete(final String config);
    boolean delete(final Config config);

    boolean saveCurrent();
    boolean reloadCurrent();

    void refresh();

    Collection<Config> getConfigs();
}
