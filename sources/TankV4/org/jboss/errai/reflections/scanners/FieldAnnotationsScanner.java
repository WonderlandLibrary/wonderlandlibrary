package org.jboss.errai.reflections.scanners;

import java.util.Iterator;
import java.util.List;

public class FieldAnnotationsScanner extends AbstractScanner {
   public void scan(Object var1) {
      String var2 = this.getMetadataAdapter().getClassName(var1);
      List var3 = this.getMetadataAdapter().getFields(var1);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         Object var5 = var4.next();
         List var6 = this.getMetadataAdapter().getFieldAnnotationNames(var5);
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            String var8 = (String)var7.next();
            if (this.acceptResult(var8)) {
               String var9 = this.getMetadataAdapter().getFieldName(var5);
               this.getStore().put(var8, String.format("%s.%s", var2, var9));
            }
         }
      }

   }
}
