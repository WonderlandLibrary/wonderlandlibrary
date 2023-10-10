package com.ibm.icu.text;

import com.ibm.icu.util.ULocale;
import java.text.Format;

public abstract class UFormat extends Format {
   private static final long serialVersionUID = -4964390515840164416L;
   private ULocale validLocale;
   private ULocale actualLocale;

   public final ULocale getLocale(ULocale.Type var1) {
      return var1 == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
   }

   final void setLocale(ULocale var1, ULocale var2) {
      if (var1 == null != (var2 == null)) {
         throw new IllegalArgumentException();
      } else {
         this.validLocale = var1;
         this.actualLocale = var2;
      }
   }
}
