package org.reflections;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MemberUsageScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.MethodParameterNamesScanner;
import org.reflections.scanners.MethodParameterScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.Scanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.Serializer;
import org.reflections.serializers.XmlSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;
import org.slf4j.Logger;

public class Reflections {
   @Nullable
   public static Logger log = Utils.findLogger(Reflections.class);
   protected final transient Configuration configuration;
   protected Store store;

   public Reflections(Configuration var1) {
      this.configuration = var1;
      this.store = new Store(var1);
      if (var1.getScanners() != null && !var1.getScanners().isEmpty()) {
         Iterator var2 = var1.getScanners().iterator();

         while(var2.hasNext()) {
            Scanner var3 = (Scanner)var2.next();
            var3.setConfiguration(var1);
            var3.setStore(this.store.getOrCreate(var3.getClass().getSimpleName()));
         }

         this.scan();
      }

   }

   public Reflections(String var1, @Nullable Scanner... var2) {
      this(var1, var2);
   }

   public Reflections(Object... var1) {
      this((Configuration)ConfigurationBuilder.build(var1));
   }

   protected Reflections() {
      this.configuration = new ConfigurationBuilder();
      this.store = new Store(this.configuration);
   }

   protected void scan() {
      if (this.configuration.getUrls() != null && !this.configuration.getUrls().isEmpty()) {
         if (log != null && log.isDebugEnabled()) {
            log.debug("going to scan these urls:\n" + Joiner.on("\n").join((Iterable)this.configuration.getUrls()));
         }

         long var1 = System.currentTimeMillis();
         int var3 = 0;
         ExecutorService var4 = this.configuration.getExecutorService();
         ArrayList var5 = Lists.newArrayList();
         Iterator var6 = this.configuration.getUrls().iterator();

         while(var6.hasNext()) {
            URL var7 = (URL)var6.next();

            try {
               if (var4 != null) {
                  var5.add(var4.submit(new Runnable(this, var7) {
                     final URL val$url;
                     final Reflections this$0;

                     {
                        this.this$0 = var1;
                        this.val$url = var2;
                     }

                     public void run() {
                        if (Reflections.log != null && Reflections.log.isDebugEnabled()) {
                           Reflections.log.debug("[" + Thread.currentThread().toString() + "] scanning " + this.val$url);
                        }

                        this.this$0.scan(this.val$url);
                     }
                  }));
               } else {
                  this.scan(var7);
               }

               ++var3;
            } catch (ReflectionsException var11) {
               if (log != null && log.isWarnEnabled()) {
                  log.warn("could not create Vfs.Dir from url. ignoring the exception and continuing", var11);
               }
            }
         }

         if (var4 != null) {
            var6 = var5.iterator();

            while(var6.hasNext()) {
               Future var13 = (Future)var6.next();

               try {
                  var13.get();
               } catch (Exception var10) {
                  throw new RuntimeException(var10);
               }
            }
         }

         var1 = System.currentTimeMillis() - var1;
         if (log != null) {
            int var12 = 0;
            int var14 = 0;

            String var9;
            for(Iterator var8 = this.store.keySet().iterator(); var8.hasNext(); var14 += this.store.get(var9).size()) {
               var9 = (String)var8.next();
               var12 += this.store.get(var9).keySet().size();
            }

            log.info(String.format("Reflections took %d ms to scan %d urls, producing %d keys and %d values %s", var1, var3, var12, var14, var4 != null && var4 instanceof ThreadPoolExecutor ? String.format("[using %d cores]", ((ThreadPoolExecutor)var4).getMaximumPoolSize()) : ""));
         }

      } else {
         if (log != null) {
            log.warn("given scan urls are empty. set urls in the configuration");
         }

      }
   }

