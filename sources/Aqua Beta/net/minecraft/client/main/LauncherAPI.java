package net.minecraft.client.main;

import java.io.File;
import net.minecraft.client.main.LauncherAPI;
import net.minecraft.client.main.Main;

/*
 * Exception performing whole class analysis ignored.
 */
public class LauncherAPI {
    public static void main(String[] args) {
        File workingDirectory;
        String userHome = System.getProperty((String)"user.home", (String)".");
        switch (1.$SwitchMap$net$minecraft$client$main$LauncherAPI$OS[OS.getOperatingSystem().ordinal()]) {
            case 1: {
                workingDirectory = new File(userHome, ".minecraft/");
                break;
            }
            case 2: {
                String applicationData = System.getenv((String)"APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                workingDirectory = new File(folder, ".minecraft/");
                break;
            }
            case 3: {
                workingDirectory = new File(userHome, "Library/Application Support/minecraft");
                break;
            }
            default: {
                workingDirectory = new File(userHome, "minecraft/");
            }
        }
        try {
            Main.main((String[])new String[]{"--version", "FantaX", "--accessToken", "0", "--gameDir", new File(workingDirectory, ".").getAbsolutePath(), "--assetsDir", new File(workingDirectory, "assets").getAbsolutePath(), "--assetIndex", "1.8", "--userProperties", "{}"});
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
