package org.reflections.scanners;

import java.lang.annotation.Inherited;
import java.util.Iterator;

public class TypeAnnotationsScanner extends AbstractScanner {
   public void scan(Object var1) {
      String var2 = this.getMetadataAdapter().getClassName(var1);
      Iterator var3 = this.getMetadataAdapter().getClassAnnotationNames(var1).iterator();

      while(true) {
         String var4;
         do {
            if (!var3.hasNext()) {
               return;
            }

            var4 = (String)var3.next();
         } while(!this.acceptResult(var4) && !var4.equals(Inherited.class.getName()));

         this.getStore().put(var4, var2);
      }
   }
}
