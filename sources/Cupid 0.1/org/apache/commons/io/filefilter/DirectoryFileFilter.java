package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class DirectoryFileFilter extends AbstractFileFilter implements Serializable {
  public static final IOFileFilter DIRECTORY = new DirectoryFileFilter();
  
  public static final IOFileFilter INSTANCE = DIRECTORY;
  
  public boolean accept(File file) {
    return file.isDirectory();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\filefilter\DirectoryFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */