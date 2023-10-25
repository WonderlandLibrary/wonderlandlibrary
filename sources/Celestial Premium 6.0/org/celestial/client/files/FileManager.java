/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.celestial.client.Celestial;
import org.celestial.client.files.impl.AltConfig;
import org.celestial.client.files.impl.CapeConfig;
import org.celestial.client.files.impl.FriendConfig;
import org.celestial.client.files.impl.HudConfig;
import org.celestial.client.files.impl.MacroConfig;
import org.celestial.client.files.impl.XrayConfig;

public class FileManager {
    private static final File directory;
    public static ArrayList<CustomFile> files;

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
                if (!file.loadOnStart()) continue;
                file.loadFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFiles() {
        for (CustomFile f : files) {
            try {
                f.saveFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CustomFile getFile(Class clazz) {
        CustomFile file;
        Iterator<CustomFile> customFileIterator = files.iterator();
        do {
            if (customFileIterator.hasNext()) continue;
            return null;
        } while ((file = customFileIterator.next()).getClass() != clazz);
        return file;
    }

    static {
        files = new ArrayList();
        directory = new File(Celestial.name);
    }

    public static abstract class CustomFile {
        private final File file;
        private final String name;
        private final boolean load;

        public CustomFile(String name, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(directory, name + ".json");
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                }
                catch (Exception e) {
                    e.printStackTrace();
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

        public abstract void loadFile() throws IOException;

        public abstract void saveFile() throws IOException;
    }
}

