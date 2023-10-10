package org.reflections.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.adapters.JavaReflectionAdapter;
import org.reflections.adapters.JavassistAdapter;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.Serializer;
import org.reflections.serializers.XmlSerializer;

public class ConfigurationBuilder implements Configuration {
   @Nonnull
   private Set scanners = Sets.newHashSet((Object[])(new TypeAnnotationsScanner(), new SubTypesScanner()));
   @Nonnull
   private Set urls = Sets.newHashSet();
   protected MetadataAdapter metadataAdapter;
   @Nullable
   private Predicate inputsFilter;
   private Serializer serializer;
   @Nullable
   private ExecutorService executorService;
   @Nullable
   private ClassLoader[] classLoaders;

   public static ConfigurationBuilder build(@Nullable Object... var0) {
      ConfigurationBuilder var1 = new ConfigurationBuilder();
      ArrayList var2 = Lists.newArrayList();
      Iterator var7;
      Object var8;
      if (var0 != null) {
         Object[] var3 = var0;
         int var4 = var0.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            if (var6 != null) {
               if (var6.getClass().isArray()) {
                  Object[] var18 = (Object[])((Object[])var6);
                  int var19 = var18.length;

                  for(int var9 = 0; var9 < var19; ++var9) {
                     Object var10 = var18[var9];
                     if (var10 != null) {
                        var2.add(var10);
                     }
                  }
               } else if (var6 instanceof Iterable) {
                  var7 = ((Iterable)var6).iterator();

                  while(var7.hasNext()) {
                     var8 = var7.next();
                     if (var8 != null) {
                        var2.add(var8);
                     }
                  }
               } else {
                  var2.add(var6);
               }
            }
         }
      }

      ArrayList var12 = Lists.newArrayList();
      Iterator var13 = var2.iterator();

      while(var13.hasNext()) {
         Object var15 = var13.next();
         if (var15 instanceof ClassLoader) {
            var12.add((ClassLoader)var15);
         }
      }

      ClassLoader[] var14 = var12.isEmpty() ? null : (ClassLoader[])var12.toArray(new ClassLoader[var12.size()]);
      FilterBuilder var16 = new FilterBuilder();
      ArrayList var17 = Lists.newArrayList();
      var7 = var2.iterator();

      while(var7.hasNext()) {
         var8 = var7.next();
         if (var8 instanceof String) {
            var1.addUrls(ClasspathHelper.forPackage((String)var8, var14));
            var16.includePackage((String)var8);
         } else if (var8 instanceof Class) {
            if (Scanner.class.isAssignableFrom((Class)var8)) {
               try {
                  var1.addScanners((Scanner)((Class)var8).newInstance());
               } catch (Exception var11) {
               }
            }

            var1.addUrls(ClasspathHelper.forClass((Class)var8, var14));
            var16.includePackage((Class)var8);
         } else if (var8 instanceof Scanner) {
            var17.add((Scanner)var8);
         } else if (var8 instanceof URL) {
            var1.addUrls((URL)var8);
         } else if (!(var8 instanceof ClassLoader)) {
            if (var8 instanceof Predicate) {
               var16.add((Predicate)var8);
            } else if (var8 instanceof ExecutorService) {
               var1.setExecutorService((ExecutorService)var8);
            } else if (Reflections.log != null) {
               throw new ReflectionsException("could not use param " + var8);
            }
         }
      }

      if (var1.getUrls().isEmpty()) {
         if (var14 != null) {
            var1.addUrls(ClasspathHelper.forClassLoader(var14));
         } else {
            var1.addUrls(ClasspathHelper.forClassLoader());
         }
      }

      var1.filterInputsBy(var16);
      if (!var17.isEmpty()) {
         var1.setScanners((Scanner[])var17.toArray(new Scanner[var17.size()]));
      }

      if (!var12.isEmpty()) {
         var1.addClassLoaders((Collection)var12);
      }

      return var1;
   }

   public ConfigurationBuilder forPackages(String... var1) {
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         this.addUrls(ClasspathHelper.forPackage(var5));
      }

      return this;
   }

   @Nonnull
   public Set getScanners() {
      return this.scanners;
   }

   public ConfigurationBuilder setScanners(@Nonnull Scanner... var1) {
      this.scanners.clear();
      return this.addScanners(var1);
   }

   public ConfigurationBuilder addScanners(Scanner... var1) {
      this.scanners.addAll(Sets.newHashSet((Object[])var1));
      return this;
   }

   @Nonnull
   public Set getUrls() {
      return this.urls;
   }

   public ConfigurationBuilder setUrls(@Nonnull Collection var1) {
      this.urls = Sets.newHashSet((Iterable)var1);
      return this;
   }

   public ConfigurationBuilder setUrls(URL... var1) {
      this.urls = Sets.newHashSet((Object[])var1);
      return this;
   }

   public ConfigurationBuilder addUrls(Collection var1) {
      this.urls.addAll(var1);
      return this;
   }

   public ConfigurationBuilder addUrls(URL... var1) {
      this.urls.addAll(Sets.newHashSet((Object[])var1));
      return this;
   }

   public MetadataAdapter getMetadataAdapter() {
      if (this.metadataAdapter != null) {
         return this.metadataAdapter;
      } else {
         try {
            return this.metadataAdapter = new JavassistAdapter();
         } catch (Throwable var2) {
            if (Reflections.log != null) {
               Reflections.log.warn("could not create JavassistAdapter, using JavaReflectionAdapter", var2);
            }

            return this.metadataAdapter = new JavaReflectionAdapter();
         }
      }
   }

   public ConfigurationBuilder setMetadataAdapter(MetadataAdapter var1) {
      this.metadataAdapter = var1;
      return this;
   }

   @Nullable
   public Predicate getInputsFilter() {
      return this.inputsFilter;
   }

   public void setInputsFilter(@Nullable Predicate var1) {
      this.inputsFilter = var1;
   }

   public ConfigurationBuilder filterInputsBy(Predicate var1) {
      this.inputsFilter = var1;
      return this;
   }

   @Nullable
   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public ConfigurationBuilder setExecutorService(@Nullable ExecutorService var1) {
      this.executorService = var1;
      return this;
   }

   public ConfigurationBuilder useParallelExecutor() {
      return this.useParallelExecutor(Runtime.getRuntime().availableProcessors());
   }

   public ConfigurationBuilder useParallelExecutor(int var1) {
      this.setExecutorService(Executors.newFixedThreadPool(var1));
      return this;
   }

   public Serializer getSerializer() {
      return this.serializer != null ? this.serializer : (this.serializer = new XmlSerializer());
   }

   public ConfigurationBuilder setSerializer(Serializer var1) {
      this.serializer = var1;
      return this;
   }

   @Nullable
   public ClassLoader[] getClassLoaders() {
      return this.classLoaders;
   }

   public void setClassLoaders(@Nullable ClassLoader[] var1) {
      this.classLoaders = var1;
   }

   public ConfigurationBuilder addClassLoader(ClassLoader var1) {
      return this.addClassLoaders(var1);
   }

   public ConfigurationBuilder addClassLoaders(ClassLoader... var1) {
      this.classLoaders = this.classLoaders == null ? var1 : (ClassLoader[])ObjectArrays.concat(this.classLoaders, var1, ClassLoader.class);
      return this;
   }

   public ConfigurationBuilder addClassLoaders(Collection var1) {
      return this.addClassLoaders((ClassLoader[])var1.toArray(new ClassLoader[var1.size()]));
   }
}
