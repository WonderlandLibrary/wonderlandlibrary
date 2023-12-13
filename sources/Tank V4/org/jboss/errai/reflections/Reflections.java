package org.jboss.errai.reflections;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Pattern;
import org.jboss.errai.reflections.scanners.Scanner;
import org.jboss.errai.reflections.scanners.SubTypesScanner;
import org.jboss.errai.reflections.scanners.TypeAnnotationsScanner;
import org.jboss.errai.reflections.serializers.Serializer;
import org.jboss.errai.reflections.util.ClasspathHelper;
import org.jboss.errai.reflections.util.ConfigurationBuilder;
import org.jboss.errai.reflections.util.FilterBuilder;
import org.jboss.errai.reflections.util.Utils;
import org.jboss.errai.reflections.vfs.Vfs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reflections extends ReflectionUtils {
   private static final Logger log = LoggerFactory.getLogger(Reflections.class);
   protected final transient Configuration configuration;
   private Store store;

   public Reflections(Configuration var1) {
      this.configuration = var1;
      this.store = new Store(var1);
      Iterator var2 = var1.getScanners().iterator();

      while(var2.hasNext()) {
         Scanner var3 = (Scanner)var2.next();
         var3.setConfiguration(var1);
         var3.setStore(this.store.get(var3));
      }

   }

   public Reflections(String var1, Scanner... var2) {
      this(new ConfigurationBuilder(var1, var2) {
         final String val$prefix;
         final Scanner[] val$scanners;

         {
            this.val$prefix = var1;
            this.val$scanners = var2;
            FilterBuilder.Include var3 = new FilterBuilder.Include(FilterBuilder.prefix(this.val$prefix));
            this.setUrls(ClasspathHelper.forPackage(this.val$prefix));
            this.filterInputsBy(var3);
            if (this.val$scanners != null && this.val$scanners.length != 0) {
               Scanner[] var4 = this.val$scanners;
               int var5 = var4.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  Scanner var7 = var4[var6];
                  var7.filterResultsBy(Predicates.and(var3, var7.getResultFilter()));
               }

               this.setScanners(this.val$scanners);
            } else {
               this.setScanners(new Scanner[]{(new TypeAnnotationsScanner()).filterResultsBy(var3), (new SubTypesScanner()).filterResultsBy(var3)});
            }

         }
      });
   }

   public Reflections(Object[] var1, Scanner... var2) {
      this(new ConfigurationBuilder(var1, var2) {
         final Object[] val$urlHints;
         final Scanner[] val$scanners;

         {
            this.val$urlHints = var1;
            this.val$scanners = var2;
            ArrayList var3 = Lists.newArrayList();
            Object[] var4 = this.val$urlHints;
            int var5 = var4.length;

            int var6;
            for(var6 = 0; var6 < var5; ++var6) {
               Object var7 = var4[var6];
               if (var7 instanceof String) {
                  this.addUrls(ClasspathHelper.forPackage((String)var7));
                  var3.add((String)var7);
               } else if (var7 instanceof Class) {
                  this.addUrls(new URL[]{ClasspathHelper.forClass((Class)var7)});
                  var3.add(((Class)var7).getPackage().getName());
               }
            }

            FilterBuilder var9 = new FilterBuilder();
            Iterator var10 = var3.iterator();

            while(var10.hasNext()) {
               String var12 = (String)var10.next();
               var9.include(FilterBuilder.prefix(var12));
            }

            this.filterInputsBy(var9);
            if (this.val$scanners != null && this.val$scanners.length != 0) {
               Scanner[] var11 = this.val$scanners;
               var6 = var11.length;

               for(int var13 = 0; var13 < var6; ++var13) {
                  Scanner var8 = var11[var13];
                  var8.filterResultsBy(Predicates.and(var9, var8.getResultFilter()));
               }

               this.setScanners(this.val$scanners);
            } else {
               this.setScanners(new Scanner[]{(new TypeAnnotationsScanner()).filterResultsBy(var9), (new SubTypesScanner()).filterResultsBy(var9)});
            }

         }
      });
   }

   protected Reflections() {
      this.configuration = null;
   }

   protected void scan() {
      if (this.configuration.getUrls() != null && !this.configuration.getUrls().isEmpty()) {
         if (log.isDebugEnabled()) {
            StringBuilder var1 = new StringBuilder();
            Iterator var2 = this.configuration.getUrls().iterator();

            while(var2.hasNext()) {
               URL var3 = (URL)var2.next();
               var1.append("\t").append(var3.toExternalForm()).append("\n");
            }

            log.debug("going to scan these urls:\n" + var1);
         }

         long var13 = System.currentTimeMillis();
         ExecutorService var14 = this.configuration.getExecutorService();
         if (var14 == null) {
            Iterator var4 = this.configuration.getUrls().iterator();

            while(var4.hasNext()) {
               URL var5 = (URL)var4.next();

               try {
                  Iterator var6 = Vfs.fromURL(var5).getFiles().iterator();

                  while(var6.hasNext()) {
                     Vfs.File var7 = (Vfs.File)var6.next();
                     this.scan(var7);
                  }
               } catch (ReflectionsException var12) {
                  log.error("could not create Vfs.Dir from url. ignoring the exception and continuing", var12);
               }
            }
         } else {
            ArrayList var15 = Lists.newArrayList();
            Iterator var17 = this.configuration.getUrls().iterator();

            while(var17.hasNext()) {
               URL var19 = (URL)var17.next();

               try {
                  Iterator var21 = Vfs.fromURL(var19).getFiles().iterator();

                  while(var21.hasNext()) {
                     Vfs.File var8 = (Vfs.File)var21.next();
                     var15.add(var14.submit(new Runnable(this, var8) {
                        final Vfs.File val$file;
                        final Reflections this$0;

                        {
                           this.this$0 = var1;
                           this.val$file = var2;
                        }

                        public void run() {
                           Reflections.access$000(this.this$0, this.val$file);
                        }
                     }));
                  }
               } catch (ReflectionsException var11) {
                  log.error("could not create Vfs.Dir from url. ignoring the exception and continuing", var11);
               }
            }

            var17 = var15.iterator();

            while(var17.hasNext()) {
               Future var20 = (Future)var17.next();

               try {
                  var20.get();
               } catch (Exception var10) {
                  throw new RuntimeException(var10);
               }
            }

            var14.shutdown();
         }

         var13 = System.currentTimeMillis() - var13;
         Integer var16 = this.store.getKeysCount();
         Integer var18 = this.store.getValuesCount();
         log.info(String.format("Reflections took %d ms to scan %d urls, producing %d keys and %d values %s", var13, this.configuration.getUrls().size(), var16, var18, var14 != null && var14 instanceof ThreadPoolExecutor ? String.format("[using %d cores]", ((ThreadPoolExecutor)var14).getMaximumPoolSize()) : ""));
      }
   }

   private void scan(Vfs.File var1) {
      String var2 = var1.getRelativePath().replace('/', '.');
      if (this.configuration.acceptsInput(var2)) {
         Iterator var3 = this.configuration.getScanners().iterator();

         while(var3.hasNext()) {
            Scanner var4 = (Scanner)var3.next();

            try {
               if (var4.acceptsInput(var2)) {
                  var4.scan(var1);
               }
            } catch (Exception var6) {
               log.warn("could not scan file " + var1.getFullPath() + " with scanner " + var4.getName(), var6);
            }
         }
      }

   }

   public static Reflections collect() {
      return (new Reflections(new ConfigurationBuilder())).collect("META-INF/reflections", (new FilterBuilder()).include(".*-reflections.xml"));
   }

   public Reflections collect(String var1, Predicate var2) {
      return this.collect(var1, var2, this.configuration.getSerializer());
   }

   public Reflections collect(String var1, Predicate var2, Serializer var3) {
      InputStream var6;
      for(Iterator var4 = Vfs.findFiles(ClasspathHelper.forPackage(var1), var1, var2).iterator(); var4.hasNext(); Utils.close(var6)) {
         Vfs.File var5 = (Vfs.File)var4.next();
         var6 = null;

         try {
            var6 = var5.openInputStream();
            this.merge(var3.read(var6));
            log.info("Reflections collected metadata from " + var5 + " using serializer " + var3.getClass().getName());
         } catch (IOException var9) {
            throw new ReflectionsException("could not merge " + var5, var9);
         }
      }

      return this;
   }

   public Reflections collect(InputStream var1) {
      try {
         this.merge(this.configuration.getSerializer().read(var1));
         log.info("Reflections collected metadata from input stream using serializer " + this.configuration.getSerializer().getClass().getName());
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
      } catch (FileNotFoundException var8) {
         throw new ReflectionsException("could not obtain input stream from file " + var1, var8);
      }

      if (var2 != null) {
         try {
            var2.close();
         } catch (IOException var7) {
         }
      }

      return var3;
   }

   public Reflections merge(Reflections var1) {
      this.store.merge(var1.store);
      return this;
   }

   public Set getSubTypesOf(Class var1) {
      Set var2 = this.store.getSubTypesOf(var1.getName());
      return ImmutableSet.copyOf((Object[])ReflectionUtils.forNames(var2));
   }

   public Set getTypesAnnotatedWith(Class var1) {
      Set var2 = this.store.getTypesAnnotatedWith(var1.getName());
      return ImmutableSet.copyOf((Object[])forNames(var2, new ClassLoader[0]));
   }

   public Set getTypesAnnotatedWith(Class var1, boolean var2) {
      Set var3 = this.store.getTypesAnnotatedWith(var1.getName(), var2);
      return ImmutableSet.copyOf((Object[])forNames(var3, new ClassLoader[0]));
   }

   public Set getTypesAnnotatedWith(Annotation var1) {
      return this.getTypesAnnotatedWith(var1, true);
   }

   public Set getTypesAnnotatedWith(Annotation var1, boolean var2) {
      return getMatchingAnnotations(this.getTypesAnnotatedWith(var1.annotationType(), var2), var1);
   }

   public Set getMethodsAnnotatedWith(Class var1) {
      Set var2 = this.store.getMethodsAnnotatedWith(var1.getName());
      HashSet var3 = Sets.newHashSet();
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var3.add(Utils.getMethodFromDescriptor(var5));
      }

      return var3;
   }

   public Set getMethodsAnnotatedWith(Annotation var1) {
      return getMatchingAnnotations(this.getMethodsAnnotatedWith(var1.annotationType()), var1);
   }

   public Set getFieldsAnnotatedWith(Class var1) {
      HashSet var2 = Sets.newHashSet();
      Set var3 = this.store.getFieldsAnnotatedWith(var1.getName());
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var2.add(Utils.getFieldFromString(var5));
      }

      return var2;
   }

   public Set getFieldsAnnotatedWith(Annotation var1) {
      return getMatchingAnnotations(this.getFieldsAnnotatedWith(var1.annotationType()), var1);
   }

   public Set getConverters(Class var1, Class var2) {
      HashSet var3 = Sets.newHashSet();
      Set var4 = this.store.getConverters(var1.getName(), var2.getName());
      Iterator var5 = var4.iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         var3.add(Utils.getMethodFromDescriptor(var6));
      }

      return var3;
   }

   public Set getResources(Predicate var1) {
      return this.store.getResources(var1);
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

   public Store getStore() {
      return this.store;
   }

   public File save(String var1) {
      return this.save(var1, this.configuration.getSerializer());
   }

   public File save(String var1, Serializer var2) {
      File var3 = var2.save(this, var1);
      log.info("Reflections successfully saved in " + var3 + " using " + var2.getClass().getSimpleName());
      return var3;
   }

   static void access$000(Reflections var0, Vfs.File var1) {
      var0.scan(var1);
   }
}
