package javassist.scopedpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.LoaderClassPath;

public class ScopedClassPoolRepositoryImpl implements ScopedClassPoolRepository {
   private static final ScopedClassPoolRepositoryImpl instance = new ScopedClassPoolRepositoryImpl();
   private boolean prune = true;
   boolean pruneWhenCached;
   protected Map registeredCLs = Collections.synchronizedMap(new WeakHashMap());
   protected ClassPool classpool = ClassPool.getDefault();
   protected ScopedClassPoolFactory factory = new ScopedClassPoolFactoryImpl();

   public static ScopedClassPoolRepository getInstance() {
      return instance;
   }

   private ScopedClassPoolRepositoryImpl() {
      ClassLoader var1 = Thread.currentThread().getContextClassLoader();
      this.classpool.insertClassPath((ClassPath)(new LoaderClassPath(var1)));
   }

   public boolean isPrune() {
      return this.prune;
   }

   public void setPrune(boolean var1) {
      this.prune = var1;
   }

   public ScopedClassPool createScopedClassPool(ClassLoader var1, ClassPool var2) {
      return this.factory.create(var1, var2, this);
   }

   public ClassPool findClassPool(ClassLoader var1) {
      return var1 == null ? this.registerClassLoader(ClassLoader.getSystemClassLoader()) : this.registerClassLoader(var1);
   }

   public ClassPool registerClassLoader(ClassLoader var1) {
      Map var2;
      synchronized(var2 = this.registeredCLs){}
      if (this.registeredCLs.containsKey(var1)) {
         return (ClassPool)this.registeredCLs.get(var1);
      } else {
         ScopedClassPool var3 = this.createScopedClassPool(var1, this.classpool);
         this.registeredCLs.put(var1, var3);
         return var3;
      }
   }

   public Map getRegisteredCLs() {
      this.clearUnregisteredClassLoaders();
      return this.registeredCLs;
   }

   public void clearUnregisteredClassLoaders() {
      ArrayList var1 = null;
      Map var2;
      synchronized(var2 = this.registeredCLs){}
      Iterator var3 = this.registeredCLs.values().iterator();

      while(var3.hasNext()) {
         ScopedClassPool var4 = (ScopedClassPool)var3.next();
         if (var4.isUnloadedClassLoader()) {
            var3.remove();
            ClassLoader var5 = var4.getClassLoader();
            if (var5 != null) {
               if (var1 == null) {
                  var1 = new ArrayList();
               }

               var1.add(var5);
            }
         }
      }

      if (var1 != null) {
         for(int var7 = 0; var7 < var1.size(); ++var7) {
            this.unregisterClassLoader((ClassLoader)var1.get(var7));
         }
      }

   }

   public void unregisterClassLoader(ClassLoader var1) {
      Map var2;
      synchronized(var2 = this.registeredCLs){}
      ScopedClassPool var3 = (ScopedClassPool)this.registeredCLs.remove(var1);
      if (var3 != null) {
         var3.close();
      }

   }

   public void insertDelegate(ScopedClassPoolRepository var1) {
   }

   public void setClassPoolFactory(ScopedClassPoolFactory var1) {
      this.factory = var1;
   }

   public ScopedClassPoolFactory getClassPoolFactory() {
      return this.factory;
   }
}
