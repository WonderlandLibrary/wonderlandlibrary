// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.files;

import java.io.IOException;
import ru.fluger.client.Fluger;
import java.util.Iterator;
import ru.fluger.client.files.impl.MacroConfig;
import ru.fluger.client.files.impl.HudConfig;
import ru.fluger.client.files.impl.FriendConfig;
import ru.fluger.client.files.impl.AltConfig;
import java.util.ArrayList;
import java.io.File;

public class FileManager
{
    private static final File directory;
    public static ArrayList<CustomFile> files;
    
    public FileManager() {
        FileManager.files.add(new AltConfig("AltConfig", true));
        FileManager.files.add(new FriendConfig("FriendConfig", true));
        FileManager.files.add(new HudConfig("HudConfig", true));
        FileManager.files.add(new MacroConfig("MacroConfig", true));
    }
    
    public void loadFiles() {
        for (final CustomFile file : FileManager.files) {
            try {
                if (!file.loadOnStart()) {
                    continue;
                }
                file.loadFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void saveFiles() {
        for (final CustomFile f : FileManager.files) {
            try {
                f.saveFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public CustomFile getFile(final Class<?> clazz) {
        for (final CustomFile file : FileManager.files) {
            if (file.getClass() == clazz) {
                return file;
            }
        }
        return null;
    }
    
    static {
        FileManager.files = new ArrayList<CustomFile>();
        directory = new File(Fluger.name);
    }
    
    public abstract static class CustomFile
    {
        private final File file;
        private final String name;
        private final boolean load;
        
        public CustomFile(final String name, final boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(FileManager.directory, name + ".json");
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
