package org.reflections.scanners;

import java.util.Iterator;

public class MethodAnnotationsScanner extends AbstractScanner {
   public void scan(Object var1) {
      Iterator var2 = this.getMetadataAdapter().getMethods(var1).iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         Iterator var4 = this.getMetadataAdapter().getMethodAnnotationNames(var3).iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            if (this.acceptResult(var5)) {
               this.getStore().put(var5, this.getMetadataAdapter().getMethodFullKey(var1, var3));
            }
         }
      }

   }
}