   protected void scan(URL var1) {
      Vfs.Dir var2 = Vfs.fromURL(var1);
      Iterator var3 = var2.getFiles().iterator();

      while(true) {
         Vfs.File var4;
         Predicate var5;
         String var6;
         String var7;
         do {
            if (!var3.hasNext()) {
               var2.close();
               return;
            }

            var4 = (Vfs.File)var3.next();
            var5 = this.configuration.getInputsFilter();
            var6 = var4.getRelativePath();
            var7 = var6.replace('/', '.');
         } while(var5 != null && !var5.apply(var6) && !var5.apply(var7));

         Object var8 = null;
         Iterator var9 = this.configuration.getScanners().iterator();

         while(var9.hasNext()) {
            Scanner var10 = (Scanner)var9.next();

            try {
               if (var10.acceptsInput(var6) || var10.acceptResult(var7)) {
                  var8 = var10.scan(var4, var8);
               }
            } catch (Exception var13) {
               if (log != null && log.isDebugEnabled()) {
                  log.debug("could not scan file " + var4.getRelativePath() + " in url " + var1.toExternalForm() + " with scanner " + var10.getClass().getSimpleName(), var13.getMessage());
               }
            }
         }
      }
   }

   public static Reflections collect() {
      return collect("META-INF/reflections/", (new FilterBuilder()).include(".*-reflections.xml"));
   }

   public static Reflections collect(String var0, Predicate var1, @Nullable Serializer... var2) {
      Object var3 = var2 != null && var2.length == 1 ? var2[0] : new XmlSerializer();
      Collection var4 = ClasspathHelper.forPackage(var0);
      if (var4.isEmpty()) {
         return null;
      } else {
         long var5 = System.currentTimeMillis();
         Reflections var7 = new Reflections();
         Iterable var8 = Vfs.findFiles(var4, var0, var1);

         InputStream var11;
         for(Iterator var9 = var8.iterator(); var9.hasNext(); Utils.close(var11)) {
            Vfs.File var10 = (Vfs.File)var9.next();
            var11 = null;

            try {
               var11 = var10.openInputStream();
               var7.merge(((Serializer)var3).read(var11));
            } catch (IOException var14) {
               throw new ReflectionsException("could not merge " + var10, var14);
            }
         }

         if (log != null) {
            Store var15 = var7.getStore();
            int var16 = 0;
            int var17 = 0;

            String var13;
            for(Iterator var12 = var15.keySet().iterator(); var12.hasNext(); var17 += var15.get(var13).size()) {
               var13 = (String)var12.next();
               var16 += var15.get(var13).keySet().size();
            }

            log.info(String.format("Reflections took %d ms to collect %d url%s, producing %d keys and %d values [%s]", System.currentTimeMillis() - var5, var4.size(), var4.size() > 1 ? "s" : "", var16, var17, Joiner.on(", ").join((Iterable)var4)));
         }

         return var7;
      }
   }

   public Reflections collect(InputStream var1) {
      try {
         this.merge(this.configuration.getSerializer().read(var1));
         if (log != null) {
            log.info("Reflections collected metadata from input stream using serializer " + this.configuration.getSerializer().getClass().getName());
         }

         return this;
      } catch (Exception var3) {
         throw new ReflectionsException("could not merge input stream", var3);
      }
   }

   public Reflections collect(File var1) {
      FileInputStream var2 = null;

      Reflections var3;
      try {
         var2 = new FileInputStream(var1);
         var3 = this.collect((InputStream)var2);
      } catch (FileNotFoundException var5) {
         throw new ReflectionsException("could not obtain input stream from file " + var1, var5);
      }

      Utils.close(var2);
      return var3;
   }

   public Reflections merge(Reflections var1) {
      if (var1.store != null) {
         Iterator var2 = var1.store.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            Multimap var4 = var1.store.get(var3);
            Iterator var5 = var4.keySet().iterator();

            while(var5.hasNext()) {
               String var6 = (String)var5.next();
               Iterator var7 = var4.get(var6).iterator();

               while(var7.hasNext()) {
                  String var8 = (String)var7.next();
                  this.store.getOrCreate(var3).put(var6, var8);
               }
            }
         }
      }

