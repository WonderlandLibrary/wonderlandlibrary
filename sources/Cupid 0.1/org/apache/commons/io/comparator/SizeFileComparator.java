package org.apache.commons.io.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class SizeFileComparator extends AbstractFileComparator implements Serializable {
  public static final Comparator<File> SIZE_COMPARATOR = new SizeFileComparator();
  
  public static final Comparator<File> SIZE_REVERSE = new ReverseComparator(SIZE_COMPARATOR);
  
  public static final Comparator<File> SIZE_SUMDIR_COMPARATOR = new SizeFileComparator(true);
  
  public static final Comparator<File> SIZE_SUMDIR_REVERSE = new ReverseComparator(SIZE_SUMDIR_COMPARATOR);
  
  private final boolean sumDirectoryContents;
  
  public SizeFileComparator() {
    this.sumDirectoryContents = false;
  }
  
  public SizeFileComparator(boolean sumDirectoryContents) {
    this.sumDirectoryContents = sumDirectoryContents;
  }
  
  public int compare(File file1, File file2) {
    long size1 = 0L;
    if (file1.isDirectory()) {
      size1 = (this.sumDirectoryContents && file1.exists()) ? FileUtils.sizeOfDirectory(file1) : 0L;
    } else {
      size1 = file1.length();
    } 
    long size2 = 0L;
    if (file2.isDirectory()) {
      size2 = (this.sumDirectoryContents && file2.exists()) ? FileUtils.sizeOfDirectory(file2) : 0L;
    } else {
      size2 = file2.length();
    } 
    long result = size1 - size2;
    if (result < 0L)
      return -1; 
    if (result > 0L)
      return 1; 
    return 0;
  }
  
  public String toString() {
    return super.toString() + "[sumDirectoryContents=" + this.sumDirectoryContents + "]";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\comparator\SizeFileComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */