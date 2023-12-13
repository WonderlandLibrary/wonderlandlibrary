package org.jboss.errai.reflections;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import java.lang.annotation.Inherited;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import org.jboss.errai.reflections.scanners.ConvertersScanner;
import org.jboss.errai.reflections.scanners.FieldAnnotationsScanner;
import org.jboss.errai.reflections.scanners.MethodAnnotationsScanner;
import org.jboss.errai.reflections.scanners.ResourcesScanner;
import org.jboss.errai.reflections.scanners.Scanner;
import org.jboss.errai.reflections.scanners.SubTypesScanner;
import org.jboss.errai.reflections.scanners.TypeAnnotationsScanner;

public class Store {
   private final LoadingCache loadingCache;

   public Store(Configuration var1) {
      this(var1.getExecutorService() != null);
   }

   protected Store(boolean var1) {
      CacheLoader var2 = CacheLoader.from(new Function(this) {
         final Store this$0;

         {
            this.this$0 = var1;
         }

         public Multimap apply(String var1) {
            return Multimaps.synchronizedSetMultimap(Multimaps.newSetMultimap(new HashMap(), new Supplier(this) {
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
               }

               public Set get() {
                  return Sets.newHashSet();
               }

               public Object get() {
                  return this.get();
               }
            }));
         }

         public Object apply(Object var1) {
            return this.apply((String)var1);
         }
      });
      this.loadingCache = CacheBuilder.newBuilder().build(var2);
   }

   protected Store() {
      this(false);
   }

   public Set get(Class var1, String... var2) {
      HashSet var3 = Sets.newHashSet();
      Multimap var4 = this.get(var1);
      String[] var5 = var2;
      int var6 = var2.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String var8 = var5[var7];
         var3.addAll(var4.get(var8));
      }

      return var3;
   }

   public Multimap get(Scanner var1) {
      return this.get(var1.getClass());
   }

   public Multimap get(Class var1) {
      return this.get(var1.getName());
   }

   public Multimap get(String var1) {
      try {
         return (Multimap)this.loadingCache.get(var1);
      } catch (ExecutionException var3) {
         throw new RuntimeException("error loading from cache", var3);
      }
   }

   public Map getStoreMap() {
      return this.loadingCache.asMap();
   }

   void merge(Store var1) {
      ConcurrentMap var2 = this.loadingCache.asMap();
      ConcurrentMap var3 = var1.loadingCache.asMap();

      String var5;
      Object var6;
      for(Iterator var4 = var3.keySet().iterator(); var4.hasNext(); ((Multimap)var6).putAll(var1.get(var5))) {
         var5 = (String)var4.next();
         var6 = (Multimap)var2.get(var5);
         if (var6 == null) {
            var2.put(var5, var6 = HashMultimap.create());
         }
      }

   }

   public Integer getKeysCount() {
      Integer var1 = 0;

      Multimap var3;
      for(Iterator var2 = this.loadingCache.asMap().values().iterator(); var2.hasNext(); var1 = var1 + var3.keySet().size()) {
         var3 = (Multimap)var2.next();
      }

      return var1;
   }

   public Integer getValuesCount() {
      Integer var1 = 0;

      Multimap var3;
      for(Iterator var2 = this.loadingCache.asMap().values().iterator(); var2.hasNext(); var1 = var1 + var3.size()) {
         var3 = (Multimap)var2.next();
      }

      return var1;
   }

   public Set getSubTypesOf(String var1) {
      HashSet var2 = new HashSet();
      Set var3 = this.get(SubTypesScanner.class, var1);
      var2.addAll(var3);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         String var5 = (String)var4.next();
         var2.addAll(this.getSubTypesOf(var5));
      }

      return var2;
   }

   public Set getTypesAnnotatedWith(String var1) {
      return this.getTypesAnnotatedWith(var1, true);
   }

   public Set getTypesAnnotatedWith(String var1, boolean var2) {
      HashSet var3 = new HashSet();
      if (this.isAnnotation(var1)) {
         Set var4 = this.get(TypeAnnotationsScanner.class, var1);
         var3.addAll(var4);
         Iterator var5;
         String var6;
         if (var2 && this.isInheritedAnnotation(var1)) {
            var5 = var4.iterator();

            while(var5.hasNext()) {
               var6 = (String)var5.next();
               if (var6 == false) {
                  var3.addAll(this.getSubTypesOf(var6));
               }
            }
         } else if (!var2) {
            var5 = var4.iterator();

            while(var5.hasNext()) {
               var6 = (String)var5.next();
               if (this.isAnnotation(var6)) {
                  var3.addAll(this.getTypesAnnotatedWith(var1, false));
               } else if (this.hasSubTypes(var6)) {
                  var3.addAll(this.getSubTypesOf(var6));
               }
            }
         }
      }

      return var3;
   }

   public Set getMethodsAnnotatedWith(String var1) {
      return this.get(MethodAnnotationsScanner.class, var1);
   }

   public Set getFieldsAnnotatedWith(String var1) {
      return this.get(FieldAnnotationsScanner.class, var1);
   }

   public Set getConverters(String var1, String var2) {
      return this.get(ConvertersScanner.class, ConvertersScanner.getConverterKey(var1, var2));
   }

   public Set getResources(String var1) {
      return this.get(ResourcesScanner.class, var1);
   }

   public Set getResources(Predicate var1) {
      Set var2 = this.get(ResourcesScanner.class).keySet();
      Collection var3 = Collections2.filter(var2, var1);
      return this.get(ResourcesScanner.class, (String[])var3.toArray(new String[var3.size()]));
   }

   public Set getResources(Pattern var1) {
      return this.getResources(new Predicate(this, var1) {
         final Pattern val$pattern;
         final Store this$0;

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

   public boolean isInterface(String var1) {
      return ReflectionUtils.forName(var1).isInterface();
   }

   public boolean isAnnotation(String var1) {
      return this.getTypeAnnotations().contains(var1);
   }

   public boolean isInheritedAnnotation(String var1) {
      return this.get(TypeAnnotationsScanner.class).get(Inherited.class.getName()).contains(var1);
   }

   public boolean hasSubTypes(String var1) {
      return this.getSuperTypes().contains(var1);
   }

   public Multiset getSuperTypes() {
      return this.get(SubTypesScanner.class).keys();
   }

   public Set getTypeAnnotations() {
      return this.get(TypeAnnotationsScanner.class).keySet();
   }
}
