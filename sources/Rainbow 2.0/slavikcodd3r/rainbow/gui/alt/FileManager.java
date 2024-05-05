package slavikcodd3r.rainbow.gui.alt;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import java.io.File;
import java.util.ArrayList;

public class FileManager
{
    public static ArrayList<CustomFile> Files;
    private static File directory;
    
    public FileManager() {
        super();
        this.makeDirectories();
        FileManager.Files.add(new Alts("Accounts.rainbow", false, true));
    }
    
    public void loadFiles() {
        for (final CustomFile f : FileManager.Files) {
            try {
                if (!f.loadOnStart()) {
                    continue;
                }
                f.loadFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void saveFiles() {
        for (final CustomFile f : FileManager.Files) {
            try {
                f.saveFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public CustomFile getFile(final Class<? extends CustomFile> clazz) {
        for (final CustomFile file : FileManager.Files) {
            if (file.getClass() != clazz) {
                continue;
            }
            return file;
        }
        return null;
    }
    
    public void makeDirectories() {
        if (!FileManager.directory.exists()) {
            if (FileManager.directory.mkdir()) {
                System.out.println("Directory is created!");
            }
            else {
                System.out.println("Failed to create directory!");
            }
        }
    }
    
    static /* synthetic */ File access$100() {
        return FileManager.directory;
    }
    
    static {
        FileManager.Files = new ArrayList<CustomFile>();
        FileManager.directory = new File(String.valueOf(Minecraft.getMinecraft().mcDataDir.toString()) + "\\" + "ArthimoWare");
    }
    
    public abstract static class CustomFile
    {
        private final File file;
        private final String name;
        private boolean load;
        
        public CustomFile(final String name, final boolean Module2, final boolean loadOnStart) {
            super();
            this.name = name;
            this.load = loadOnStart;
            this.file = new File(FileManager.directory, String.valueOf(name) + ".txt");
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
        
        static /* synthetic */ boolean access$000(final CustomFile x0) {
            return x0.loadOnStart();
        }
    }
}