      return this;
   }

   public Set getSubTypesOf(Class var1) {
      return Sets.newHashSet((Iterable)ReflectionUtils.forNames(this.store.getAll(index(SubTypesScanner.class), (Iterable)Arrays.asList(var1.getName())), this.loaders()));
   }

   public Set getTypesAnnotatedWith(Class var1) {
      return this.getTypesAnnotatedWith(var1, false);
   }

   public Set getTypesAnnotatedWith(Class var1, boolean var2) {
      Iterable var3 = this.store.get(index(TypeAnnotationsScanner.class), var1.getName());
      Iterable var4 = this.getAllAnnotated(var3, var1.isAnnotationPresent(Inherited.class), var2);
      return Sets.newHashSet(Iterables.concat(ReflectionUtils.forNames(var3, this.loaders()), ReflectionUtils.forNames(var4, this.loaders())));
   }

   public Set getTypesAnnotatedWith(Annotation var1) {
      return this.getTypesAnnotatedWith(var1, false);
   }

   public Set getTypesAnnotatedWith(Annotation var1, boolean var2) {
      Iterable var3 = this.store.get(index(TypeAnnotationsScanner.class), var1.annotationType().getName());
      Set var4 = ReflectionUtils.filter((Iterable)ReflectionUtils.forNames(var3, this.loaders()), ReflectionUtils.withAnnotation(var1));
      Iterable var5 = this.getAllAnnotated(Utils.names((Iterable)var4), var1.annotationType().isAnnotationPresent(Inherited.class), var2);
      return Sets.newHashSet(Iterables.concat(var4, ReflectionUtils.forNames(ReflectionUtils.filter(var5, Predicates.not(Predicates.in(Sets.newHashSet(var3)))), this.loaders())));
   }

   protected Iterable getAllAnnotated(Iterable var1, boolean var2, boolean var3) {
      Iterable var4;
      if (var3) {
         if (var2) {
            var4 = this.store.get(index(SubTypesScanner.class), (Iterable)ReflectionUtils.filter(var1, new Predicate(this) {
               final Reflections this$0;

               {
                  this.this$0 = var1;
               }

               public boolean apply(@Nullable String var1) {
                  return !ReflectionUtils.forName(var1, Reflections.access$000(this.this$0)).isInterface();
               }

               public boolean apply(@Nullable Object var1) {
                  return this.apply((String)var1);
               }
            }));
            return Iterables.concat(var4, this.store.getAll(index(SubTypesScanner.class), var4));
         } else {
            return var1;
         }
      } else {
         var4 = Iterables.concat(var1, this.store.getAll(index(TypeAnnotationsScanner.class), var1));
         return Iterables.concat(var4, this.store.getAll(index(SubTypesScanner.class), var4));
      }
   }

   public Set getMethodsAnnotatedWith(Class var1) {
      Iterable var2 = this.store.get(index(MethodAnnotationsScanner.class), var1.getName());
      return Utils.getMethodsFromDescriptors(var2, this.loaders());
   }

   public Set getMethodsAnnotatedWith(Annotation var1) {
      return ReflectionUtils.filter((Iterable)this.getMethodsAnnotatedWith(var1.annotationType()), ReflectionUtils.withAnnotation(var1));
   }

   public Set getMethodsMatchParams(Class... var1) {
      return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), Utils.names(var1).toString()), this.loaders());
   }

   public Set getMethodsReturn(Class var1) {
      return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), (Iterable)Utils.names(var1)), this.loaders());
   }

   public Set getMethodsWithAnyParamAnnotated(Class var1) {
      return Utils.getMethodsFromDescriptors(this.store.get(index(MethodParameterScanner.class), var1.getName()), this.loaders());
   }

   public Set getMethodsWithAnyParamAnnotated(Annotation var1) {
      return ReflectionUtils.filter((Iterable)this.getMethodsWithAnyParamAnnotated(var1.annotationType()), ReflectionUtils.withAnyParameterAnnotation(var1));
   }

   public Set getConstructorsAnnotatedWith(Class var1) {
      Iterable var2 = this.store.get(index(MethodAnnotationsScanner.class), var1.getName());
      return Utils.getConstructorsFromDescriptors(var2, this.loaders());
   }

   public Set getConstructorsAnnotatedWith(Annotation var1) {
      return ReflectionUtils.filter((Iterable)this.getConstructorsAnnotatedWith(var1.annotationType()), ReflectionUtils.withAnnotation(var1));
   }

   public Set getConstructorsMatchParams(Class... var1) {
      return Utils.getConstructorsFromDescriptors(this.store.get(index(MethodParameterScanner.class), Utils.names(var1).toString()), this.loaders());
   }

   public Set getConstructorsWithAnyParamAnnotated(Class var1) {
      return Utils.getConstructorsFromDescriptors(this.store.get(index(MethodParameterScanner.class), var1.getName()), this.loaders());
   }

   public Set getConstructorsWithAnyParamAnnotated(Annotation var1) {
      return ReflectionUtils.filter((Iterable)this.getConstructorsWithAnyParamAnnotated(var1.annotationType()), ReflectionUtils.withAnyParameterAnnotation(var1));
   }

   public Set getFieldsAnnotatedWith(Class var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = this.store.get(index(FieldAnnotationsScanner.class), var1.getName()).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(Utils.getFieldFromString(var4, this.loaders()));
      }

      return var2;
   }

   public Set getFieldsAnnotatedWith(Annotation var1) {
      return ReflectionUtils.filter((Iterable)this.getFieldsAnnotatedWith(var1.annotationType()), ReflectionUtils.withAnnotation(var1));
   }

   public Set getResources(Predicate var1) {
      Iterable var2 = Iterables.filter(this.store.get(index(ResourcesScanner.class)).keySet(), (Predicate)var1);
      return Sets.newHashSet(this.store.get(index(ResourcesScanner.class), var2));
   }

   public Set getResources(Pattern var1) {
      return this.getResources(new Predicate(this, var1) {
         final Pattern val$pattern;
         final Reflections this$0;

         {
            this.this$0 = var1;
            this.val$pattern = var2;
         }

         public boolean apply(String var1) {
            return this.val$pattern.matcher(var1).matches();
         }

         public boolean apply(Object var1) {
            return this.apply((String)var1);
         }
      });
   }

   public List getMethodParamNames(Method var1) {
      Iterable var2 = this.store.get(index(MethodParameterNamesScanner.class), Utils.name(var1));
      return !Iterables.isEmpty(var2) ? Arrays.asList(((String)Iterables.getOnlyElement(var2)).split(", ")) : Arrays.asList();
   }

   public List getConstructorParamNames(Constructor var1) {
      Iterable var2 = this.store.get(index(MethodParameterNamesScanner.class), Utils.name(var1));
      return !Iterables.isEmpty(var2) ? Arrays.asList(((String)Iterables.getOnlyElement(var2)).split(", ")) : Arrays.asList();
   }

   public Set getFieldUsage(Field var1) {
      return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(var1)));
   }

   public Set getMethodUsage(Method var1) {
      return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(var1)));
   }

   public Set getConstructorUsage(Constructor var1) {
      return Utils.getMembersFromDescriptors(this.store.get(index(MemberUsageScanner.class), Utils.name(var1)));
   }

   public Set getAllTypes() {
      HashSet var1 = Sets.newHashSet(this.store.getAll(index(SubTypesScanner.class), Object.class.getName()));
      if (var1.isEmpty()) {
         throw new ReflectionsException("Couldn't find subtypes of Object. Make sure SubTypesScanner initialized to include Object class - new SubTypesScanner(false)");
      } else {
         return var1;
      }
   }

   public Store getStore() {
      return this.store;
   }

   public Configuration getConfiguration() {
      return this.configuration;
   }

   public File save(String var1) {
      return this.save(var1, this.configuration.getSerializer());
   }

   public File save(String var1, Serializer var2) {
      File var3 = var2.save(this, var1);
      if (log != null) {
         log.info("Reflections successfully saved in " + var3.getAbsolutePath() + " using " + var2.getClass().getSimpleName());
      }

      return var3;
   }

   private static String index(Class var0) {
      return var0.getSimpleName();
   }

   private ClassLoader[] loaders() {
      return this.configuration.getClassLoaders();
   }

   static ClassLoader[] access$000(Reflections var0) {
      return var0.loaders();
   }
}
