package org.reflections.util;

import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import javax.servlet.ServletContext;
import org.reflections.Reflections;

public abstract class ClasspathHelper {
   public static ClassLoader contextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }

   public static ClassLoader staticClassLoader() {
      return Reflections.class.getClassLoader();
   }

   public static ClassLoader[] classLoaders(ClassLoader... var0) {
      if (var0 != null && var0.length != 0) {
         return var0;
      } else {
         ClassLoader var1 = contextClassLoader();
         ClassLoader var2 = staticClassLoader();
         return var1 != null ? (var2 != null && var1 != var2 ? new ClassLoader[]{var1, var2} : new ClassLoader[]{var1}) : new ClassLoader[0];
      }
   }

   public static Collection forPackage(String var0, ClassLoader... var1) {
      return forResource(resourceName(var0), var1);
   }

   public static Collection forResource(String var0, ClassLoader... var1) {
      ArrayList var2 = new ArrayList();
      ClassLoader[] var3 = classLoaders(var1);
      ClassLoader[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ClassLoader var7 = var4[var6];

         try {
            Enumeration var8 = var7.getResources(var0);

            while(var8.hasMoreElements()) {
               URL var9 = (URL)var8.nextElement();
               int var10 = var9.toExternalForm().lastIndexOf(var0);
               if (var10 != -1) {
                  var2.add(new URL(var9.toExternalForm().substring(0, var10)));
               } else {
                  var2.add(var9);
               }
            }
         } catch (IOException var11) {
            if (Reflections.log != null) {
               Reflections.log.error("error getting resources for " + var0, var11);
            }
         }
      }

      return distinctUrls(var2);
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
            if (Reflections.log != null) {
               Reflections.log.warn("Could not get URL", var10);
            }
         }
      }

      return null;
   }

   public static Collection forClassLoader() {
      return forClassLoader(classLoaders());
   }

   public static Collection forClassLoader(ClassLoader... var0) {
      ArrayList var1 = new ArrayList();
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

      return distinctUrls(var1);
   }

   public static Collection forJavaClassPath() {
      ArrayList var0 = new ArrayList();
      String var1 = System.getProperty("java.class.path");
      if (var1 != null) {
         String[] var2 = var1.split(File.pathSeparator);
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String var5 = var2[var4];

            try {
               var0.add((new File(var5)).toURI().toURL());
            } catch (Exception var7) {
               if (Reflections.log != null) {
                  Reflections.log.warn("Could not get URL", var7);
               }
            }
         }
      }

      return distinctUrls(var0);
   }

   public static Collection forWebInfLib(ServletContext var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.getResourcePaths("/WEB-INF/lib").iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();

         try {
            var1.add(var0.getResource((String)var3));
         } catch (MalformedURLException var5) {
         }
      }

      return distinctUrls(var1);
   }

   public static URL forWebInfClasses(ServletContext var0) {
      try {
         String var1 = var0.getRealPath("/WEB-INF/classes");
         if (var1 == null) {
            return var0.getResource("/WEB-INF/classes");
         }

         File var2 = new File(var1);
         if (var2.exists()) {
            return var2.toURL();
         }
      } catch (MalformedURLException var3) {
      }

      return null;
   }

   public static Collection forManifest() {
      return forManifest((Iterable)forClassLoader());
   }

   public static Collection forManifest(URL var0) {
      ArrayList var1 = new ArrayList();
      var1.add(var0);

      try {
         String var2 = cleanPath(var0);
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

      return distinctUrls(var1);
   }

   public static Collection forManifest(Iterable var0) {
      ArrayList var1 = new ArrayList();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         URL var3 = (URL)var2.next();
         var1.addAll(forManifest(var3));
      }

      return distinctUrls(var1);
   }

   static URL tryToGetValidUrl(String var0, String var1, String var2) {
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

         if ((new File((new URL(var2)).getFile())).exists()) {
            return (new File((new URL(var2)).getFile())).toURI().toURL();
         }
      } catch (MalformedURLException var4) {
      }

      return null;
   }

   public static String cleanPath(URL var0) {
      String var1 = var0.getPath();

      try {
         var1 = URLDecoder.decode(var1, "UTF-8");
      } catch (UnsupportedEncodingException var3) {
      }

      if (var1.startsWith("jar:")) {
         var1 = var1.substring(4);
      }

      if (var1.startsWith("file:")) {
         var1 = var1.substring(5);
      }

      if (var1.endsWith("!/")) {
         var1 = var1.substring(0, var1.lastIndexOf("!/")) + "/";
      }

      return var1;
   }

   private static String resourceName(String var0) {
      if (var0 != null) {
         String var1 = var0.replace(".", "/");
         var1 = var1.replace("\\", "/");
         if (var1.startsWith("/")) {
            var1 = var1.substring(1);
         }

         return var1;
      } else {
         return null;
      }
   }

   private static Collection distinctUrls(Collection var0) {
      HashMap var1 = new HashMap(var0.size());
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         URL var3 = (URL)var2.next();
         var1.put(var3.toExternalForm(), var3);
      }

      return var1.values();
   }
}
