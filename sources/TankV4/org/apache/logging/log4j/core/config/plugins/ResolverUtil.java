package org.apache.logging.log4j.core.config.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

public class ResolverUtil {
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static final String VFSZIP = "vfszip";
   private static final String BUNDLE_RESOURCE = "bundleresource";
   private final Set classMatches = new HashSet();
   private final Set resourceMatches = new HashSet();
   private ClassLoader classloader;

   public Set getClasses() {
      return this.classMatches;
   }

   public Set getResources() {
      return this.resourceMatches;
   }

   public ClassLoader getClassLoader() {
      return this.classloader != null ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, (Class)null));
   }

   public void setClassLoader(ClassLoader var1) {
      this.classloader = var1;
   }

   public void findImplementations(Class var1, String... var2) {
      if (var2 != null) {
         ResolverUtil.IsA var3 = new ResolverUtil.IsA(var1);
         String[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            this.findInPackage(var3, var7);
         }

      }
   }

   public void findSuffix(String var1, String... var2) {
      if (var2 != null) {
         ResolverUtil.NameEndsWith var3 = new ResolverUtil.NameEndsWith(var1);
         String[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            this.findInPackage(var3, var7);
         }

      }
   }

   public void findAnnotated(Class var1, String... var2) {
      if (var2 != null) {
         ResolverUtil.AnnotatedWith var3 = new ResolverUtil.AnnotatedWith(var1);
         String[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            this.findInPackage(var3, var7);
         }

      }
   }

   public void findNamedResource(String var1, String... var2) {
      if (var2 != null) {
         ResolverUtil.NameIs var3 = new ResolverUtil.NameIs(var1);
         String[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String var7 = var4[var6];
            this.findInPackage(var3, var7);
         }

      }
   }

   public void find(ResolverUtil.Test var1, String... var2) {
      if (var2 != null) {
         String[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String var6 = var3[var5];
            this.findInPackage(var1, var6);
         }

      }
   }

   public void findInPackage(ResolverUtil.Test var1, String var2) {
      var2 = var2.replace('.', '/');
      ClassLoader var3 = this.getClassLoader();

      Enumeration var4;
      try {
         var4 = var3.getResources(var2);
      } catch (IOException var12) {
         LOGGER.warn((String)("Could not read package: " + var2), (Throwable)var12);
         return;
      }

      while(var4.hasMoreElements()) {
         try {
            URL var5 = (URL)var4.nextElement();
            String var6 = var5.getFile();
            var6 = URLDecoder.decode(var6, Charsets.UTF_8.name());
            if (var6.startsWith("file:")) {
               var6 = var6.substring(5);
            }

            if (var6.indexOf(33) > 0) {
               var6 = var6.substring(0, var6.indexOf(33));
            }

            LOGGER.info("Scanning for classes in [" + var6 + "] matching criteria: " + var1);
            if ("vfszip".equals(var5.getProtocol())) {
               String var7 = var6.substring(0, var6.length() - var2.length() - 2);
               URL var8 = new URL(var5.getProtocol(), var5.getHost(), var7);
               JarInputStream var9 = new JarInputStream(var8.openStream());
               this.loadImplementationsInJar(var1, var2, var7, var9);
               this.close(var9, var8);
            } else if ("bundleresource".equals(var5.getProtocol())) {
               this.loadImplementationsInBundle(var1, var2);
            } else {
               File var13 = new File(var6);
               if (var13.isDirectory()) {
                  this.loadImplementationsInDirectory(var1, var2, var13);
               } else {
                  this.loadImplementationsInJar(var1, var2, var13);
               }
            }
         } catch (IOException var11) {
            LOGGER.warn((String)"could not read entries", (Throwable)var11);
         }
      }

   }

   private void loadImplementationsInBundle(ResolverUtil.Test var1, String var2) {
      BundleWiring var3 = (BundleWiring)FrameworkUtil.getBundle(ResolverUtil.class).adapt(BundleWiring.class);
      Collection var4 = var3.listResources(var2, "*.class", 1);
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         this.addIfMatching(var1, var6);
      }

   }

   private void loadImplementationsInDirectory(ResolverUtil.Test var1, String var2, File var3) {
      File[] var4 = var3.listFiles();
      if (var4 != null) {
         File[] var6 = var4;
         int var7 = var4.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            File var9 = var6[var8];
            StringBuilder var5 = new StringBuilder();
            var5.append(var2).append("/").append(var9.getName());
            String var10 = var2 == null ? var9.getName() : var5.toString();
            if (var9.isDirectory()) {
               this.loadImplementationsInDirectory(var1, var10, var9);
            } else if (var9.getName() == false) {
               this.addIfMatching(var1, var10);
            }
         }

      }
   }

   private void loadImplementationsInJar(ResolverUtil.Test var1, String var2, File var3) {
      JarInputStream var4 = null;

      try {
         var4 = new JarInputStream(new FileInputStream(var3));
         this.loadImplementationsInJar(var1, var2, var3.getPath(), var4);
      } catch (FileNotFoundException var7) {
         LOGGER.error("Could not search jar file '" + var3 + "' for classes matching criteria: " + var1 + " file not found");
         this.close(var4, var3);
         return;
      } catch (IOException var8) {
         LOGGER.error((String)("Could not search jar file '" + var3 + "' for classes matching criteria: " + var1 + " due to an IOException"), (Throwable)var8);
         this.close(var4, var3);
         return;
      }

      this.close(var4, var3);
   }

   private void close(JarInputStream var1, Object var2) {
      if (var1 != null) {
         try {
            var1.close();
         } catch (IOException var4) {
            LOGGER.error("Error closing JAR file stream for {}", var2, var4);
         }
      }

   }

   private void loadImplementationsInJar(ResolverUtil.Test var1, String var2, String var3, JarInputStream var4) {
      while(true) {
         try {
            JarEntry var5;
            if ((var5 = var4.getNextJarEntry()) != null) {
               String var6 = var5.getName();
               if (!var5.isDirectory() && var6.startsWith(var2) && var6 == false) {
                  this.addIfMatching(var1, var6);
               }
               continue;
            }
         } catch (IOException var7) {
            LOGGER.error((String)("Could not search jar file '" + var3 + "' for classes matching criteria: " + var1 + " due to an IOException"), (Throwable)var7);
         }

         return;
      }
   }

   protected void addIfMatching(ResolverUtil.Test var1, String var2) {
      try {
         ClassLoader var3 = this.getClassLoader();
         if (var1.doesMatchClass()) {
            String var4 = var2.substring(0, var2.indexOf(46)).replace('/', '.');
            if (LOGGER.isDebugEnabled()) {
               LOGGER.debug("Checking to see if class " + var4 + " matches criteria [" + var1 + "]");
            }

            Class var5 = var3.loadClass(var4);
            if (var1.matches(var5)) {
               this.classMatches.add(var5);
            }
         }

         if (var1.doesMatchResource()) {
            URL var7 = var3.getResource(var2);
            if (var7 == null) {
               var7 = var3.getResource(var2.substring(1));
            }

            if (var7 != null && var1.matches(var7.toURI())) {
               this.resourceMatches.add(var7.toURI());
            }
         }
      } catch (Throwable var6) {
         LOGGER.warn("Could not examine class '" + var2 + "' due to a " + var6.getClass().getName() + " with message: " + var6.getMessage());
      }

   }

   public static class NameIs extends ResolverUtil.ResourceTest {
      private final String name;

      public NameIs(String var1) {
         this.name = "/" + var1;
      }

      public boolean matches(URI var1) {
         return var1.getPath().endsWith(this.name);
      }

      public String toString() {
         return "named " + this.name;
      }
   }

   public static class AnnotatedWith extends ResolverUtil.ClassTest {
      private final Class annotation;

      public AnnotatedWith(Class var1) {
         this.annotation = var1;
      }

      public boolean matches(Class var1) {
         return var1 != null && var1.isAnnotationPresent(this.annotation);
      }

      public String toString() {
         return "annotated with @" + this.annotation.getSimpleName();
      }
   }

   public static class NameEndsWith extends ResolverUtil.ClassTest {
      private final String suffix;

      public NameEndsWith(String var1) {
         this.suffix = var1;
      }

      public boolean matches(Class var1) {
         return var1 != null && var1.getName().endsWith(this.suffix);
      }

      public String toString() {
         return "ends with the suffix " + this.suffix;
      }
   }

   public static class IsA extends ResolverUtil.ClassTest {
      private final Class parent;

      public IsA(Class var1) {
         this.parent = var1;
      }

      public boolean matches(Class var1) {
         return var1 != null && this.parent.isAssignableFrom(var1);
      }

      public String toString() {
         return "is assignable to " + this.parent.getSimpleName();
      }
   }

   public abstract static class ResourceTest implements ResolverUtil.Test {
      public boolean matches(Class var1) {
         throw new UnsupportedOperationException();
      }

      public boolean doesMatchClass() {
         return false;
      }

      public boolean doesMatchResource() {
         return true;
      }
   }

   public abstract static class ClassTest implements ResolverUtil.Test {
      public boolean matches(URI var1) {
         throw new UnsupportedOperationException();
      }

      public boolean doesMatchClass() {
         return true;
      }

      public boolean doesMatchResource() {
         return false;
      }
   }

   public interface Test {
      boolean matches(Class var1);

      boolean matches(URI var1);

      boolean doesMatchClass();

      boolean doesMatchResource();
   }
}
