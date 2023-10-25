package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class NotFileFilter extends AbstractFileFilter implements Serializable {
  private final IOFileFilter filter;
  
  public NotFileFilter(IOFileFilter filter) {
    if (filter == null)
      throw new IllegalArgumentException("The filter must not be null"); 
    this.filter = filter;
  }
  
  public boolean accept(File file) {
    return !this.filter.accept(file);
  }
  
  public boolean accept(File file, String name) {
    return !this.filter.accept(file, name);
  }
  
  public String toString() {
    return super.toString() + "(" + this.filter.toString() + ")";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\filefilter\NotFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */