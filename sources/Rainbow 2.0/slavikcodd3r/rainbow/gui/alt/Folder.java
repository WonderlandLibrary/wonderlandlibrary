package slavikcodd3r.rainbow.gui.alt;

import java.io.File;

public class Folder
{
    public static Folder instance;
    private static FileManager fileManager;
    private File dataDirectory;
    
    public static FileManager getFileManager() {
        return Folder.fileManager;
    }
    
    public Folder() {
    }
       
    public void setup() {
        this.dataDirectory = new File("Rainbow 2.0");
    }
      
    public static File getDataDir() {
        return Folder.instance.dataDirectory;
    }
    
}
