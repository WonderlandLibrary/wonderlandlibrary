package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable {
  private final List<IOFileFilter> fileFilters;
  
  public AndFileFilter() {
    this.fileFilters = new ArrayList<IOFileFilter>();
  }
  
  public AndFileFilter(List<IOFileFilter> fileFilters) {
    if (fileFilters == null) {
      this.fileFilters = new ArrayList<IOFileFilter>();
    } else {
      this.fileFilters = new ArrayList<IOFileFilter>(fileFilters);
    } 
  }
  
  public AndFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
    if (filter1 == null || filter2 == null)
      throw new IllegalArgumentException("The filters must not be null"); 
    this.fileFilters = new ArrayList<IOFileFilter>(2);
    addFileFilter(filter1);
    addFileFilter(filter2);
  }
  
  public void addFileFilter(IOFileFilter ioFileFilter) {
    this.fileFilters.add(ioFileFilter);
  }
  
  public List<IOFileFilter> getFileFilters() {
    return Collections.unmodifiableList(this.fileFilters);
  }
  
  public boolean removeFileFilter(IOFileFilter ioFileFilter) {
    return this.fileFilters.remove(ioFileFilter);
  }
  
  public void setFileFilters(List<IOFileFilter> fileFilters) {
    this.fileFilters.clear();
    this.fileFilters.addAll(fileFilters);
  }
  
  public boolean accept(File file) {
    if (this.fileFilters.isEmpty())
      return false; 
    for (IOFileFilter fileFilter : this.fileFilters) {
      if (!fileFilter.accept(file))
        return false; 
    } 
    return true;
  }
  
  public boolean accept(File file, String name) {
    if (this.fileFilters.isEmpty())
      return false; 
    for (IOFileFilter fileFilter : this.fileFilters) {
      if (!fileFilter.accept(file, name))
        return false; 
    } 
    return true;
  }
  
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append(super.toString());
    buffer.append("(");
    if (this.fileFilters != null)
      for (int i = 0; i < this.fileFilters.size(); i++) {
        if (i > 0)
          buffer.append(","); 
        Object filter = this.fileFilters.get(i);
        buffer.append((filter == null) ? "null" : filter.toString());
      }  
    buffer.append(")");
    return buffer.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\filefilter\AndFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */