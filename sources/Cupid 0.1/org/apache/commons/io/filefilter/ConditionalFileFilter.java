package org.apache.commons.io.filefilter;

import java.util.List;

public interface ConditionalFileFilter {
  void addFileFilter(IOFileFilter paramIOFileFilter);
  
  List<IOFileFilter> getFileFilters();
  
  boolean removeFileFilter(IOFileFilter paramIOFileFilter);
  
  void setFileFilters(List<IOFileFilter> paramList);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\filefilter\ConditionalFileFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */