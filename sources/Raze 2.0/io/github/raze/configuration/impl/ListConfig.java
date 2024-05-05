package io.github.raze.configuration.impl;

import io.github.raze.configuration.Configuration;

import java.io.File;

public class ListConfig extends Configuration {

    public ListConfig() {
        super("List", "Obtain a list of all of your configs.");
    }

    public String listConfigs(String directory) {
        File configDir = new File(directory);
        if (!configDir.exists() || !configDir.isDirectory()) {
            return "No configs found.";
        }

        File[] configFiles = configDir.listFiles((dir, name) -> name.endsWith(".json"));
        if (configFiles == null || configFiles.length == 0) {
            return "No configs found.";
        }

        StringBuilder result = new StringBuilder("Available configs:\n");
        for (File configFile : configFiles) {
            String configName = configFile.getName().replace(".json", "");
            result.append("- ").append(configName).append("\n");
        }

        return result.toString();
    }

}
