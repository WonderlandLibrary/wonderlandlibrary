package org.apache.commons.lang3;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.lang3.mutable.MutableObject;

public class ClassUtils {
   public static final char PACKAGE_SEPARATOR_CHAR = '.';
   public static final String PACKAGE_SEPARATOR = String.valueOf('.');
   public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
   public static final String INNER_CLASS_SEPARATOR = String.valueOf('$');
   private static final Map primitiveWrapperMap = new HashMap();
   private static final Map wrapperPrimitiveMap;
   private static final Map abbreviationMap;
   private static final Map reverseAbbreviationMap;

   public static String getShortClassName(Object var0, String var1) {
      return var0 == null ? var1 : getShortClassName(var0.getClass());
   }

   public static String getShortClassName(Class var0) {
      return var0 == null ? "" : getShortClassName(var0.getName());
   }

   public static String getShortClassName(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return "";
      } else {
         StringBuilder var1 = new StringBuilder();
         if (var0.startsWith("[")) {
            while(var0.charAt(0) == '[') {
               var0 = var0.substring(1);
               var1.append("[]");
            }

            if (var0.charAt(0) == 'L' && var0.charAt(var0.length() - 1) == ';') {
               var0 = var0.substring(1, var0.length() - 1);
            }

            if (reverseAbbreviationMap.containsKey(var0)) {
               var0 = (String)reverseAbbreviationMap.get(var0);
            }
         }

         int var2 = var0.lastIndexOf(46);
         int var3 = var0.indexOf(36, var2 == -1 ? 0 : var2 + 1);
         String var4 = var0.substring(var2 + 1);
         if (var3 != -1) {
            var4 = var4.replace('$', '.');
         }

         return var4 + var1;
      }
   }

   public static String getSimpleName(Class var0) {
      return var0 == null ? "" : var0.getSimpleName();
   }

   public static String getSimpleName(Object var0, String var1) {
      return var0 == null ? var1 : getSimpleName(var0.getClass());
   }

   public static String getPackageName(Object var0, String var1) {
      return var0 == null ? var1 : getPackageName(var0.getClass());
   }

   public static String getPackageName(Class var0) {
      return var0 == null ? "" : getPackageName(var0.getName());
   }

   public static String getPackageName(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return "";
      } else {
         while(var0.charAt(0) == '[') {
            var0 = var0.substring(1);
         }

         if (var0.charAt(0) == 'L' && var0.charAt(var0.length() - 1) == ';') {
            var0 = var0.substring(1);
         }

         int var1 = var0.lastIndexOf(46);
         return var1 == -1 ? "" : var0.substring(0, var1);
      }
   }

   public static List getAllSuperclasses(Class var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList();

         for(Class var2 = var0.getSuperclass(); var2 != null; var2 = var2.getSuperclass()) {
            var1.add(var2);
         }

         return var1;
      }
   }

   public static List getAllInterfaces(Class var0) {
      if (var0 == null) {
         return null;
      } else {
         LinkedHashSet var1 = new LinkedHashSet();
         getAllInterfaces(var0, var1);
         return new ArrayList(var1);
      }
   }

   private static void getAllInterfaces(Class var0, HashSet var1) {
      while(var0 != null) {
         Class[] var2 = var0.getInterfaces();
         Class[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Class var6 = var3[var5];
            if (var1.add(var6)) {
               getAllInterfaces(var6, var1);
            }
         }

         var0 = var0.getSuperclass();
      }

   }

   public static List convertClassNamesToClasses(List var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList(var0.size());
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();

            try {
               var1.add(Class.forName(var3));
            } catch (Exception var5) {
               var1.add((Object)null);
            }
         }

         return var1;
      }
   }

   public static List convertClassesToClassNames(List var0) {
      if (var0 == null) {
         return null;
      } else {
         ArrayList var1 = new ArrayList(var0.size());
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            Class var3 = (Class)var2.next();
            if (var3 == null) {
               var1.add((Object)null);
            } else {
               var1.add(var3.getName());
            }
         }

         return var1;
      }
   }

   public static boolean isAssignable(Class[] var0, Class... var1) {
      return isAssignable(var0, var1, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
   }

   public static boolean isAssignable(Class[] var0, Class[] var1, boolean var2) {
      if (!ArrayUtils.isSameLength((Object[])var0, (Object[])var1)) {
         return false;
      } else {
         if (var0 == null) {
            var0 = ArrayUtils.EMPTY_CLASS_ARRAY;
         }

         if (var1 == null) {
            var1 = ArrayUtils.EMPTY_CLASS_ARRAY;
         }

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Class var10000 = var0[var3];
            Class var10001 = var1[var3];
            if (var2 == null) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isPrimitiveOrWrapper(Class var0) {
      if (var0 == null) {
         return false;
      } else {
         return var0.isPrimitive() || isPrimitiveWrapper(var0);
      }
   }

   public static boolean isPrimitiveWrapper(Class var0) {
      return wrapperPrimitiveMap.containsKey(var0);
   }

   public static boolean isAssignable(Class var0, Class var1) {
      return isAssignable(var0, var1, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
   }

   public static Class primitiveToWrapper(Class var0) {
      Class var1 = var0;
      if (var0 != null && var0.isPrimitive()) {
         var1 = (Class)primitiveWrapperMap.get(var0);
      }

      return var1;
   }

   public static Class[] primitivesToWrappers(Class... var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return var0;
      } else {
         Class[] var1 = new Class[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = primitiveToWrapper(var0[var2]);
         }

         return var1;
      }
   }

   public static Class wrapperToPrimitive(Class var0) {
      return (Class)wrapperPrimitiveMap.get(var0);
   }

   public static Class[] wrappersToPrimitives(Class... var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return var0;
      } else {
         Class[] var1 = new Class[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = wrapperToPrimitive(var0[var2]);
         }

         return var1;
      }
   }

   public static boolean isInnerClass(Class var0) {
      return var0 != null && var0.getEnclosingClass() != null;
   }

   public static Class getClass(ClassLoader var0, String var1, boolean var2) throws ClassNotFoundException {
      try {
         Class var3;
         if (abbreviationMap.containsKey(var1)) {
            String var8 = "[" + (String)abbreviationMap.get(var1);
            var3 = Class.forName(var8, var2, var0).getComponentType();
         } else {
            var3 = Class.forName(toCanonicalName(var1), var2, var0);
         }

         return var3;
      } catch (ClassNotFoundException var7) {
         int var4 = var1.lastIndexOf(46);
         if (var4 != -1) {
            try {
               return getClass(var0, var1.substring(0, var4) + '$' + var1.substring(var4 + 1), var2);
            } catch (ClassNotFoundException var6) {
            }
         }

         throw var7;
      }
   }

   public static Class getClass(ClassLoader var0, String var1) throws ClassNotFoundException {
      return getClass(var0, var1, true);
   }

   public static Class getClass(String var0) throws ClassNotFoundException {
      return getClass(var0, true);
   }

   public static Class getClass(String var0, boolean var1) throws ClassNotFoundException {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      ClassLoader var3 = var2 == null ? ClassUtils.class.getClassLoader() : var2;
      return getClass(var3, var0, var1);
   }

   public static Method getPublicMethod(Class var0, String var1, Class... var2) throws SecurityException, NoSuchMethodException {
      Method var3 = var0.getMethod(var1, var2);
      if (Modifier.isPublic(var3.getDeclaringClass().getModifiers())) {
         return var3;
      } else {
         ArrayList var4 = new ArrayList();
         var4.addAll(getAllInterfaces(var0));
         var4.addAll(getAllSuperclasses(var0));
         Iterator var5 = var4.iterator();

         while(true) {
            Class var6;
            do {
               if (!var5.hasNext()) {
                  throw new NoSuchMethodException("Can't find a public method for " + var1 + " " + ArrayUtils.toString(var2));
               }

               var6 = (Class)var5.next();
            } while(!Modifier.isPublic(var6.getModifiers()));

            Method var7;
            try {
               var7 = var6.getMethod(var1, var2);
            } catch (NoSuchMethodException var9) {
               continue;
            }

            if (Modifier.isPublic(var7.getDeclaringClass().getModifiers())) {
               return var7;
            }
         }
      }
   }

   private static String toCanonicalName(String var0) {
      var0 = StringUtils.deleteWhitespace(var0);
      if (var0 == null) {
         throw new NullPointerException("className must not be null.");
      } else {
         if (var0.endsWith("[]")) {
            StringBuilder var1 = new StringBuilder();

            while(var0.endsWith("[]")) {
               var0 = var0.substring(0, var0.length() - 2);
               var1.append("[");
            }

            String var2 = (String)abbreviationMap.get(var0);
            if (var2 != null) {
               var1.append(var2);
            } else {
               var1.append("L").append(var0).append(";");
            }

            var0 = var1.toString();
         }

         return var0;
      }
   }

   public static Class[] toClass(Object... var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return ArrayUtils.EMPTY_CLASS_ARRAY;
      } else {
         Class[] var1 = new Class[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2] == null ? null : var0[var2].getClass();
         }

         return var1;
      }
   }

   public static String getShortCanonicalName(Object var0, String var1) {
      return var0 == null ? var1 : getShortCanonicalName(var0.getClass().getName());
   }

   public static String getShortCanonicalName(Class var0) {
      return var0 == null ? "" : getShortCanonicalName(var0.getName());
   }

   public static String getShortCanonicalName(String var0) {
      return getShortClassName(getCanonicalName(var0));
   }

   public static String getPackageCanonicalName(Object var0, String var1) {
      return var0 == null ? var1 : getPackageCanonicalName(var0.getClass().getName());
   }

   public static String getPackageCanonicalName(Class var0) {
      return var0 == null ? "" : getPackageCanonicalName(var0.getName());
   }

   public static String getPackageCanonicalName(String var0) {
      return getPackageName(getCanonicalName(var0));
   }

   private static String getCanonicalName(String var0) {
      var0 = StringUtils.deleteWhitespace(var0);
      if (var0 == null) {
         return null;
      } else {
         int var1;
         for(var1 = 0; var0.startsWith("["); var0 = var0.substring(1)) {
            ++var1;
         }

         if (var1 < 1) {
            return var0;
         } else {
            if (var0.startsWith("L")) {
               var0 = var0.substring(1, var0.endsWith(";") ? var0.length() - 1 : var0.length());
            } else if (var0.length() > 0) {
               var0 = (String)reverseAbbreviationMap.get(var0.substring(0, 1));
            }

            StringBuilder var2 = new StringBuilder(var0);

            for(int var3 = 0; var3 < var1; ++var3) {
               var2.append("[]");
            }

            return var2.toString();
         }
      }
   }

   public static Iterable hierarchy(Class var0) {
      return hierarchy(var0, ClassUtils.Interfaces.EXCLUDE);
   }

   public static Iterable hierarchy(Class var0, ClassUtils.Interfaces var1) {
      Iterable var2 = new Iterable(var0) {
         final Class val$type;

         {
            this.val$type = var1;
         }

         public Iterator iterator() {
            MutableObject var1 = new MutableObject(this.val$type);
            return new Iterator(this, var1) {
               final MutableObject val$next;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.val$next = var2;
               }

               public boolean hasNext() {
                  return this.val$next.getValue() != null;
               }

               public Class next() {
                  Class var1 = (Class)this.val$next.getValue();
                  this.val$next.setValue(var1.getSuperclass());
                  return var1;
               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }

               public Object next() {
                  return this.next();
               }
            };
         }
      };
      return var1 != ClassUtils.Interfaces.INCLUDE ? var2 : new Iterable(var2) {
         final Iterable val$classes;

         {
            this.val$classes = var1;
         }

         public Iterator iterator() {
            HashSet var1 = new HashSet();
            Iterator var2 = this.val$classes.iterator();
            return new Iterator(this, var2, var1) {
               Iterator interfaces;
               final Iterator val$wrapped;
               final Set val$seenInterfaces;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.val$wrapped = var2;
                  this.val$seenInterfaces = var3;
                  this.interfaces = Collections.emptySet().iterator();
               }

               public boolean hasNext() {
                  return this.interfaces.hasNext() || this.val$wrapped.hasNext();
               }

               public Class next() {
                  Class var1;
                  if (this.interfaces.hasNext()) {
                     var1 = (Class)this.interfaces.next();
                     this.val$seenInterfaces.add(var1);
                     return var1;
                  } else {
                     var1 = (Class)this.val$wrapped.next();
                     LinkedHashSet var2 = new LinkedHashSet();
                     this.walkInterfaces(var2, var1);
                     this.interfaces = var2.iterator();
                     return var1;
                  }
               }

               private void walkInterfaces(Set var1, Class var2) {
                  Class[] var3 = var2.getInterfaces();
                  int var4 = var3.length;

                  for(int var5 = 0; var5 < var4; ++var5) {
                     Class var6 = var3[var5];
                     if (!this.val$seenInterfaces.contains(var6)) {
                        var1.add(var6);
                     }

                     this.walkInterfaces(var1, var6);
                  }

               }

               public void remove() {
                  throw new UnsupportedOperationException();
               }

               public Object next() {
                  return this.next();
               }
            };
         }
      };
   }

   static {
      primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
      primitiveWrapperMap.put(Byte.TYPE, Byte.class);
      primitiveWrapperMap.put(Character.TYPE, Character.class);
      primitiveWrapperMap.put(Short.TYPE, Short.class);
      primitiveWrapperMap.put(Integer.TYPE, Integer.class);
      primitiveWrapperMap.put(Long.TYPE, Long.class);
      primitiveWrapperMap.put(Double.TYPE, Double.class);
      primitiveWrapperMap.put(Float.TYPE, Float.class);
      primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
      wrapperPrimitiveMap = new HashMap();
      Iterator var0 = primitiveWrapperMap.keySet().iterator();

      while(var0.hasNext()) {
         Class var1 = (Class)var0.next();
         Class var2 = (Class)primitiveWrapperMap.get(var1);
         if (!var1.equals(var2)) {
            wrapperPrimitiveMap.put(var2, var1);
         }
      }

      HashMap var4 = new HashMap();
      var4.put("int", "I");
      var4.put("boolean", "Z");
      var4.put("float", "F");
      var4.put("long", "J");
      var4.put("short", "S");
      var4.put("byte", "B");
      var4.put("double", "D");
      var4.put("char", "C");
      var4.put("void", "V");
      HashMap var5 = new HashMap();
      Iterator var6 = var4.entrySet().iterator();

      while(var6.hasNext()) {
         Entry var3 = (Entry)var6.next();
         var5.put(var3.getValue(), var3.getKey());
      }

      abbreviationMap = Collections.unmodifiableMap(var4);
      reverseAbbreviationMap = Collections.unmodifiableMap(var5);
   }

   public static enum Interfaces {
      INCLUDE,
      EXCLUDE;

      private static final ClassUtils.Interfaces[] $VALUES = new ClassUtils.Interfaces[]{INCLUDE, EXCLUDE};
   }
}
