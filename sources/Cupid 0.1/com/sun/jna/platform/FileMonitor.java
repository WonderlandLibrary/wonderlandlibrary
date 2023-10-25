package com.sun.jna.platform;

import com.sun.jna.platform.win32.W32FileMonitor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FileMonitor {
  public static final int FILE_CREATED = 1;
  
  public static final int FILE_DELETED = 2;
  
  public static final int FILE_MODIFIED = 4;
  
  public static final int FILE_ACCESSED = 8;
  
  public static final int FILE_NAME_CHANGED_OLD = 16;
  
  public static final int FILE_NAME_CHANGED_NEW = 32;
  
  public static final int FILE_RENAMED = 48;
  
  public static final int FILE_SIZE_CHANGED = 64;
  
  public static final int FILE_ATTRIBUTES_CHANGED = 128;
  
  public static final int FILE_SECURITY_CHANGED = 256;
  
  public static final int FILE_ANY = 511;
  
  public static interface FileListener {
    void fileChanged(FileMonitor.FileEvent param1FileEvent);
  }
  
  public class FileEvent extends EventObject {
    private final File file;
    
    private final int type;
    
    public FileEvent(File file, int type) {
      super(FileMonitor.this);
      this.file = file;
      this.type = type;
    }
    
    public File getFile() {
      return this.file;
    }
    
    public int getType() {
      return this.type;
    }
    
    public String toString() {
      return "FileEvent: " + this.file + ":" + this.type;
    }
  }
  
  private final Map<File, Integer> watched = new HashMap<File, Integer>();
  
  private List<FileListener> listeners = new ArrayList<FileListener>();
  
  protected abstract void watch(File paramFile, int paramInt, boolean paramBoolean) throws IOException;
  
  protected abstract void unwatch(File paramFile);
  
  public abstract void dispose();
  
  public void addWatch(File dir) throws IOException {
    addWatch(dir, 511);
  }
  
  public void addWatch(File dir, int mask) throws IOException {
    addWatch(dir, mask, dir.isDirectory());
  }
  
  public void addWatch(File dir, int mask, boolean recursive) throws IOException {
    this.watched.put(dir, new Integer(mask));
    watch(dir, mask, recursive);
  }
  
  public void removeWatch(File file) {
    if (this.watched.remove(file) != null)
      unwatch(file); 
  }
  
  protected void notify(FileEvent e) {
    for (FileListener listener : this.listeners)
      listener.fileChanged(e); 
  }
  
  public synchronized void addFileListener(FileListener listener) {
    List<FileListener> list = new ArrayList<FileListener>(this.listeners);
    list.add(listener);
    this.listeners = list;
  }
  
  public synchronized void removeFileListener(FileListener x) {
    List<FileListener> list = new ArrayList<FileListener>(this.listeners);
    list.remove(x);
    this.listeners = list;
  }
  
  protected void finalize() {
    for (File watchedFile : this.watched.keySet())
      removeWatch(watchedFile); 
    dispose();
  }
  
  private static class Holder {
    public static final FileMonitor INSTANCE;
    
    static {
      String os = System.getProperty("os.name");
      if (os.startsWith("Windows")) {
        INSTANCE = (FileMonitor)new W32FileMonitor();
      } else {
        throw new Error("FileMonitor not implemented for " + os);
      } 
    }
  }
  
  public static FileMonitor getInstance() {
    return Holder.INSTANCE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\FileMonitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */