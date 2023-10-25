package org.dreamcore.client.files;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.dreamcore.client.dreamcore;
import org.dreamcore.client.files.impl.*;
import org.dreamcore.security.utils.HashUtil;
import org.dreamcore.security.utils.HwidUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class FileManager {

    public static File directory = new File(dreamcore.instance.name);
    public static ArrayList<CustomFile> files = new ArrayList<>();

    public FileManager() {
        files.add(new AltConfig("AltConfig", true));
        files.add(new FriendConfig("FriendConfig", true));
        files.add(new MacroConfig("MacroConfig", true));
        files.add(new HudConfig("HudConfig", true));
        files.add(new CapeConfig("CapeConfig", true));
        files.add(new XrayConfig("XrayConfig", true));
    }

    public void loadFiles() {
        for (CustomFile file : files) {
            try {
                if (file.loadOnStart()) {
                    file.loadFile();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFiles() {
        for (CustomFile f : files) {
            try {
                f.saveFile();
            } catch (Exception e) {

            }
        }
    }

    public CustomFile getFile(Class<?> clazz) {
        Iterator<CustomFile> customFileIterator = files.iterator();

        CustomFile file;
        do {
            if (!customFileIterator.hasNext()) {
                return null;
            }

            file = customFileIterator.next();
        } while (file.getClass() != clazz);

        return file;
    }

    public abstract static class CustomFile {

        private final File file;
        private final String name;
        private final boolean load;

        public CustomFile(String name, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(FileManager.directory, name + ".json");
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                } catch (Exception e) {

                }
            }
        }

        public final File getFile() {
            return this.file;
        }

        private boolean loadOnStart() {
            return this.load;
        }

        public final String getName() {
            return this.name;
        }

        public abstract void loadFile() throws Exception;

        public abstract void saveFile() throws Exception;
    }
}
