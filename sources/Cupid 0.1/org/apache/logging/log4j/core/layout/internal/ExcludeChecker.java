package org.apache.logging.log4j.core.layout.internal;

import java.util.List;

public class ExcludeChecker implements ListChecker {
  private final List<String> list;
  
  public ExcludeChecker(List<String> list) {
    this.list = list;
  }
  
  public boolean check(String key) {
    return !this.list.contains(key);
  }
  
  public String toString() {
    return "ThreadContextExcludes=" + this.list.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\internal\ExcludeChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */