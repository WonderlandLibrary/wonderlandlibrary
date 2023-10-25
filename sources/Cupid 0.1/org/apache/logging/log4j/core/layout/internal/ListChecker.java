package org.apache.logging.log4j.core.layout.internal;

public interface ListChecker {
  public static final NoopChecker NOOP_CHECKER = new NoopChecker();
  
  boolean check(String paramString);
  
  public static class NoopChecker implements ListChecker {
    public boolean check(String key) {
      return true;
    }
    
    public String toString() {
      return "";
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\layout\internal\ListChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */