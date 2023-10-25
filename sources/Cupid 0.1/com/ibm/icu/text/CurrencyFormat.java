package com.ibm.icu.text;

import com.ibm.icu.util.CurrencyAmount;
import com.ibm.icu.util.ULocale;
import java.text.FieldPosition;
import java.text.ParsePosition;

class CurrencyFormat extends MeasureFormat {
  static final long serialVersionUID = -931679363692504634L;
  
  private NumberFormat fmt;
  
  public CurrencyFormat(ULocale locale) {
    this.fmt = NumberFormat.getCurrencyInstance(locale.toLocale());
  }
  
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
    try {
      CurrencyAmount currency = (CurrencyAmount)obj;
      this.fmt.setCurrency(currency.getCurrency());
      return this.fmt.format(currency.getNumber(), toAppendTo, pos);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Invalid type: " + obj.getClass().getName());
    } 
  }
  
  public Object parseObject(String source, ParsePosition pos) {
    return this.fmt.parseCurrency(source, pos);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CurrencyFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */