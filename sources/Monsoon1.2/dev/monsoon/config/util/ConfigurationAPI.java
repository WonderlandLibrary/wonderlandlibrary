package dev.monsoon.config.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class ConfigurationAPI {
    public static Configuration loadExistingConfiguration(File file) throws IOException {
        JSONObject jsonObject = new JSONObject(FileUtils.readFileToString(file, Charsets.UTF_8));
        return new Configuration(file, jsonObject.toMap());
    }

    public static Configuration newConfiguration(File file) {
        return new Configuration(file);
    }
}
