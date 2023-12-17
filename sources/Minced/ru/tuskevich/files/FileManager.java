// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.files;

import java.util.Iterator;
import java.io.IOException;
import ru.tuskevich.files.impl.AltConfig;
import java.util.ArrayList;
import java.io.File;

public class FileManager
{
    public static final File directory;
    public static ArrayList<CustomFile> files;
    
    public FileManager() {
        FileManager.files.add(new AltConfig("accounts", true));
    }
    
    public void init() throws IOException {
        if (!FileManager.directory.exists()) {
            FileManager.directory.mkdirs();
        }
        else {
            this.loadFiles();
        }
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
        directory = new File("C:\\Minced\\game\\minced\\");
        FileManager.files = new ArrayList<CustomFile>();
    }
    
    public abstract static class CustomFile
    {
        private final File file;
        private final String name;
        private final boolean load;
        
        public CustomFile(final String name, final boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(FileManager.directory, name + ".pon");
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
