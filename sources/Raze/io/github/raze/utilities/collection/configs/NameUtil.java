package io.github.raze.utilities.collection.configs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class NameUtil {

    public static void saveCustomName(String inputName) {
        File directory = new File("raze");
        if (!directory.exists())
            directory.mkdirs();

        File cfgFile = new File("raze/config.properties");
        try {
            Properties properties = new Properties();

            // Load existing properties if the file exists
            if (cfgFile.exists()) {
                InputStream inputStream = Files.newInputStream(cfgFile.toPath());
                properties.load(inputStream);
                inputStream.close();
            }

            OutputStream outputStream = Files.newOutputStream(cfgFile.toPath());
            properties.setProperty("name", inputName);
            properties.store(outputStream, "Raze Properties");
            outputStream.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


    public static String getCustomNameFromFile() {
        File directory = new File("raze");
        if (!directory.exists())
            directory.mkdirs();

        File cfgFile = new File("raze/config.properties");
        try {
            if (!cfgFile.exists())
                return "RazeUser";

            Properties properties = new Properties();
            InputStream inputStream = Files.newInputStream(Paths.get("raze/config.properties"));
            properties.load(inputStream);
            inputStream.close();

            String customName = properties.getProperty("name");
            if (customName != null && !customName.isEmpty())
                return customName;

        } catch (IOException io) {
            io.printStackTrace();
        }

        return "";
    }

}
