package javassist.scopedpool;

import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.Iterator;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class ScopedClassPool extends ClassPool {
   protected ScopedClassPoolRepository repository;
   protected WeakReference classLoader;
   protected LoaderClassPath classPath;
   protected SoftValueHashMap softcache;
   boolean isBootstrapCl;

   /** @deprecated */
   protected ScopedClassPool(ClassLoader var1, ClassPool var2, ScopedClassPoolRepository var3) {
      this(var1, var2, var3, false);
   }

   protected ScopedClassPool(ClassLoader var1, ClassPool var2, ScopedClassPoolRepository var3, boolean var4) {
      super(var2);
      this.softcache = new SoftValueHashMap();
      this.isBootstrapCl = true;
      this.repository = var3;
      this.classLoader = new WeakReference(var1);
      if (var1 != null) {
         this.classPath = new LoaderClassPath(var1);
         this.insertClassPath(this.classPath);
      }

      this.childFirstLookup = true;
      if (!var4 && var1 == null) {
         this.isBootstrapCl = true;
      }

   }

   public ClassLoader getClassLoader() {
      ClassLoader var1 = this.getClassLoader0();
      if (var1 == null && !this.isBootstrapCl) {
         throw new IllegalStateException("ClassLoader has been garbage collected");
      } else {
         return var1;
      }
   }

   protected ClassLoader getClassLoader0() {
      return (ClassLoader)this.classLoader.get();
   }

   public void close() {
      this.removeClassPath(this.classPath);
      this.classPath.close();
      this.classes.clear();
      this.softcache.clear();
   }

   public synchronized void flushClass(String var1) {
      this.classes.remove(var1);
      this.softcache.remove(var1);
   }

   public synchronized void soften(CtClass var1) {
      if (this.repository.isPrune()) {
         var1.prune();
      }

      this.classes.remove(var1.getName());
      this.softcache.put(var1.getName(), var1);
   }

   public boolean isUnloadedClassLoader() {
      return false;
   }

   protected CtClass getCached(String var1) {
      CtClass var2 = this.getCachedLocally(var1);
      if (var2 == null) {
         boolean var3 = false;
         ClassLoader var4 = this.getClassLoader0();
         if (var4 != null) {
            int var5 = var1.lastIndexOf(36);
            String var6 = null;
            if (var5 < 0) {
               var6 = var1.replaceAll("[\\.]", "/") + ".class";
            } else {
               var6 = var1.substring(0, var5).replaceAll("[\\.]", "/") + var1.substring(var5) + ".class";
            }

            var3 = var4.getResource(var6) != null;
         }

         if (!var3) {
            Map var10 = this.repository.getRegisteredCLs();
            synchronized(var10){}
            Iterator var7 = var10.values().iterator();

            while(var7.hasNext()) {
               ScopedClassPool var8 = (ScopedClassPool)var7.next();
               if (var8.isUnloadedClassLoader()) {
                  this.repository.unregisterClassLoader(var8.getClassLoader());
               } else {
                  var2 = var8.getCachedLocally(var1);
                  if (var2 != null) {
                     return var2;
                  }
               }
            }
         }
      }

      return var2;
   }

   protected void cacheCtClass(String var1, CtClass var2, boolean var3) {
      if (var3) {
         super.cacheCtClass(var1, var2, var3);
      } else {
         if (this.repository.isPrune()) {
            var2.prune();
         }

         this.softcache.put(var1, var2);
      }

   }

   public void lockInCache(CtClass var1) {
      super.cacheCtClass(var1.getName(), var1, false);
   }

   protected CtClass getCachedLocally(String var1) {
      CtClass var2 = (CtClass)this.classes.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         SoftValueHashMap var3;
         synchronized(var3 = this.softcache){}
         return (CtClass)this.softcache.get(var1);
      }
   }

   public synchronized CtClass getLocally(String var1) throws NotFoundException {
      this.softcache.remove(var1);
      CtClass var2 = (CtClass)this.classes.get(var1);
      if (var2 == null) {
         var2 = this.createCtClass(var1, true);
         if (var2 == null) {
            throw new NotFoundException(var1);
         }

         super.cacheCtClass(var1, var2, false);
      }

      return var2;
   }

   public Class toClass(CtClass var1, ClassLoader var2, ProtectionDomain var3) throws CannotCompileException {
      this.lockInCache(var1);
      return super.toClass(var1, this.getClassLoader0(), var3);
   }

   static {
      ClassPool.doPruning = false;
      ClassPool.releaseUnmodifiedClassFile = false;
   }
}
