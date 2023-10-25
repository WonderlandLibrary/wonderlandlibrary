package com.sun.jna.platform;

import com.sun.jna.platform.mac.MacFileUtils;
import com.sun.jna.platform.win32.W32FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileUtils {
  public boolean hasTrash() {
    return false;
  }
  
  public abstract void moveToTrash(File[] paramArrayOfFile) throws IOException;
  
  private static class Holder {
    public static final FileUtils INSTANCE;
    
    static {
      String os = System.getProperty("os.name");
      if (os.startsWith("Windows")) {
        INSTANCE = (FileUtils)new W32FileUtils();
      } else if (os.startsWith("Mac")) {
        INSTANCE = (FileUtils)new MacFileUtils();
      } else {
        INSTANCE = new FileUtils.DefaultFileUtils();
      } 
    }
  }
  
  public static FileUtils getInstance() {
    return Holder.INSTANCE;
  }
  
  private static class DefaultFileUtils extends FileUtils {
    private DefaultFileUtils() {}
    
    private File getTrashDirectory() {
      File home = new File(System.getProperty("user.home"));
      File trash = new File(home, ".Trash");
      if (!trash.exists()) {
        trash = new File(home, "Trash");
        if (!trash.exists()) {
          File desktop = new File(home, "Desktop");
          if (desktop.exists()) {
            trash = new File(desktop, ".Trash");
            if (!trash.exists()) {
              trash = new File(desktop, "Trash");
              if (!trash.exists())
                trash = new File(System.getProperty("fileutils.trash", "Trash")); 
            } 
          } 
        } 
      } 
      return trash;
    }
    
    public boolean hasTrash() {
      return getTrashDirectory().exists();
    }
    
    public void moveToTrash(File[] files) throws IOException {
      File trash = getTrashDirectory();
      if (!trash.exists())
        throw new IOException("No trash location found (define fileutils.trash to be the path to the trash)"); 
      List<File> failed = new ArrayList<File>();
      for (int i = 0; i < files.length; i++) {
        File src = files[i];
        File target = new File(trash, src.getName());
        if (!src.renameTo(target))
          failed.add(src); 
      } 
      if (failed.size() > 0)
        throw new IOException("The following files could not be trashed: " + failed); 
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\FileUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */