package org.apache.logging.log4j.core.layout.internal;

import java.util.List;

public class IncludeChecker implements ListChecker {
  private final List<String> list;
  
  public IncludeChecker(List<String> list) {
    this.list = list;
  }
  
  public boolean check(String key) {
    return this.list.contains(key);
  }
  
  public String toString() {
    return "ThreadContextIncludes=" + this.list.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\internal\IncludeChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */