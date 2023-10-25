package com.ibm.icu.text;

import com.ibm.icu.util.ULocale;
import java.text.Format;

public abstract class UFormat extends Format {
  private static final long serialVersionUID = -4964390515840164416L;
  
  private ULocale validLocale;
  
  private ULocale actualLocale;
  
  public final ULocale getLocale(ULocale.Type type) {
    return (type == ULocale.ACTUAL_LOCALE) ? this.actualLocale : this.validLocale;
  }
  
  final void setLocale(ULocale valid, ULocale actual) {
    if (((valid == null) ? true : false) != ((actual == null) ? true : false))
      throw new IllegalArgumentException(); 
    this.validLocale = valid;
    this.actualLocale = actual;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */