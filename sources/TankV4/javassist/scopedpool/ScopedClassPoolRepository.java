package javassist.scopedpool;

import java.util.Map;
import javassist.ClassPool;

public interface ScopedClassPoolRepository {
   void setClassPoolFactory(ScopedClassPoolFactory var1);

   ScopedClassPoolFactory getClassPoolFactory();

   boolean isPrune();

   void setPrune(boolean var1);

   ScopedClassPool createScopedClassPool(ClassLoader var1, ClassPool var2);

   ClassPool findClassPool(ClassLoader var1);

   ClassPool registerClassLoader(ClassLoader var1);

   Map getRegisteredCLs();

   void clearUnregisteredClassLoaders();

   void unregisterClassLoader(ClassLoader var1);
}
