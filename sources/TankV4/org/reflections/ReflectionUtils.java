package org.reflections;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.Utils;

public abstract class ReflectionUtils {
   public static boolean includeObject = false;
   private static List primitiveNames;
   private static List primitiveTypes;
   private static List primitiveDescriptors;

   public static Set getAllSuperTypes(Class var0, Predicate... var1) {
      LinkedHashSet var2 = Sets.newLinkedHashSet();
      if (var0 != null && (includeObject || !var0.equals(Object.class))) {
         var2.add(var0);
         var2.addAll(getAllSuperTypes(var0.getSuperclass()));
         Class[] var3 = var0.getInterfaces();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class var6 = var3[var5];
            var2.addAll(getAllSuperTypes(var6));
         }
      }

      return filter((Iterable)var2, var1);
   }

   public static Set getAllMethods(Class var0, Predicate... var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = getAllSuperTypes(var0).iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         var2.addAll(getMethods(var4, var1));
      }

      return var2;
   }

   public static Set getMethods(Class var0, Predicate... var1) {
      return filter((Object[])(var0.isInterface() ? var0.getMethods() : var0.getDeclaredMethods()), var1);
   }

   public static Set getAllConstructors(Class var0, Predicate... var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = getAllSuperTypes(var0).iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         var2.addAll(getConstructors(var4, var1));
      }

      return var2;
   }

   public static Set getConstructors(Class var0, Predicate... var1) {
      return filter((Object[])var0.getDeclaredConstructors(), var1);
   }

   public static Set getAllFields(Class var0, Predicate... var1) {
      HashSet var2 = Sets.newHashSet();
      Iterator var3 = getAllSuperTypes(var0).iterator();

      while(var3.hasNext()) {
         Class var4 = (Class)var3.next();
         var2.addAll(getFields(var4, var1));
      }

      return var2;
   }

   public static Set getFields(Class var0, Predicate... var1) {
      return filter((Object[])var0.getDeclaredFields(), var1);
   }

   public static Set getAllAnnotations(AnnotatedElement var0, Predicate... var1) {
      HashSet var2 = Sets.newHashSet();
      if (var0 instanceof Class) {
         Iterator var3 = getAllSuperTypes((Class)var0).iterator();

         while(var3.hasNext()) {
            Class var4 = (Class)var3.next();
            var2.addAll(getAnnotations(var4, var1));
         }
      } else {
         var2.addAll(getAnnotations(var0, var1));
      }

      return var2;
   }

   public static Set getAnnotations(AnnotatedElement var0, Predicate... var1) {
      return filter((Object[])var0.getDeclaredAnnotations(), var1);
   }

   public static Set getAll(Set var0, Predicate... var1) {
      return (Set)(Utils.isEmpty(var1) ? var0 : Sets.newHashSet(Iterables.filter(var0, (Predicate)Predicates.and(var1))));
   }

   public static Predicate withName(String var0) {
      return new Predicate(var0) {
         final String val$name;

         {
            this.val$name = var1;
         }

         public boolean apply(@Nullable Member var1) {
            return var1 != null && var1.getName().equals(this.val$name);
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Member)var1);
         }
      };
   }

   public static Predicate withPrefix(String var0) {
      return new Predicate(var0) {
         final String val$prefix;

         {
            this.val$prefix = var1;
         }

         public boolean apply(@Nullable Member var1) {
            return var1 != null && var1.getName().startsWith(this.val$prefix);
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Member)var1);
         }
      };
   }

   public static Predicate withPattern(String var0) {
      return new Predicate(var0) {
         final String val$regex;

         {
            this.val$regex = var1;
         }

         public boolean apply(@Nullable AnnotatedElement var1) {
            return Pattern.matches(this.val$regex, var1.toString());
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((AnnotatedElement)var1);
         }
      };
   }

   public static Predicate withAnnotation(Class var0) {
      return new Predicate(var0) {
         final Class val$annotation;

         {
            this.val$annotation = var1;
         }

         public boolean apply(@Nullable AnnotatedElement var1) {
            return var1 != null && var1.isAnnotationPresent(this.val$annotation);
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((AnnotatedElement)var1);
         }
      };
   }

   public static Predicate withAnnotations(Class... var0) {
      return new Predicate(var0) {
         final Class[] val$annotations;

         {
            this.val$annotations = var1;
         }

         public boolean apply(@Nullable AnnotatedElement var1) {
            return var1 != null && Arrays.equals(this.val$annotations, ReflectionUtils.access$000(var1.getAnnotations()));
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((AnnotatedElement)var1);
         }
      };
   }

   public static Predicate withAnnotation(Annotation var0) {
      return new Predicate(var0) {
         final Annotation val$annotation;

         {
            this.val$annotation = var1;
         }

         public boolean apply(@Nullable AnnotatedElement var1) {
            return var1 != null && var1.isAnnotationPresent(this.val$annotation.annotationType()) && ReflectionUtils.access$100(var1.getAnnotation(this.val$annotation.annotationType()), this.val$annotation);
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((AnnotatedElement)var1);
         }
      };
   }

   public static Predicate withAnnotations(Annotation... var0) {
      return new Predicate(var0) {
         final Annotation[] val$annotations;

         {
            this.val$annotations = var1;
         }

         public boolean apply(@Nullable AnnotatedElement var1) {
            if (var1 != null) {
               Annotation[] var2 = var1.getAnnotations();
               if (var2.length == this.val$annotations.length) {
                  for(int var3 = 0; var3 < var2.length; ++var3) {
                     if (!ReflectionUtils.access$100(var2[var3], this.val$annotations[var3])) {
                        return false;
                     }
                  }
               }
            }

            return true;
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((AnnotatedElement)var1);
         }
      };
   }

   public static Predicate withParameters(Class... var0) {
      return new Predicate(var0) {
         final Class[] val$types;

         {
            this.val$types = var1;
         }

         public boolean apply(@Nullable Member var1) {
            return Arrays.equals(ReflectionUtils.access$200(var1), this.val$types);
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Member)var1);
         }
      };
   }

   public static Predicate withParametersAssignableTo(Class... var0) {
      return new Predicate(var0) {
         final Class[] val$types;

         {
            this.val$types = var1;
         }

         public boolean apply(@Nullable Member var1) {
            if (var1 != null) {
               Class[] var2 = ReflectionUtils.access$200(var1);
               if (var2.length == this.val$types.length) {
                  for(int var3 = 0; var3 < var2.length; ++var3) {
                     if (!var2[var3].isAssignableFrom(this.val$types[var3]) || var2[var3] == Object.class && this.val$types[var3] != Object.class) {
                        return false;
                     }
                  }

                  return true;
               }
            }

            return false;
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Member)var1);
         }
      };
   }

   public static Predicate withParametersCount(int var0) {
      return new Predicate(var0) {
         final int val$count;

         {
            this.val$count = var1;
         }

         public boolean apply(@Nullable Member var1) {
            return var1 != null && ReflectionUtils.access$200(var1).length == this.val$count;
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Member)var1);
         }
      };
   }

   public static Predicate withAnyParameterAnnotation(Class var0) {
      return new Predicate(var0) {
         final Class val$annotationClass;

         {
            this.val$annotationClass = var1;
         }

         public boolean apply(@Nullable Member var1) {
            return var1 != null && Iterables.any(ReflectionUtils.access$400(ReflectionUtils.access$300(var1)), new Predicate(this) {
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
               }

               public boolean apply(@Nullable Class var1) {
                  return var1.equals(this.this$0.val$annotationClass);
               }

               public boolean apply(@Nullable Object var1) {
                  return this.apply((Class)var1);
               }
            });
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Member)var1);
         }
      };
   }

   public static Predicate withAnyParameterAnnotation(Annotation var0) {
      return new Predicate(var0) {
         final Annotation val$annotation;

         {
            this.val$annotation = var1;
         }

         public boolean apply(@Nullable Member var1) {
            return var1 != null && Iterables.any(ReflectionUtils.access$300(var1), new Predicate(this) {
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
               }

               public boolean apply(@Nullable Annotation var1) {
                  return ReflectionUtils.access$100(this.this$0.val$annotation, var1);
               }

               public boolean apply(@Nullable Object var1) {
                  return this.apply((Annotation)var1);
               }
            });
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Member)var1);
         }
      };
   }

   public static Predicate withType(Class var0) {
      return new Predicate(var0) {
         final Class val$type;

         {
            this.val$type = var1;
         }

         public boolean apply(@Nullable Field var1) {
            return var1 != null && var1.getType().equals(this.val$type);
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Field)var1);
         }
      };
   }

   public static Predicate withTypeAssignableTo(Class var0) {
      return new Predicate(var0) {
         final Class val$type;

         {
            this.val$type = var1;
         }

         public boolean apply(@Nullable Field var1) {
            return var1 != null && this.val$type.isAssignableFrom(var1.getType());
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Field)var1);
         }
      };
   }

   public static Predicate withReturnType(Class var0) {
      return new Predicate(var0) {
         final Class val$type;

         {
            this.val$type = var1;
         }

         public boolean apply(@Nullable Method var1) {
            return var1 != null && var1.getReturnType().equals(this.val$type);
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Method)var1);
         }
      };
   }

   public static Predicate withReturnTypeAssignableTo(Class var0) {
      return new Predicate(var0) {
         final Class val$type;

         {
            this.val$type = var1;
         }

         public boolean apply(@Nullable Method var1) {
            return var1 != null && this.val$type.isAssignableFrom(var1.getReturnType());
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Method)var1);
         }
      };
   }

   public static Predicate withModifier(int var0) {
      return new Predicate(var0) {
         final int val$mod;

         {
            this.val$mod = var1;
         }

         public boolean apply(@Nullable Member var1) {
            return var1 != null && (var1.getModifiers() & this.val$mod) != 0;
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Member)var1);
         }
      };
   }

   public static Predicate withClassModifier(int var0) {
      return new Predicate(var0) {
         final int val$mod;

         {
            this.val$mod = var1;
         }

         public boolean apply(@Nullable Class var1) {
            return var1 != null && (var1.getModifiers() & this.val$mod) != 0;
         }

         public boolean apply(@Nullable Object var1) {
            return this.apply((Class)var1);
         }
      };
   }

   public static Class forName(String var0, ClassLoader... var1) {
      if (getPrimitiveNames().contains(var0)) {
         return (Class)getPrimitiveTypes().get(getPrimitiveNames().indexOf(var0));
      } else {
         String var2;
         if (var0.contains("[")) {
            int var3 = var0.indexOf("[");
            var2 = var0.substring(0, var3);
            String var4 = var0.substring(var3).replace("]", "");
            if (getPrimitiveNames().contains(var2)) {
               var2 = (String)getPrimitiveDescriptors().get(getPrimitiveNames().indexOf(var2));
            } else {
               var2 = "L" + var2 + ";";
            }

            var2 = var4 + var2;
         } else {
            var2 = var0;
         }

         ArrayList var11 = Lists.newArrayList();
         ClassLoader[] var12 = ClasspathHelper.classLoaders(var1);
         int var5 = var12.length;
         int var6 = 0;

         while(var6 < var5) {
            ClassLoader var7 = var12[var6];
            if (var2.contains("[")) {
               try {
                  return Class.forName(var2, false, var7);
               } catch (Throwable var10) {
                  var11.add(new ReflectionsException("could not get type for name " + var0, var10));
               }
            }

            try {
               return var7.loadClass(var2);
            } catch (Throwable var9) {
               var11.add(new ReflectionsException("could not get type for name " + var0, var9));
               ++var6;
            }
         }

         if (Reflections.log != null) {
            Iterator var13 = var11.iterator();

            while(var13.hasNext()) {
               ReflectionsException var14 = (ReflectionsException)var13.next();
               Reflections.log.warn("could not get type for name " + var0 + " from any class loader", var14);
            }
         }

         return null;
      }
   }

   public static List forNames(Iterable var0, ClassLoader... var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         Class var5 = forName(var4, var1);
         if (var5 != null) {
            var2.add(var5);
         }
      }

      return var2;
   }

   private static Class[] parameterTypes(Member var0) {
      return var0 != null ? (var0.getClass() == Method.class ? ((Method)var0).getParameterTypes() : (var0.getClass() == Constructor.class ? ((Constructor)var0).getParameterTypes() : null)) : null;
   }

   private static Set parameterAnnotations(Member var0) {
      HashSet var1 = Sets.newHashSet();
      Annotation[][] var2 = var0 instanceof Method ? ((Method)var0).getParameterAnnotations() : (var0 instanceof Constructor ? ((Constructor)var0).getParameterAnnotations() : (Annotation[][])null);
      Annotation[][] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Annotation[] var6 = var3[var5];
         Collections.addAll(var1, var6);
      }

      return var1;
   }

   private static Set annotationTypes(Iterable var0) {
      HashSet var1 = Sets.newHashSet();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Annotation var3 = (Annotation)var2.next();
         var1.add(var3.annotationType());
      }

      return var1;
   }

   private static Class[] annotationTypes(Annotation[] var0) {
      Class[] var1 = new Class[var0.length];

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1[var2] = var0[var2].annotationType();
      }

      return var1;
   }

   private static void initPrimitives() {
      if (primitiveNames == null) {
         primitiveNames = Lists.newArrayList((Object[])("boolean", "char", "byte", "short", "int", "long", "float", "double", "void"));
         primitiveTypes = Lists.newArrayList((Object[])(Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE));
         primitiveDescriptors = Lists.newArrayList((Object[])("Z", "C", "B", "S", "I", "J", "F", "D", "V"));
      }

   }

   private static List getPrimitiveNames() {
      initPrimitives();
      return primitiveNames;
   }

   private static List getPrimitiveTypes() {
      initPrimitives();
      return primitiveTypes;
   }

   private static List getPrimitiveDescriptors() {
      initPrimitives();
      return primitiveDescriptors;
   }

   static Set filter(Object[] var0, Predicate... var1) {
      return Utils.isEmpty(var1) ? Sets.newHashSet(var0) : Sets.newHashSet(Iterables.filter(Arrays.asList(var0), (Predicate)Predicates.and(var1)));
   }

   static Set filter(Iterable var0, Predicate... var1) {
      return Utils.isEmpty(var1) ? Sets.newHashSet(var0) : Sets.newHashSet(Iterables.filter(var0, Predicates.and(var1)));
   }

   private static boolean areAnnotationMembersMatching(Annotation var0, Annotation var1) {
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

   static Class[] access$000(Annotation[] var0) {
      return annotationTypes(var0);
   }

   static boolean access$100(Annotation var0, Annotation var1) {
      return areAnnotationMembersMatching(var0, var1);
   }

   static Class[] access$200(Member var0) {
      return parameterTypes(var0);
   }

   static Set access$300(Member var0) {
      return parameterAnnotations(var0);
   }

   static Set access$400(Iterable var0) {
      return annotationTypes(var0);
   }
}
