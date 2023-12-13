package org.jboss.errai.reflections.scanners;

import java.util.Iterator;

public class TypeElementsScanner extends AbstractScanner {
   public void scan(Object var1) {
      if (!TypesScanner.isJavaCodeSerializer(this.getMetadataAdapter().getInterfacesNames(var1))) {
         String var2 = this.getMetadataAdapter().getClassName(var1);
         Iterator var3 = this.getMetadataAdapter().getFields(var1).iterator();

         Object var4;
         while(var3.hasNext()) {
            var4 = var3.next();
            String var5 = this.getMetadataAdapter().getFieldName(var4);
            this.getStore().put(var2, var5);
         }

         var3 = this.getMetadataAdapter().getMethods(var1).iterator();

         while(var3.hasNext()) {
            var4 = var3.next();
            this.getStore().put(var2, this.getMetadataAdapter().getMethodKey(var1, var4));
         }

      }
   }
}
