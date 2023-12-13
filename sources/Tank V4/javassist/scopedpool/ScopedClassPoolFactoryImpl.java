package javassist.scopedpool;

import javassist.ClassPool;

public class ScopedClassPoolFactoryImpl implements ScopedClassPoolFactory {
   public ScopedClassPool create(ClassLoader var1, ClassPool var2, ScopedClassPoolRepository var3) {
      return new ScopedClassPool(var1, var2, var3, false);
   }

   public ScopedClassPool create(ClassPool var1, ScopedClassPoolRepository var2) {
      return new ScopedClassPool((ClassLoader)null, var1, var2, true);
   }
}
