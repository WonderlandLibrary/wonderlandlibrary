package javassist;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Hashtable;
import java.util.Vector;

public class Loader extends ClassLoader {
   private Hashtable notDefinedHere;
   private Vector notDefinedPackages;
   private ClassPool source;
   private Translator translator;
   private ProtectionDomain domain;
   public boolean doDelegation;

   public Loader() {
      this((ClassPool)null);
   }

   public Loader(ClassPool var1) {
      this.doDelegation = true;
      this.init(var1);
   }

   public Loader(ClassLoader var1, ClassPool var2) {
      super(var1);
      this.doDelegation = true;
      this.init(var2);
   }

   private void init(ClassPool var1) {
      this.notDefinedHere = new Hashtable();
      this.notDefinedPackages = new Vector();
      this.source = var1;
      this.translator = null;
      this.domain = null;
      this.delegateLoadingOf("javassist.Loader");
   }

   public void delegateLoadingOf(String var1) {
      if (var1.endsWith(".")) {
         this.notDefinedPackages.addElement(var1);
      } else {
         this.notDefinedHere.put(var1, this);
      }

   }

   public void setDomain(ProtectionDomain var1) {
      this.domain = var1;
   }

   public void setClassPool(ClassPool var1) {
      this.source = var1;
   }

   public void addTranslator(ClassPool var1, Translator var2) throws NotFoundException, CannotCompileException {
      this.source = var1;
      this.translator = var2;
      var2.start(var1);
   }

   public static void main(String[] var0) throws Throwable {
      Loader var1 = new Loader();
      var1.run(var0);
   }

   public void run(String[] var1) throws Throwable {
      int var2 = var1.length - 1;
      if (var2 >= 0) {
         String[] var3 = new String[var2];

         for(int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = var1[var4 + 1];
         }

         this.run(var1[0], var3);
      }

   }

   public void run(String var1, String[] var2) throws Throwable {
      Class var3 = this.loadClass(var1);

      try {
         var3.getDeclaredMethod("main", String[].class).invoke((Object)null, var2);
      } catch (InvocationTargetException var5) {
         throw var5.getTargetException();
      }
   }

   protected Class loadClass(String var1, boolean var2) throws ClassFormatError, ClassNotFoundException {
      var1 = var1.intern();
      synchronized(var1){}
      Class var4 = this.findLoadedClass(var1);
      if (var4 == null) {
         var4 = this.loadClassByDelegation(var1);
      }

      if (var4 == null) {
         var4 = this.findClass(var1);
      }

      if (var4 == null) {
         var4 = this.delegateToParent(var1);
      }

      if (var2) {
         this.resolveClass(var4);
      }

      return var4;
   }

   protected Class findClass(String var1) throws ClassNotFoundException {
      byte[] var2;
      try {
         if (this.source != null) {
            if (this.translator != null) {
               this.translator.onLoad(this.source, var1);
            }

            try {
               var2 = this.source.get(var1).toBytecode();
            } catch (NotFoundException var7) {
               return null;
            }
         } else {
            String var3 = "/" + var1.replace('.', '/') + ".class";
            InputStream var4 = this.getClass().getResourceAsStream(var3);
            if (var4 == null) {
               return null;
            }

            var2 = ClassPoolTail.readStream(var4);
         }
      } catch (Exception var8) {
         throw new ClassNotFoundException("caught an exception while obtaining a class file for " + var1, var8);
      }

      int var9 = var1.lastIndexOf(46);
      if (var9 != -1) {
         String var10 = var1.substring(0, var9);
         if (this.getPackage(var10) == null) {
            try {
               this.definePackage(var10, (String)null, (String)null, (String)null, (String)null, (String)null, (String)null, (URL)null);
            } catch (IllegalArgumentException var6) {
            }
         }
      }

      return this.domain == null ? this.defineClass(var1, var2, 0, var2.length) : this.defineClass(var1, var2, 0, var2.length, this.domain);
   }

   protected Class loadClassByDelegation(String var1) throws ClassNotFoundException {
      Class var2 = null;
      if (this.doDelegation && (var1.startsWith("java.") || var1.startsWith("javax.") || var1.startsWith("sun.") || var1.startsWith("com.sun.") || var1.startsWith("org.w3c.") || var1.startsWith("org.xml.") || var1 != null)) {
         var2 = this.delegateToParent(var1);
      }

      return var2;
   }

   protected Class delegateToParent(String var1) throws ClassNotFoundException {
      ClassLoader var2 = this.getParent();
      return var2 != null ? var2.loadClass(var1) : this.findSystemClass(var1);
   }
}
