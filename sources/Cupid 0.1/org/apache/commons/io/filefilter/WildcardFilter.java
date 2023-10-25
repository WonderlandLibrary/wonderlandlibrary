package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

@Deprecated
public class WildcardFilter extends AbstractFileFilter implements Serializable {
  private final String[] wildcards;
  
  public WildcardFilter(String wildcard) {
    if (wildcard == null)
      throw new IllegalArgumentException("The wildcard must not be null"); 
    this.wildcards = new String[] { wildcard };
  }
  
  public WildcardFilter(String[] wildcards) {
    if (wildcards == null)
      throw new IllegalArgumentException("The wildcard array must not be null"); 
    this.wildcards = new String[wildcards.length];
    System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
  }
  
  public WildcardFilter(List<String> wildcards) {
    if (wildcards == null)
      throw new IllegalArgumentException("The wildcard list must not be null"); 
    this.wildcards = wildcards.<String>toArray(new String[wildcards.size()]);
  }
  
  public boolean accept(File dir, String name) {
    if (dir != null && (new File(dir, name)).isDirectory())
      return false; 
    for (String wildcard : this.wildcards) {
      if (FilenameUtils.wildcardMatch(name, wildcard))
        return true; 
    } 
    return false;
  }
  
  public boolean accept(File file) {
    if (file.isDirectory())
      return false; 
    for (String wildcard : this.wildcards) {
      if (FilenameUtils.wildcardMatch(file.getName(), wildcard))
        return true; 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\io\filefilter\WildcardFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */