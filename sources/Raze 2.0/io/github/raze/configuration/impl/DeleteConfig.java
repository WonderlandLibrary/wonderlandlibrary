package io.github.raze.configuration.impl;

import io.github.raze.configuration.Configuration;

import java.io.File;

public class DeleteConfig extends Configuration {

    public DeleteConfig() {
        super("Delete", "Delete your configs.");
    }

    public void deleteConfig(String filename) {
        File configFile = new File("raze/configs/" + filename);
        if (configFile.exists()) {
            configFile.delete();
        }
    }

    public boolean doesConfigExist(String filename) {
        File configFile = new File("raze/configs/" + filename);
        return configFile.exists();
    }

}
