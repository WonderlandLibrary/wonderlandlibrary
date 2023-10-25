package org.apache.commons.io.comparator;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

abstract class AbstractFileComparator implements Comparator<File> {
  public File[] sort(File... files) {
    if (files != null)
      Arrays.sort(files, this); 
    return files;
  }
  
  public List<File> sort(List<File> files) {
    if (files != null)
      Collections.sort(files, this); 
    return files;
  }
  
  public String toString() {
    return getClass().getSimpleName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\comparator\AbstractFileComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */