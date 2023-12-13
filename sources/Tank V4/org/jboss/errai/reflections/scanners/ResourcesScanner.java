package org.jboss.errai.reflections.scanners;

import org.jboss.errai.reflections.vfs.Vfs;

public class ResourcesScanner extends AbstractScanner {
   public boolean acceptsInput(String var1) {
      return !var1.endsWith(".class");
   }

   public void scan(Vfs.File var1) {
      this.getStore().put(var1.getName(), var1.getRelativePath());
   }

   public void scan(Object var1) {
      throw new UnsupportedOperationException();
   }
}
