package org.jboss.errai.reflections;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jboss.errai.reflections.util.ClasspathHelper;

public abstract class ReflectionUtils {
   public static final List primitiveNames = Lists.newArrayList((Object[])("boolean", "char", "byte", "short", "int", "long", "float", "double", "void"));
   public static final List primitiveTypes;
   public static final List primitiveDescriptors;

   public static Collection getAllSuperTypes(Class var0) {
      ArrayList var1 = Lists.newArrayList();
      Class var2 = var0.getSuperclass();
      Class[] var3 = var0.getInterfaces();
      Collections.addAll(var1, var3);
      var1.add(var2);
      Collection var8 = Collections2.filter(var1, Predicates.notNull());
      ArrayList var4 = Lists.newArrayList();
      Iterator var5 = var8.iterator();

      while(var5.hasNext()) {
         Class var6 = (Class)var5.next();
         Collection var7 = getAllSuperTypes(var6);
         var4.addAll(var7);
      }

      var8.addAll(var4);
      return var8;
   }

   public static List getAllSuperTypesAnnotatedWith(AnnotatedElement var0, Annotation var1) {
      ArrayList var2 = Lists.newArrayList();
      if (var0 != null) {
         if (var0.isAnnotationPresent(var1.annotationType())) {
            var2.add(var0);
         }

         if (var0 instanceof Class) {
            ArrayList var3 = Lists.newArrayList();
            Class var4 = (Class)var0;
            var3.addAll(getAllSuperTypesAnnotatedWith(var4.getSuperclass(), var1));
            Class[] var5 = var4.getInterfaces();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               Class var8 = var5[var7];
               var3.addAll(getAllSuperTypesAnnotatedWith(var8, var1));
            }

            var2.addAll(var3);
         }
      }

      return var2;
   }

   public static boolean areAnnotationMembersMatching(Annotation var0, Annotation var1) {
      if (var1 != null && var0.annotationType() == var1.annotationType()) {
         Method[] var2 = var0.annotationType().getDeclaredMethods();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Method var5 = var2[var4];

            try {
               if (!var5.invoke(var0).equals(var5.invoke(var1))) {
                  return false;
               }
            } catch (Exception var7) {
               throw new ReflectionsException(String.format("could not invoke method %s on annotation %s", var5.getName(), var0.annotationType()), var7);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected static Set getMatchingAnnotations(Set var0, Annotation var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         AnnotatedElement var4 = (AnnotatedElement)var3.next();
         if (var4 == false) {
            var2.add(var4);
         }
      }

      return var2;
   }

   public static Class forName(String var0, ClassLoader... var1) {
      if (primitiveNames.contains(var0)) {
         return (Class)primitiveTypes.get(primitiveNames.indexOf(var0));
      } else {
         String var2;
         if (var0.contains("[")) {
            int var3 = var0.indexOf("[");
            var2 = var0.substring(0, var3);
            String var4 = var0.substring(var3).replace("]", "");
            if (primitiveNames.contains(var2)) {
               var2 = (String)primitiveDescriptors.get(primitiveNames.indexOf(var2));
            } else {
               var2 = "L" + var2 + ";";
            }

            var2 = var4 + var2;
         } else {
            var2 = var0;
         }

         ClassLoader[] var9 = ClasspathHelper.classLoaders(var1);
         int var10 = var9.length;
         int var5 = 0;

         while(var5 < var10) {
            ClassLoader var6 = var9[var5];

            try {
               return Class.forName(var2, false, var6);
            } catch (ClassNotFoundException var8) {
               ++var5;
            }
         }

         throw new IllegalArgumentException("Unable to load class \"" + var2 + "\"");
      }
   }

   public static Class[] forNames(Iterable var0, ClassLoader... var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(forName(var4, var1));
      }

      return (Class[])var2.toArray(new Class[var2.size()]);
   }

   static {
      primitiveTypes = Lists.newArrayList((Object[])(Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE));
      primitiveDescriptors = Lists.newArrayList((Object[])("Z", "C", "B", "S", "I", "J", "F", "D", "V"));
   }
}
