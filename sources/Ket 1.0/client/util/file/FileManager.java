package client.util.file;

import client.Client;
import client.util.MinecraftInstance;

import java.io.File;

public class FileManager {

    public static final File DIRECTORY = new File(MinecraftInstance.mc.mcDataDir, Client.NAME);

    public void init() {
        if (!DIRECTORY.exists()) DIRECTORY.mkdir();
    }
}