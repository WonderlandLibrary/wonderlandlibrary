package com.ibm.icu.text;

import com.ibm.icu.util.CurrencyAmount;
import com.ibm.icu.util.ULocale;
import java.text.FieldPosition;
import java.text.ParsePosition;

class CurrencyFormat extends MeasureFormat {
   static final long serialVersionUID = -931679363692504634L;
   private NumberFormat fmt;

   public CurrencyFormat(ULocale var1) {
      this.fmt = NumberFormat.getCurrencyInstance(var1.toLocale());
   }

   public StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      try {
         CurrencyAmount var4 = (CurrencyAmount)var1;
         this.fmt.setCurrency(var4.getCurrency());
         return this.fmt.format((Object)var4.getNumber(), var2, var3);
      } catch (ClassCastException var5) {
         throw new IllegalArgumentException("Invalid type: " + var1.getClass().getName());
      }
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return this.fmt.parseCurrency(var1, var2);
   }
}
