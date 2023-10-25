package ru.smertnix.celestial.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import ru.smertnix.celestial.files.impl.AltConfig;
import ru.smertnix.celestial.files.impl.ClientConfig;
import ru.smertnix.celestial.files.impl.FriendConfig;
import ru.smertnix.celestial.files.impl.HudConfig;
import ru.smertnix.celestial.files.impl.MacroConfig;
import ru.smertnix.celestial.files.impl.QuickJoin;

public class FileManager {
    private static final File directory;

    public static ArrayList<CustomFile> files = new ArrayList<>();

    static {
        directory = new File("C:\\Minced");
    }

    public FileManager() {
        files.add(new AltConfig("alt", true));
        files.add(new FriendConfig("friend", true));
        files.add(new MacroConfig("macro", true));
        files.add(new HudConfig("hud", true));
        files.add(new ClientConfig("client", true));
        files.add(new QuickJoin("quickjoin", true));
    }

    public void loadFiles() {
        for (CustomFile file : files) {
            try {
                if (file.loadOnStart())
                    file.loadFile();
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
                e.printStackTrace();
            }
        }
    }

    public CustomFile getFile(Class<?> clazz) {
        CustomFile file;
        Iterator<CustomFile> customFileIterator = files.iterator();
        do {
            if (!customFileIterator.hasNext())
                return null;
            file = customFileIterator.next();
        } while (file.getClass() != clazz);
        return file;
    }

    public static abstract class CustomFile {
        private final File file;

        private final String name;

        private final boolean load;

        public CustomFile(String name, boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(FileManager.directory, name + ".celka");
            if (!this.file.exists())
                try {
                    saveFile();
                } catch (Exception e) {
                    e.printStackTrace();
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
