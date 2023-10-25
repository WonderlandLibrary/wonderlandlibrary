package org.apache.commons.io;

import java.io.File;

@Deprecated
public class FileCleaner {
  static final FileCleaningTracker theInstance = new FileCleaningTracker();
  
  @Deprecated
  public static void track(File file, Object marker) {
    theInstance.track(file, marker);
  }
  
  @Deprecated
  public static void track(File file, Object marker, FileDeleteStrategy deleteStrategy) {
    theInstance.track(file, marker, deleteStrategy);
  }
  
  @Deprecated
  public static void track(String path, Object marker) {
    theInstance.track(path, marker);
  }
  
  @Deprecated
  public static void track(String path, Object marker, FileDeleteStrategy deleteStrategy) {
    theInstance.track(path, marker, deleteStrategy);
  }
  
  @Deprecated
  public static int getTrackCount() {
    return theInstance.getTrackCount();
  }
  
  @Deprecated
  public static synchronized void exitWhenFinished() {
    theInstance.exitWhenFinished();
  }
  
  public static FileCleaningTracker getInstance() {
    return theInstance;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\FileCleaner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */