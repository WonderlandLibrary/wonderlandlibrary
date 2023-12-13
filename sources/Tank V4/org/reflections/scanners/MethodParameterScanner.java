package org.reflections.scanners;

import java.util.Iterator;
import java.util.List;
import org.reflections.adapters.MetadataAdapter;

public class MethodParameterScanner extends AbstractScanner {
   public void scan(Object var1) {
      MetadataAdapter var2 = this.getMetadataAdapter();
      Iterator var3 = var2.getMethods(var1).iterator();

      while(var3.hasNext()) {
         Object var4 = var3.next();
         String var5 = var2.getParameterNames(var4).toString();
         if (this.acceptResult(var5)) {
            this.getStore().put(var5, var2.getMethodFullKey(var1, var4));
         }

         String var6 = var2.getReturnTypeName(var4);
         if (this.acceptResult(var6)) {
            this.getStore().put(var6, var2.getMethodFullKey(var1, var4));
         }

         List var7 = var2.getParameterNames(var4);

         for(int var8 = 0; var8 < var7.size(); ++var8) {
            Iterator var9 = var2.getParameterAnnotationNames(var4, var8).iterator();

            while(var9.hasNext()) {
               Object var10 = var9.next();
               if (this.acceptResult((String)var10)) {
                  this.getStore().put((String)var10, var2.getMethodFullKey(var1, var4));
               }
            }
         }
      }

   }
}
