package javassist;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;

public class ClassPool {
   private static Method defineClass1;
   private static Method defineClass2;
   private static Method definePackage;
   public boolean childFirstLookup;
   public static boolean doPruning;
   private int compressCount;
   private static final int COMPRESS_THRESHOLD = 100;
   public static boolean releaseUnmodifiedClassFile;
   protected ClassPoolTail source;
   protected ClassPool parent;
   protected Hashtable classes;
   private Hashtable cflow;
   private static final int INIT_HASH_SIZE = 191;
   private ArrayList importedPackages;
   private static ClassPool defaultPool;

   public ClassPool() {
      this((ClassPool)null);
   }

   public ClassPool(boolean var1) {
      this((ClassPool)null);
      if (var1) {
         this.appendSystemPath();
      }

   }

   public ClassPool(ClassPool var1) {
      this.childFirstLookup = false;
      this.cflow = null;
      this.classes = new Hashtable(191);
      this.source = new ClassPoolTail();
      this.parent = var1;
      if (var1 == null) {
         CtClass[] var2 = CtClass.primitiveTypes;

         for(int var3 = 0; var3 < var2.length; ++var3) {
            this.classes.put(var2[var3].getName(), var2[var3]);
         }
      }

      this.cflow = null;
      this.compressCount = 0;
      this.clearImportedPackages();
   }

   public static synchronized ClassPool getDefault() {
      if (defaultPool == null) {
         defaultPool = new ClassPool((ClassPool)null);
         defaultPool.appendSystemPath();
      }

      return defaultPool;
   }

   protected CtClass getCached(String var1) {
      return (CtClass)this.classes.get(var1);
   }

   protected void cacheCtClass(String var1, CtClass var2, boolean var3) {
      this.classes.put(var1, var2);
   }

   protected CtClass removeCached(String var1) {
      return (CtClass)this.classes.remove(var1);
   }

   public String toString() {
      return this.source.toString();
   }

   void compress() {
      if (this.compressCount++ > 100) {
         this.compressCount = 0;
         Enumeration var1 = this.classes.elements();

         while(var1.hasMoreElements()) {
            ((CtClass)var1.nextElement()).compress();
         }
      }

   }

   public void importPackage(String var1) {
      this.importedPackages.add(var1);
   }

   public void clearImportedPackages() {
      this.importedPackages = new ArrayList();
      this.importedPackages.add("java.lang");
   }

   public Iterator getImportedPackages() {
      return this.importedPackages.iterator();
   }

   /** @deprecated */
   public void recordInvalidClassName(String var1) {
   }

   void recordCflow(String var1, String var2, String var3) {
      if (this.cflow == null) {
         this.cflow = new Hashtable();
      }

      this.cflow.put(var1, new Object[]{var2, var3});
   }

   public Object[] lookupCflow(String var1) {
      if (this.cflow == null) {
         this.cflow = new Hashtable();
      }

      return (Object[])((Object[])this.cflow.get(var1));
   }

   public CtClass getAndRename(String var1, String var2) throws NotFoundException {
      CtClass var3 = this.get0(var1, false);
      if (var3 == null) {
         throw new NotFoundException(var1);
      } else {
         if (var3 instanceof CtClassType) {
            ((CtClassType)var3).setClassPool(this);
         }

         var3.setName(var2);
         return var3;
      }
   }

   synchronized void classNameChanged(String var1, CtClass var2) {
      CtClass var3 = this.getCached(var1);
      if (var3 == var2) {
         this.removeCached(var1);
      }

      String var4 = var2.getName();
      this.checkNotFrozen(var4);
      this.cacheCtClass(var4, var2, false);
   }

   public CtClass get(String var1) throws NotFoundException {
      CtClass var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = this.get0(var1, true);
      }

      if (var2 == null) {
         throw new NotFoundException(var1);
      } else {
         var2.incGetCounter();
         return var2;
      }
   }

   public CtClass getOrNull(String var1) {
      CtClass var2 = null;
      if (var1 == null) {
         var2 = null;
      } else {
         try {
            var2 = this.get0(var1, true);
         } catch (NotFoundException var4) {
         }
      }

      if (var2 != null) {
         var2.incGetCounter();
      }

      return var2;
   }

   public CtClass getCtClass(String var1) throws NotFoundException {
      return var1.charAt(0) == '[' ? Descriptor.toCtClass(var1, this) : this.get(var1);
   }

   protected synchronized CtClass get0(String var1, boolean var2) throws NotFoundException {
      CtClass var3 = null;
      if (var2) {
         var3 = this.getCached(var1);
         if (var3 != null) {
            return var3;
         }
      }

      if (!this.childFirstLookup && this.parent != null) {
         var3 = this.parent.get0(var1, var2);
         if (var3 != null) {
            return var3;
         }
      }

      var3 = this.createCtClass(var1, var2);
      if (var3 != null) {
         if (var2) {
            this.cacheCtClass(var3.getName(), var3, false);
         }

         return var3;
      } else {
         if (this.childFirstLookup && this.parent != null) {
            var3 = this.parent.get0(var1, var2);
         }

         return var3;
      }
   }

   protected CtClass createCtClass(String var1, boolean var2) {
      if (var1.charAt(0) == '[') {
         var1 = Descriptor.toClassName(var1);
      }

      if (!var1.endsWith("[]")) {
         return this.find(var1) == null ? null : new CtClassType(var1, this);
      } else {
         String var3 = var1.substring(0, var1.indexOf(91));
         return (!var2 || this.getCached(var3) == null) && this.find(var3) == null ? null : new CtArray(var1, this);
      }
   }

   public URL find(String var1) {
      return this.source.find(var1);
   }

   void checkNotFrozen(String var1) throws RuntimeException {
      CtClass var2 = this.getCached(var1);
      if (var2 == null) {
         if (!this.childFirstLookup && this.parent != null) {
            try {
               var2 = this.parent.get0(var1, true);
            } catch (NotFoundException var4) {
            }

            if (var2 != null) {
               throw new RuntimeException(var1 + " is in a parent ClassPool.  Use the parent.");
            }
         }
      } else if (var2.isFrozen()) {
         throw new RuntimeException(var1 + ": frozen class (cannot edit)");
      }

   }

   CtClass checkNotExists(String var1) {
      CtClass var2 = this.getCached(var1);
      if (var2 == null && !this.childFirstLookup && this.parent != null) {
         try {
            var2 = this.parent.get0(var1, true);
         } catch (NotFoundException var4) {
         }
      }

      return var2;
   }

   InputStream openClassfile(String var1) throws NotFoundException {
      return this.source.openClassfile(var1);
   }

   void writeClassfile(String var1, OutputStream var2) throws NotFoundException, IOException, CannotCompileException {
      this.source.writeClassfile(var1, var2);
   }

   public CtClass[] get(String[] var1) throws NotFoundException {
      if (var1 == null) {
         return new CtClass[0];
      } else {
         int var2 = var1.length;
         CtClass[] var3 = new CtClass[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = this.get(var1[var4]);
         }

         return var3;
      }
   }

   public CtMethod getMethod(String var1, String var2) throws NotFoundException {
      CtClass var3 = this.get(var1);
      return var3.getDeclaredMethod(var2);
   }

   public CtClass makeClass(InputStream var1) throws IOException, RuntimeException {
      return this.makeClass(var1, true);
   }

   public CtClass makeClass(InputStream var1, boolean var2) throws IOException, RuntimeException {
      this.compress();
      BufferedInputStream var5 = new BufferedInputStream(var1);
      CtClassType var3 = new CtClassType(var5, this);
      var3.checkModify();
      String var4 = var3.getName();
      if (var2) {
         this.checkNotFrozen(var4);
      }

      this.cacheCtClass(var4, var3, true);
      return var3;
   }

   public CtClass makeClass(ClassFile var1) throws RuntimeException {
      return this.makeClass(var1, true);
   }

   public CtClass makeClass(ClassFile var1, boolean var2) throws RuntimeException {
      this.compress();
      CtClassType var3 = new CtClassType(var1, this);
      var3.checkModify();
      String var4 = var3.getName();
      if (var2) {
         this.checkNotFrozen(var4);
      }

      this.cacheCtClass(var4, var3, true);
      return var3;
   }

   public CtClass makeClassIfNew(InputStream var1) throws IOException, RuntimeException {
      this.compress();
      BufferedInputStream var5 = new BufferedInputStream(var1);
      CtClassType var2 = new CtClassType(var5, this);
      var2.checkModify();
      String var3 = var2.getName();
      CtClass var4 = this.checkNotExists(var3);
      if (var4 != null) {
         return var4;
      } else {
         this.cacheCtClass(var3, var2, true);
         return var2;
      }
   }

   public CtClass makeClass(String var1) throws RuntimeException {
      return this.makeClass(var1, (CtClass)null);
   }

   public synchronized CtClass makeClass(String var1, CtClass var2) throws RuntimeException {
      this.checkNotFrozen(var1);
      CtNewClass var3 = new CtNewClass(var1, this, false, var2);
      this.cacheCtClass(var1, var3, true);
      return var3;
   }

   synchronized CtClass makeNestedClass(String var1) {
      this.checkNotFrozen(var1);
      CtNewNestedClass var2 = new CtNewNestedClass(var1, this, false, (CtClass)null);
      this.cacheCtClass(var1, var2, true);
      return var2;
   }

   public CtClass makeInterface(String var1) throws RuntimeException {
      return this.makeInterface(var1, (CtClass)null);
   }

   public synchronized CtClass makeInterface(String var1, CtClass var2) throws RuntimeException {
      this.checkNotFrozen(var1);
      CtNewClass var3 = new CtNewClass(var1, this, true, var2);
      this.cacheCtClass(var1, var3, true);
      return var3;
   }

   public CtClass makeAnnotation(String var1) throws RuntimeException {
      try {
         CtClass var2 = this.makeInterface(var1, this.get("java.lang.annotation.Annotation"));
         var2.setModifiers(var2.getModifiers() | 8192);
         return var2;
      } catch (NotFoundException var3) {
         throw new RuntimeException(var3.getMessage(), var3);
      }
   }

   public ClassPath appendSystemPath() {
      return this.source.appendSystemPath();
   }

   public ClassPath insertClassPath(ClassPath var1) {
      return this.source.insertClassPath(var1);
   }

   public ClassPath appendClassPath(ClassPath var1) {
      return this.source.appendClassPath(var1);
   }

   public ClassPath insertClassPath(String var1) throws NotFoundException {
      return this.source.insertClassPath(var1);
   }

   public ClassPath appendClassPath(String var1) throws NotFoundException {
      return this.source.appendClassPath(var1);
   }

   public void removeClassPath(ClassPath var1) {
      this.source.removeClassPath(var1);
   }

   public void appendPathList(String var1) throws NotFoundException {
      char var2 = File.pathSeparatorChar;
      int var3 = 0;

      while(true) {
         int var4 = var1.indexOf(var2, var3);
         if (var4 < 0) {
            this.appendClassPath(var1.substring(var3));
            return;
         }

         this.appendClassPath(var1.substring(var3, var4));
         var3 = var4 + 1;
      }
   }

   public Class toClass(CtClass var1) throws CannotCompileException {
      return this.toClass(var1, this.getClassLoader());
   }

   public ClassLoader getClassLoader() {
      return getContextClassLoader();
   }

   static ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   /** @deprecated */
   public Class toClass(CtClass var1, ClassLoader var2) throws CannotCompileException {
      return this.toClass(var1, var2, (ProtectionDomain)null);
   }

   public Class toClass(CtClass var1, ClassLoader var2, ProtectionDomain var3) throws CannotCompileException {
      try {
         byte[] var4 = var1.toBytecode();
         Method var5;
         Object[] var6;
         if (var3 == null) {
            var5 = defineClass1;
            var6 = new Object[]{var1.getName(), var4, new Integer(0), new Integer(var4.length)};
         } else {
            var5 = defineClass2;
            var6 = new Object[]{var1.getName(), var4, new Integer(0), new Integer(var4.length), var3};
         }

         return (Class)toClass2(var5, var2, var6);
      } catch (RuntimeException var7) {
         throw var7;
      } catch (InvocationTargetException var8) {
         throw new CannotCompileException(var8.getTargetException());
      } catch (Exception var9) {
         throw new CannotCompileException(var9);
      }
   }

   private static synchronized Object toClass2(Method var0, ClassLoader var1, Object[] var2) throws Exception {
      var0.setAccessible(true);
      Object var3 = var0.invoke(var1, var2);
      var0.setAccessible(false);
      return var3;
   }

   public void makePackage(ClassLoader var1, String var2) throws CannotCompileException {
      Object[] var3 = new Object[]{var2, null, null, null, null, null, null, null};

      Object var4;
      try {
         toClass2(definePackage, var1, var3);
         return;
      } catch (InvocationTargetException var6) {
         var4 = var6.getTargetException();
         if (var4 == null) {
            var4 = var6;
         } else if (var4 instanceof IllegalArgumentException) {
            return;
         }
      } catch (Exception var7) {
         var4 = var7;
      }

      throw new CannotCompileException((Throwable)var4);
   }

   static Method access$002(Method var0) {
      defineClass1 = var0;
      return var0;
   }

   static Method access$102(Method var0) {
      defineClass2 = var0;
      return var0;
   }

   static Method access$202(Method var0) {
      definePackage = var0;
      return var0;
   }

   static {
      try {
         AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Class var1 = Class.forName("java.lang.ClassLoader");
               ClassPool.access$002(var1.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE));
               ClassPool.access$102(var1.getDeclaredMethod("defineClass", String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class));
               ClassPool.access$202(var1.getDeclaredMethod("definePackage", String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class));
               return null;
            }
         });
      } catch (PrivilegedActionException var1) {
         throw new RuntimeException("cannot initialize ClassPool", var1.getException());
      }

      doPruning = false;
      releaseUnmodifiedClassFile = true;
      defaultPool = null;
   }
}
