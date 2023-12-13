package org.jboss.errai.reflections.util;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import javax.servlet.ServletContext;
import org.jboss.errai.reflections.Reflections;
import org.jboss.errai.reflections.vfs.Vfs;

public abstract class ClasspathHelper {
   public static ClassLoader[] defaultClassLoaders = new ClassLoader[]{getContextClassLoader(), getStaticClassLoader()};

   public static ClassLoader getContextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   public static ClassLoader getStaticClassLoader() {
      return Reflections.class.getClassLoader();
   }

   public static ClassLoader[] classLoaders(ClassLoader... var0) {
      return var0 != null && var0.length != 0 ? var0 : defaultClassLoaders;
   }

   public static Set forPackage(String var0, ClassLoader... var1) {
      HashSet var2 = Sets.newHashSet();
      ClassLoader[] var3 = classLoaders(var1);
      String var4 = var0.replace(".", "/");
      ClassLoader[] var5 = var3;
      int var6 = var3.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         ClassLoader var8 = var5[var7];

         try {
            Enumeration var9 = var8.getResources(var4);

            while(var9.hasMoreElements()) {
               URL var10 = (URL)var9.nextElement();
               URL var11 = new URL(var10.toExternalForm().substring(0, var10.toExternalForm().lastIndexOf(var4)));
               var2.add(var11);
            }
         } catch (IOException var12) {
            var12.printStackTrace();
         }
      }

      return var2;
   }

   public static URL forClass(Class var0, ClassLoader... var1) {
      ClassLoader[] var2 = classLoaders(var1);
      String var3 = var0.getName().replace(".", "/") + ".class";
      ClassLoader[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ClassLoader var7 = var4[var6];

         try {
            URL var8 = var7.getResource(var3);
            if (var8 != null) {
               String var9 = var8.toExternalForm().substring(0, var8.toExternalForm().lastIndexOf(var0.getPackage().getName().replace(".", "/")));
               return new URL(var9);
            }
         } catch (MalformedURLException var10) {
            var10.printStackTrace();
         }
      }

      return null;
   }

   public static Set forClassLoader(ClassLoader... var0) {
      HashSet var1 = Sets.newHashSet();
      ClassLoader[] var2 = classLoaders(var0);
      ClassLoader[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         for(ClassLoader var6 = var3[var5]; var6 != null; var6 = var6.getParent()) {
            if (var6 instanceof URLClassLoader) {
               URL[] var7 = ((URLClassLoader)var6).getURLs();
               if (var7 != null) {
                  var1.addAll(Sets.newHashSet((Object[])var7));
               }
            }
         }
      }

      return var1;
   }

   public static Set forJavaClassPath() {
      HashSet var0 = Sets.newHashSet();
      String var1 = System.getProperty("java.class.path");
      if (var1 != null) {
         String[] var2 = var1.split(File.pathSeparator);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];

            try {
               var0.add((new File(var5)).toURI().toURL());
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }

      return var0;
   }

   public static Set forWebInfLib(ServletContext var0) {
      HashSet var1 = Sets.newHashSet();
      Iterator var2 = var0.getResourcePaths("/WEB-INF/lib").iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();

         try {
            var1.add(var0.getResource((String)var3));
         } catch (MalformedURLException var5) {
         }
      }

      return var1;
   }

   public static URL forWebInfClasses(ServletContext var0) {
      try {
         String var1 = var0.getRealPath("/WEB-INF/classes");
         File var2 = new File(var1);
         if (var2.exists()) {
            return var2.toURL();
         }
      } catch (MalformedURLException var3) {
      }

      return null;
   }

   public static Set forManifest() {
      return forManifest((Iterable)forClassLoader());
   }

   public static Set forManifest(URL var0) {
      HashSet var1 = Sets.newHashSet();
      var1.add(var0);

      try {
         String var2 = Vfs.normalizePath(var0);
         File var3 = new File(var2);
         JarFile var4 = new JarFile(var2);
         URL var5 = tryToGetValidUrl(var3.getPath(), (new File(var2)).getParent(), var2);
         if (var5 != null) {
            var1.add(var5);
         }

         Manifest var6 = var4.getManifest();
         if (var6 != null) {
            String var7 = var6.getMainAttributes().getValue(new Name("Class-Path"));
            if (var7 != null) {
               String[] var8 = var7.split(" ");
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  String var11 = var8[var10];
                  var5 = tryToGetValidUrl(var3.getPath(), (new File(var2)).getParent(), var11);
                  if (var5 != null) {
                     var1.add(var5);
                  }
               }
            }
         }
      } catch (IOException var12) {
      }

      return var1;
   }

   public static Set forManifest(Iterable var0) {
      HashSet var1 = Sets.newHashSet();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         URL var3 = (URL)var2.next();
         var1.addAll(forManifest(var3));
      }

      return var1;
   }

   private static URL tryToGetValidUrl(String var0, String var1, String var2) {
      try {
         if ((new File(var2)).exists()) {
            return (new File(var2)).toURI().toURL();
         }

         if ((new File(var1 + File.separator + var2)).exists()) {
            return (new File(var1 + File.separator + var2)).toURI().toURL();
         }

         if ((new File(var0 + File.separator + var2)).exists()) {
            return (new File(var0 + File.separator + var2)).toURI().toURL();
         }
      } catch (MalformedURLException var4) {
      }

      return null;
   }
}
