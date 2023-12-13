package org.reflections.scanners;

import org.reflections.vfs.Vfs;

/** @deprecated */
@Deprecated
public class TypesScanner extends AbstractScanner {
   public Object scan(Vfs.File var1, Object var2) {
      var2 = super.scan(var1, var2);
      String var3 = this.getMetadataAdapter().getClassName(var2);
      this.getStore().put(var3, var3);
      return var2;
   }

   public void scan(Object var1) {
      throw new UnsupportedOperationException("should not get here");
   }
}
