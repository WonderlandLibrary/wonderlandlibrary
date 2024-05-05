package io.github.raze.utilities.collection.configs;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PrefixUtil {

    public static void savePrefix(String inputPrefix) {
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
            properties.setProperty("prefix", inputPrefix);
            properties.store(outputStream, "Raze Properties");
            outputStream.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }


    public static String getPrefixFromFile() {
        File directory = new File("raze");
        if (!directory.exists())
            directory.mkdirs();

        File cfgFile = new File("raze/config.properties");
        try {
            if (!cfgFile.exists())
                return ".";

            Properties properties = new Properties();
            InputStream inputStream = Files.newInputStream(Paths.get("raze/config.properties"));
            properties.load(inputStream);
            inputStream.close();

            String prefix = properties.getProperty("prefix");
            if (prefix != null && !prefix.isEmpty())
                return prefix;

        } catch (IOException io) {
            io.printStackTrace();
        }

        return "";
    }

}
