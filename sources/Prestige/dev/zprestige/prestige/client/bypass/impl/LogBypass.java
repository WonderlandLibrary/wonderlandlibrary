package dev.zprestige.prestige.client.bypass.impl;

import dev.zprestige.prestige.client.bypass.Bypass;
import net.minecraft.client.MinecraftClient;

import java.io.*;

public class LogBypass extends Bypass {
    public LogBypass() {
        super("Log Bypass");
    }

    @Override
    public String run() {
        File file = new File(MinecraftClient.getInstance().runDirectory + File.separator + "logs" + File.separator + "latest.log");
        if (!file.exists()) {
            return "Log Bypass failed at stage 1.";
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                if (!string.contains("prestige") && !string.contains("[main/INFO] (FabricLoader) Loading ")) {
                    stringBuilder.append(string).append("\n");
                } else if (string.contains("prestige")) {
                    stringBuilder.append(string.replace("prestige", "")).append("\n");
                }
                if (string.contains("[main/INFO] (FabricLoader) Loading ")) {
                    String object = string.split("Loading ")[1].split(" mods:")[0];
                    stringBuilder.append(string.split("Loading ")[0]).append(Integer.parseInt(object) - 1).append(" mods:").append("\n");
                }
            }
            bufferedReader.close();
            BufferedWriter object = new BufferedWriter(new FileWriter(file));
            object.write(stringBuilder.toString());
            object.close();
            return "Traces in logs have successfully been removed.";
        }
        catch (IOException iOException) {
            return "Error occurred while processing the file.";
        }
    }
}
