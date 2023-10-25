package com.ibm.icu.text;

import com.ibm.icu.util.ULocale;

public abstract class MeasureFormat extends UFormat {
  static final long serialVersionUID = -7182021401701778240L;
  
  public static MeasureFormat getCurrencyFormat(ULocale locale) {
    return new CurrencyFormat(locale);
  }
  
  public static MeasureFormat getCurrencyFormat() {
    return getCurrencyFormat(ULocale.getDefault(ULocale.Category.FORMAT));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\MeasureFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */