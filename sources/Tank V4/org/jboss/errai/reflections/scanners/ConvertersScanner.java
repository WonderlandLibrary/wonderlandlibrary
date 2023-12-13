package org.jboss.errai.reflections.scanners;

import java.util.Iterator;
import java.util.List;

public class ConvertersScanner extends AbstractScanner {
   public void scan(Object var1) {
      List var2 = this.getMetadataAdapter().getMethods(var1);
      Iterator var3 = var2.iterator();

      while(true) {
         Object var4;
         String var6;
         String var7;
         do {
            do {
               List var5;
               do {
                  if (!var3.hasNext()) {
                     return;
                  }

                  var4 = var3.next();
                  var5 = this.getMetadataAdapter().getParameterNames(var4);
               } while(var5.size() != 1);

               var6 = (String)var5.get(0);
               var7 = this.getMetadataAdapter().getReturnTypeName(var4);
            } while(var7.equals("void"));
         } while(!this.acceptResult(var6) && !this.acceptResult(var7));

         String var8 = this.getMetadataAdapter().getMethodFullKey(var1, var4);
         this.getStore().put(getConverterKey(var6, var7), var8);
      }
   }

   public static String getConverterKey(String var0, String var1) {
      return var0 + " to " + var1;
   }

   public static String getConverterKey(Class var0, Class var1) {
      return getConverterKey(var0.getName(), var1.getName());
   }
}
