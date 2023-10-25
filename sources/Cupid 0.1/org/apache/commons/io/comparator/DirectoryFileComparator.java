package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class DirectoryFileComparator extends AbstractFileComparator implements Serializable {
  public static final Comparator<File> DIRECTORY_COMPARATOR = new DirectoryFileComparator();
  
  public static final Comparator<File> DIRECTORY_REVERSE = new ReverseComparator(DIRECTORY_COMPARATOR);
  
  public int compare(File file1, File file2) {
    return getType(file1) - getType(file2);
  }
  
  private int getType(File file) {
    if (file.isDirectory())
      return 1; 
    return 2;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\comparator\DirectoryFileComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */