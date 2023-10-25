package com.ibm.icu.util;

import com.ibm.icu.lang.UCharacter;

public class CaseInsensitiveString {
  private String string;
  
  private int hash = 0;
  
  private String folded = null;
  
  private static String foldCase(String foldee) {
    return UCharacter.foldCase(foldee, true);
  }
  
  private void getFolded() {
    if (this.folded == null)
      this.folded = foldCase(this.string); 
  }
  
  public CaseInsensitiveString(String s) {
    this.string = s;
  }
  
  public String getString() {
    return this.string;
  }
  
  public boolean equals(Object o) {
    if (o == null)
      return false; 
    if (this == o)
      return true; 
    getFolded();
    try {
      CaseInsensitiveString cis = (CaseInsensitiveString)o;
      cis.getFolded();
      return this.folded.equals(cis.folded);
    } catch (ClassCastException e) {
      try {
        String s = (String)o;
        return this.folded.equals(foldCase(s));
      } catch (ClassCastException e2) {
        return false;
      } 
    } 
  }
  
  public int hashCode() {
    getFolded();
    if (this.hash == 0)
      this.hash = this.folded.hashCode(); 
    return this.hash;
  }
  
  public String toString() {
    return this.string;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\CaseInsensitiveString.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */