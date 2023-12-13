package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class ClassPath {
   private static final Logger logger = Logger.getLogger(ClassPath.class.getName());
   private static final Predicate IS_TOP_LEVEL = new Predicate() {
      public boolean apply(ClassPath.ClassInfo var1) {
         return ClassPath.ClassInfo.access$000(var1).indexOf(36) == -1;
      }

      public boolean apply(Object var1) {
         return this.apply((ClassPath.ClassInfo)var1);
      }
   };
   private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
   private static final String CLASS_FILE_NAME_EXTENSION = ".class";
   private final ImmutableSet resources;

   private ClassPath(ImmutableSet var1) {
      this.resources = var1;
   }

   public static ClassPath from(ClassLoader var0) throws IOException {
      ClassPath.Scanner var1 = new ClassPath.Scanner();
      Iterator var2 = getClassPathEntries(var0).entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         var1.scan((URI)var3.getKey(), (ClassLoader)var3.getValue());
      }

      return new ClassPath(var1.getResources());
   }

   public ImmutableSet getResources() {
      return this.resources;
   }

   public ImmutableSet getAllClasses() {
      return FluentIterable.from((Iterable)this.resources).filter(ClassPath.ClassInfo.class).toSet();
   }

   public ImmutableSet getTopLevelClasses() {
      return FluentIterable.from((Iterable)this.resources).filter(ClassPath.ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
   }

   public ImmutableSet getTopLevelClasses(String var1) {
      Preconditions.checkNotNull(var1);
      ImmutableSet.Builder var2 = ImmutableSet.builder();
      Iterator var3 = this.getTopLevelClasses().iterator();

      while(var3.hasNext()) {
         ClassPath.ClassInfo var4 = (ClassPath.ClassInfo)var3.next();
         if (var4.getPackageName().equals(var1)) {
            var2.add((Object)var4);
         }
      }

      return var2.build();
   }

   public ImmutableSet getTopLevelClassesRecursive(String var1) {
      Preconditions.checkNotNull(var1);
      String var2 = var1 + '.';
      ImmutableSet.Builder var3 = ImmutableSet.builder();
      Iterator var4 = this.getTopLevelClasses().iterator();

      while(var4.hasNext()) {
         ClassPath.ClassInfo var5 = (ClassPath.ClassInfo)var4.next();
         if (var5.getName().startsWith(var2)) {
            var3.add((Object)var5);
         }
      }

      return var3.build();
   }

   @VisibleForTesting
   static ImmutableMap getClassPathEntries(ClassLoader var0) {
      LinkedHashMap var1 = Maps.newLinkedHashMap();
      ClassLoader var2 = var0.getParent();
      if (var2 != null) {
         var1.putAll(getClassPathEntries(var2));
      }

      if (var0 instanceof URLClassLoader) {
         URLClassLoader var3 = (URLClassLoader)var0;
         URL[] var4 = var3.getURLs();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            URL var7 = var4[var6];

            URI var8;
            try {
               var8 = var7.toURI();
            } catch (URISyntaxException var10) {
               throw new IllegalArgumentException(var10);
            }

            if (!var1.containsKey(var8)) {
               var1.put(var8, var0);
            }
         }
      }

      return ImmutableMap.copyOf(var1);
   }

   @VisibleForTesting
   static String getClassName(String var0) {
      int var1 = var0.length() - 6;
      return var0.substring(0, var1).replace('/', '.');
   }

   static Logger access$100() {
      return logger;
   }

   static Splitter access$200() {
      return CLASS_PATH_ATTRIBUTE_SEPARATOR;
   }

   @VisibleForTesting
   static final class Scanner {
      private final ImmutableSortedSet.Builder resources = new ImmutableSortedSet.Builder(Ordering.usingToString());
      private final Set scannedUris = Sets.newHashSet();

      ImmutableSortedSet getResources() {
         return this.resources.build();
      }

      void scan(URI var1, ClassLoader var2) throws IOException {
         if (var1.getScheme().equals("file") && this.scannedUris.add(var1)) {
            this.scanFrom(new File(var1), var2);
         }

      }

      @VisibleForTesting
      void scanFrom(File var1, ClassLoader var2) throws IOException {
         if (var1.exists()) {
            if (var1.isDirectory()) {
               this.scanDirectory(var1, var2);
            } else {
               this.scanJar(var1, var2);
            }

         }
      }

      private void scanDirectory(File var1, ClassLoader var2) throws IOException {
         this.scanDirectory(var1, var2, "", ImmutableSet.of());
      }

      private void scanDirectory(File var1, ClassLoader var2, String var3, ImmutableSet var4) throws IOException {
         File var5 = var1.getCanonicalFile();
         if (!var4.contains(var5)) {
            File[] var6 = var1.listFiles();
            if (var6 == null) {
               ClassPath.access$100().warning("Cannot read directory " + var1);
            } else {
               ImmutableSet var7 = ImmutableSet.builder().addAll((Iterable)var4).add((Object)var5).build();
               File[] var8 = var6;
               int var9 = var6.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  File var11 = var8[var10];
                  String var12 = var11.getName();
                  if (var11.isDirectory()) {
                     this.scanDirectory(var11, var2, var3 + var12 + "/", var7);
                  } else {
                     String var13 = var3 + var12;
                     if (!var13.equals("META-INF/MANIFEST.MF")) {
                        this.resources.add((Object)ClassPath.ResourceInfo.of(var13, var2));
                     }
                  }
               }

            }
         }
      }

      private void scanJar(File var1, ClassLoader var2) throws IOException {
         JarFile var3;
         try {
            var3 = new JarFile(var1);
         } catch (IOException var9) {
            return;
         }

         Iterator var4 = getClassPathFromManifest(var1, var3.getManifest()).iterator();

         while(var4.hasNext()) {
            URI var5 = (URI)var4.next();
            this.scan(var5, var2);
         }

         Enumeration var10 = var3.entries();

         while(var10.hasMoreElements()) {
            JarEntry var11 = (JarEntry)var10.nextElement();
            if (!var11.isDirectory() && !var11.getName().equals("META-INF/MANIFEST.MF")) {
               this.resources.add((Object)ClassPath.ResourceInfo.of(var11.getName(), var2));
            }
         }

         try {
            var3.close();
         } catch (IOException var8) {
         }

      }

      @VisibleForTesting
      static ImmutableSet getClassPathFromManifest(File var0, @Nullable Manifest var1) {
         if (var1 == null) {
            return ImmutableSet.of();
         } else {
            ImmutableSet.Builder var2 = ImmutableSet.builder();
            String var3 = var1.getMainAttributes().getValue(Name.CLASS_PATH.toString());
            if (var3 != null) {
               Iterator var4 = ClassPath.access$200().split(var3).iterator();

               while(var4.hasNext()) {
                  String var5 = (String)var4.next();

                  URI var6;
                  try {
                     var6 = getClassPathEntry(var0, var5);
                  } catch (URISyntaxException var8) {
                     ClassPath.access$100().warning("Invalid Class-Path entry: " + var5);
                     continue;
                  }

                  var2.add((Object)var6);
               }
            }

            return var2.build();
         }
      }

      @VisibleForTesting
      static URI getClassPathEntry(File var0, String var1) throws URISyntaxException {
         URI var2 = new URI(var1);
         return var2.isAbsolute() ? var2 : (new File(var0.getParentFile(), var1.replace('/', File.separatorChar))).toURI();
      }
   }

   @Beta
   public static final class ClassInfo extends ClassPath.ResourceInfo {
      private final String className;

      ClassInfo(String var1, ClassLoader var2) {
         super(var1, var2);
         this.className = ClassPath.getClassName(var1);
      }

      public String getPackageName() {
         return Reflection.getPackageName(this.className);
      }

      public String getSimpleName() {
         int var1 = this.className.lastIndexOf(36);
         String var2;
         if (var1 != -1) {
            var2 = this.className.substring(var1 + 1);
            return CharMatcher.DIGIT.trimLeadingFrom(var2);
         } else {
            var2 = this.getPackageName();
            return var2.isEmpty() ? this.className : this.className.substring(var2.length() + 1);
         }
      }

      public String getName() {
         return this.className;
      }

      public Class load() {
         try {
            return this.loader.loadClass(this.className);
         } catch (ClassNotFoundException var2) {
            throw new IllegalStateException(var2);
         }
      }

      public String toString() {
         return this.className;
      }

      static String access$000(ClassPath.ClassInfo var0) {
         return var0.className;
      }
   }

   @Beta
   public static class ResourceInfo {
      private final String resourceName;
      final ClassLoader loader;

      static ClassPath.ResourceInfo of(String var0, ClassLoader var1) {
         return (ClassPath.ResourceInfo)(var0.endsWith(".class") ? new ClassPath.ClassInfo(var0, var1) : new ClassPath.ResourceInfo(var0, var1));
      }

      ResourceInfo(String var1, ClassLoader var2) {
         this.resourceName = (String)Preconditions.checkNotNull(var1);
         this.loader = (ClassLoader)Preconditions.checkNotNull(var2);
      }

      public final URL url() {
         return (URL)Preconditions.checkNotNull(this.loader.getResource(this.resourceName), "Failed to load resource: %s", this.resourceName);
      }

      public final String getResourceName() {
         return this.resourceName;
      }

      public int hashCode() {
         return this.resourceName.hashCode();
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof ClassPath.ResourceInfo)) {
            return false;
         } else {
            ClassPath.ResourceInfo var2 = (ClassPath.ResourceInfo)var1;
            return this.resourceName.equals(var2.resourceName) && this.loader == var2.loader;
         }
      }

      public String toString() {
         return this.resourceName;
      }
   }
}
