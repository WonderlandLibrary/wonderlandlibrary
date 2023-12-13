package javassist.scopedpool;

import javassist.ClassPool;

public interface ScopedClassPoolFactory {
   ScopedClassPool create(ClassLoader var1, ClassPool var2, ScopedClassPoolRepository var3);

   ScopedClassPool create(ClassPool var1, ScopedClassPoolRepository var2);
}
