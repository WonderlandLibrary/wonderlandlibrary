package org.jboss.errai.reflections.scanners;

import java.util.Iterator;
import java.util.List;

public class MethodParametersAnnotationsScanner extends AbstractScanner {
   public void scan(Object var1) {
      String var2 = this.getMetadataAdapter().getClassName(var1);
      List var3 = this.getMetadataAdapter().getMethods(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         List var6 = this.getMetadataAdapter().getParameterNames(var5);

         for(int var7 = 0; var7 < var6.size(); ++var7) {
            List var8 = this.getMetadataAdapter().getParameterAnnotationNames(var5, var7);
            Iterator var9 = var8.iterator();

            while(var9.hasNext()) {
               String var10 = (String)var9.next();
               if (this.acceptResult(var10)) {
                  this.getStore().put(var10, String.format("%s.%s:%s %s", var2, var5, var6.get(var7), var10));
               }
            }
         }
      }

   }
}
