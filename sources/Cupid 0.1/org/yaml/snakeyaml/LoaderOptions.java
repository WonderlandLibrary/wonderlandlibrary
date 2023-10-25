package org.yaml.snakeyaml;

public class LoaderOptions {
  private boolean allowDuplicateKeys = true;
  
  private boolean wrappedToRootException = false;
  
  private int maxAliasesForCollections = 50;
  
  private boolean allowRecursiveKeys = false;
  
  public boolean isAllowDuplicateKeys() {
    return this.allowDuplicateKeys;
  }
  
  public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
    this.allowDuplicateKeys = allowDuplicateKeys;
  }
  
  public boolean isWrappedToRootException() {
    return this.wrappedToRootException;
  }
  
  public void setWrappedToRootException(boolean wrappedToRootException) {
    this.wrappedToRootException = wrappedToRootException;
  }
  
  public int getMaxAliasesForCollections() {
    return this.maxAliasesForCollections;
  }
  
  public void setMaxAliasesForCollections(int maxAliasesForCollections) {
    this.maxAliasesForCollections = maxAliasesForCollections;
  }
  
  public void setAllowRecursiveKeys(boolean allowRecursiveKeys) {
    this.allowRecursiveKeys = allowRecursiveKeys;
  }
  
  public boolean getAllowRecursiveKeys() {
    return this.allowRecursiveKeys;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\LoaderOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */