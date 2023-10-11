package cow.milkgod.cheese.files;

import net.minecraft.client.*;
import cow.milkgod.cheese.*;
import cow.milkgod.cheese.files.files.modulefiles.*;
import cow.milkgod.cheese.files.files.*;
import java.util.*;
import cow.milkgod.cheese.properties.*;
import java.io.*;

public class FileManager
{
    public static ArrayList<CustomFile> Files;
    private static File directory;
    private static File moduleDirectory;
    
    static {
        FileManager.Files = new ArrayList<CustomFile>();
        FileManager.directory = new File(String.valueOf(String.valueOf(Minecraft.getMinecraft().mcDataDir.toString())) + "\\" + Cheese.getClient_Name());
        FileManager.moduleDirectory = new File(String.valueOf(String.valueOf(Minecraft.getMinecraft().mcDataDir.toString())) + "\\" + Cheese.getClient_Name() + "\\" + "Modules");
    }
    

    public FileManager() {
       this.makeDirectories();
       Files.add(new Friends("Friends", false, true));
       Files.add(new Alts("alts", false, true));
       Files.add(new Modules("Modules", true, false));
       Files.add(new Binds("Binds", true, true));
       Files.add(new Movement("Movement", true, true));
       Files.add(new Combat("Combat", true, true));
       Files.add(new Exploits("Exploits", true, true));
       Files.add(new Render("Render", true, true));
       Files.add(new Misc("Misc", false, true));
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
                if (f.getClass() == Modules.class) {
                    continue;
                }
                f.saveFile();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public CustomFile getFile(final Class<? extends CustomFile> clazz) {
        for (final CustomFile file : FileManager.Files) {
            if (file.getClass() == clazz) {
                return file;
            }
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
        if (!FileManager.moduleDirectory.exists()) {
            if (FileManager.moduleDirectory.mkdir()) {
                System.out.println("Directory is created!");
            }
            else {
                System.out.println("Failed to create directory!");
            }
        }
    }
    
    public abstract static class CustomFile
    {
        private final File file;
        private final String name;
        private boolean load;
        
        public CustomFile(final String name, final boolean Module, final boolean loadOnStart) {
            this.name = name;
            this.load = loadOnStart;
            if (Module) {
                this.file = new File(FileManager.moduleDirectory, String.valueOf(String.valueOf(name)) + ".txt");
            }
            else {
                this.file = new File(FileManager.directory, String.valueOf(String.valueOf(name)) + ".txt");
            }
            if (!this.file.exists()) {
                try {
                    this.saveFile();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        public void setValue(Property p2, String value) {
            if (p2.value instanceof Integer) {
                p2.value = Integer.valueOf(value);
            } else if (p2.value instanceof Boolean) {
                p2.value = Boolean.valueOf(value);
            } else if (p2.value instanceof Double) {
                p2.value = Double.valueOf(value);
            } else if (p2.value instanceof Float) {
                p2.value = Float.valueOf(value);
            } else if (p2.value instanceof Long) {
                p2.value = Long.parseLong(value);
            } else if (p2.value instanceof String) {
                p2.value = value;
            } else {
                p2.reset();
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
