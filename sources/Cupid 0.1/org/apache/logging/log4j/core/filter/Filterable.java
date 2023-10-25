package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.LogEvent;

public interface Filterable extends LifeCycle {
  void addFilter(Filter paramFilter);
  
  void removeFilter(Filter paramFilter);
  
  Filter getFilter();
  
  boolean hasFilter();
  
  boolean isFiltered(LogEvent paramLogEvent);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\filter\Filterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */