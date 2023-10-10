package org.jboss.errai.reflections.scanners;

import java.util.Iterator;
import org.jboss.errai.reflections.util.FilterBuilder;

public class SubTypesScanner extends AbstractScanner {
   public SubTypesScanner() {
      this.filterResultsBy((new FilterBuilder()).exclude(Object.class.getName()));
   }

   public void scan(Object var1) {
      String var2 = this.getMetadataAdapter().getClassName(var1);
      String var3 = this.getMetadataAdapter().getSuperclassName(var1);
      if (this.acceptResult(var3)) {
         this.getStore().put(var3, var2);
      }

      Iterator var4 = this.getMetadataAdapter().getInterfacesNames(var1).iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         if (this.acceptResult(var5)) {
            this.getStore().put(var5, var2);
         }
      }

   }
}
