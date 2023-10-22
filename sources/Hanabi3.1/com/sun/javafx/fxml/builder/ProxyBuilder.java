package com.sun.javafx.fxml.builder;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.beans.NamedArg;
import javafx.util.Builder;
import sun.reflect.misc.ConstructorUtil;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

public class ProxyBuilder extends AbstractMap implements Builder {
   private Class type;
   private final Map constructorsMap;
   private final Map propertiesMap;
   private final Set constructors;
   private Set propertyNames;
   private boolean hasDefaultConstructor = false;
   private Constructor defaultConstructor;
   private static final String SETTER_PREFIX = "set";
   private static final String GETTER_PREFIX = "get";
   private final Comparator constructorComparator = (var0, var1x) -> {
      int var2 = var0.getParameterCount();
      int var3 = var1x.getParameterCount();
      int var4 = Math.min(var2, var3);

      for(int var5 = 0; var5 < var4; ++var5) {
         Class var6 = var0.getParameterTypes()[var5];
         Class var7 = var1x.getParameterTypes()[var5];
         if (!var6.equals(var7)) {
            if (var6.equals(Integer.TYPE) && var7.equals(Double.TYPE)) {
               return -1;
            }

            if (var6.equals(Double.TYPE) && var7.equals(Integer.TYPE)) {
               return 1;
            }

            return var6.getCanonicalName().compareTo(var7.getCanonicalName());
         }
      }

      return var2 - var3;
   };
   private final Map userValues = new HashMap();
   private final Map containers = new HashMap();
   private static final Map defaultsMap = new HashMap();

   public ProxyBuilder(Class var1) {
      this.type = var1;
      this.constructorsMap = new HashMap();
      Constructor[] var2 = ConstructorUtil.getConstructors(this.type);
      Constructor[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Constructor var6 = var3[var5];
         Class[] var8 = var6.getParameterTypes();
         Annotation[][] var9 = var6.getParameterAnnotations();
         if (var8.length == 0) {
            this.hasDefaultConstructor = true;
            this.defaultConstructor = var6;
         } else {
            int var10 = 0;
            boolean var11 = true;
            LinkedHashMap var7 = new LinkedHashMap();
            Class[] var12 = var8;
            int var13 = var8.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               Class var15 = var12[var14];
               NamedArg var16 = null;
               Annotation[] var17 = var9[var10];
               int var18 = var17.length;

               for(int var19 = 0; var19 < var18; ++var19) {
                  Annotation var20 = var17[var19];
                  if (var20 instanceof NamedArg) {
                     var16 = (NamedArg)var20;
                     break;
                  }
               }

               if (var16 == null) {
                  var11 = false;
                  break;
               }

               AnnotationValue var21 = new AnnotationValue(var16.value(), var16.defaultValue(), var15);
               var7.put(var16.value(), var21);
               ++var10;
            }

            if (var11) {
               this.constructorsMap.put(var6, var7);
            }
         }
      }

      if (!this.hasDefaultConstructor && this.constructorsMap.isEmpty()) {
         throw new RuntimeException("Cannot create instance of " + this.type.getCanonicalName() + " the constructor is not properly annotated.");
      } else {
         this.constructors = new TreeSet(this.constructorComparator);
         this.constructors.addAll(this.constructorsMap.keySet());
         this.propertiesMap = this.scanForSetters();
      }
   }

   public Object put(String var1, Object var2) {
      this.userValues.put(var1, var2);
      return null;
   }

   private Object getTemporaryContainer(String var1) {
      Object var2 = this.containers.get(var1);
      if (var2 == null) {
         var2 = this.getReadOnlyProperty(var1);
         if (var2 != null) {
            this.containers.put(var1, var2);
         }
      }

      return var2;
   }

   private Object getReadOnlyProperty(String var1) {
      return new ArrayListWrapper();
   }

   public int size() {
      throw new UnsupportedOperationException();
   }

   public Set entrySet() {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      throw new UnsupportedOperationException();
   }

   public boolean containsKey(Object var1) {
      return this.getTemporaryContainer(var1.toString()) != null;
   }

   public boolean containsValue(Object var1) {
      throw new UnsupportedOperationException();
   }

   public Object get(Object var1) {
      return this.getTemporaryContainer(var1.toString());
   }

   public Object build() {
      Object var1 = null;
      Iterator var2 = this.containers.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         this.put((String)var3.getKey(), var3.getValue());
      }

      this.propertyNames = this.userValues.keySet();
      var2 = this.constructors.iterator();

      Set var4;
      while(var2.hasNext()) {
         Constructor var8 = (Constructor)var2.next();
         var4 = this.getArgumentNames(var8);
         if (this.propertyNames.equals(var4)) {
            var1 = this.createObjectWithExactArguments(var8, var4);
            if (var1 != null) {
               return var1;
            }
         }
      }

      Set var7 = this.propertiesMap.keySet();
      if (var7.containsAll(this.propertyNames) && this.hasDefaultConstructor) {
         var1 = this.createObjectFromDefaultConstructor();
         if (var1 != null) {
            return var1;
         }
      }

      HashSet var9 = new HashSet(this.propertyNames);
      var9.retainAll(var7);
      var4 = this.chooseBestConstructors(var7);
      Iterator var5 = var4.iterator();

      do {
         if (!var5.hasNext()) {
            if (var1 == null) {
               throw new RuntimeException("Cannot create instance of " + this.type.getCanonicalName() + " with given set of properties: " + this.userValues.keySet().toString());
            }

            return var1;
         }

         Constructor var6 = (Constructor)var5.next();
         var1 = this.createObjectFromConstructor(var6, var9);
      } while(var1 == null);

      return var1;
   }

   private Set chooseBestConstructors(Set var1) {
      HashSet var2 = new HashSet(this.propertyNames);
      var2.removeAll(var1);
      HashSet var3 = new HashSet(this.propertyNames);
      var3.retainAll(var1);
      int var4 = Integer.MAX_VALUE;
      int var5 = Integer.MAX_VALUE;
      TreeSet var6 = new TreeSet(this.constructorComparator);
      Object var7 = null;
      Iterator var8 = this.constructors.iterator();

      while(true) {
         Constructor var9;
         HashSet var12;
         int var13;
         do {
            Set var10;
            do {
               if (!var8.hasNext()) {
                  if (var7 != null && !((Set)var7).isEmpty()) {
                     throw new RuntimeException("Cannot create instance of " + this.type.getCanonicalName() + " no constructor contains all properties specified in FXML.");
                  }

                  return var6;
               }

               var9 = (Constructor)var8.next();
               var10 = this.getArgumentNames(var9);
            } while(!var10.containsAll(var2));

            HashSet var11 = new HashSet(var10);
            var11.removeAll(this.propertyNames);
            var12 = new HashSet(var3);
            var12.removeAll(var10);
            var13 = var11.size();
            if (var4 == var13 && var5 == var12.size()) {
               var6.add(var9);
            }
         } while(var4 <= var13 && (var4 != var13 || var5 <= var12.size()));

         var4 = var13;
         var5 = var12.size();
         var6.clear();
         var6.add(var9);
      }
   }

   private Set getArgumentNames(Constructor var1) {
      Map var2 = (Map)this.constructorsMap.get(var1);
      Set var3 = null;
      if (var2 != null) {
         var3 = var2.keySet();
      }

      return var3;
   }

   private Object createObjectFromDefaultConstructor() throws RuntimeException {
      Object var1 = null;

      try {
         var1 = this.createInstance(this.defaultConstructor, new Object[0]);
      } catch (Exception var6) {
         throw new RuntimeException(var6);
      }

      Iterator var2 = this.propertyNames.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();

         try {
            Property var4 = (Property)this.propertiesMap.get(var3);
            var4.invoke(var1, this.getUserValue(var3, var4.getType()));
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }
      }

      return var1;
   }

   private Object createObjectFromConstructor(Constructor var1, Set var2) {
      Object var3 = null;
      Map var4 = (Map)this.constructorsMap.get(var1);
      Object[] var5 = new Object[var4.size()];
      int var6 = 0;
      HashSet var7 = new HashSet(var2);

      Iterator var8;
      for(var8 = var4.values().iterator(); var8.hasNext(); ++var6) {
         AnnotationValue var9 = (AnnotationValue)var8.next();
         Object var10 = this.getUserValue(var9.getName(), var9.getType());
         if (var10 != null) {
            try {
               var5[var6] = BeanAdapter.coerce(var10, var9.getType());
            } catch (Exception var15) {
               return null;
            }
         } else if (!var9.getDefaultValue().isEmpty()) {
            try {
               var5[var6] = BeanAdapter.coerce(var9.getDefaultValue(), var9.getType());
            } catch (Exception var14) {
               return null;
            }
         } else {
            var5[var6] = getDefaultValue(var9.getType());
         }

         var7.remove(var9.getName());
      }

      try {
         var3 = this.createInstance(var1, var5);
      } catch (Exception var13) {
      }

      if (var3 != null) {
         var8 = var7.iterator();

         while(var8.hasNext()) {
            String var17 = (String)var8.next();

            try {
               Property var16 = (Property)this.propertiesMap.get(var17);
               var16.invoke(var3, this.getUserValue(var17, var16.getType()));
            } catch (Exception var12) {
               return null;
            }
         }
      }

      return var3;
   }

   private Object getUserValue(String var1, Class var2) {
      Object var3 = this.userValues.get(var1);
      if (var3 == null) {
         return null;
      } else if (var2.isAssignableFrom(var3.getClass())) {
         return var3;
      } else {
         if (var2.isArray()) {
            try {
               return convertListToArray(var3, var2);
            } catch (RuntimeException var5) {
            }
         }

         if (ArrayListWrapper.class.equals(var3.getClass())) {
            List var4 = (List)var3;
            return var4.get(0);
         } else {
            return var3;
         }
      }
   }

   private Object createObjectWithExactArguments(Constructor var1, Set var2) {
      Object var3 = null;
      Object[] var4 = new Object[var2.size()];
      Map var5 = (Map)this.constructorsMap.get(var1);
      int var6 = 0;
      Iterator var7 = var2.iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         Class var9 = ((AnnotationValue)var5.get(var8)).getType();
         Object var10 = this.getUserValue(var8, var9);

         try {
            var4[var6++] = BeanAdapter.coerce(var10, var9);
         } catch (Exception var13) {
            return null;
         }
      }

      try {
         var3 = this.createInstance(var1, var4);
      } catch (Exception var12) {
      }

      return var3;
   }

   private Object createInstance(Constructor var1, Object[] var2) throws Exception {
      Object var3 = null;
      ReflectUtil.checkPackageAccess(this.type);
      var3 = var1.newInstance(var2);
      return var3;
   }

   private Map scanForSetters() {
      HashMap var1 = new HashMap();
      HashMap var2 = getClassMethodCache(this.type);
      Iterator var3 = var2.keySet().iterator();

      while(true) {
         String var4;
         String var5;
         List var6;
         Iterator var7;
         Method var8;
         Class var9;
         Class[] var10;
         do {
            if (!var3.hasNext()) {
               return var1;
            }

            var4 = (String)var3.next();
            if (var4.startsWith("set")) {
               var5 = var4.substring("set".length());
               var5 = Character.toLowerCase(var5.charAt(0)) + var5.substring(1);
               var6 = (List)var2.get(var4);
               var7 = var6.iterator();

               while(var7.hasNext()) {
                  var8 = (Method)var7.next();
                  var9 = var8.getReturnType();
                  var10 = var8.getParameterTypes();
                  if (var9.equals(Void.TYPE) && var10.length == 1) {
                     var1.put(var5, new Setter(var8, var10[0]));
                  }
               }
            }
         } while(!var4.startsWith("get"));

         var5 = var4.substring("set".length());
         var5 = Character.toLowerCase(var5.charAt(0)) + var5.substring(1);
         var6 = (List)var2.get(var4);
         var7 = var6.iterator();

         while(var7.hasNext()) {
            var8 = (Method)var7.next();
            var9 = var8.getReturnType();
            var10 = var8.getParameterTypes();
            if (Collection.class.isAssignableFrom(var9) && var10.length == 0) {
               var1.put(var5, new Getter(var8, var9));
            }
         }
      }
   }

   private static HashMap getClassMethodCache(Class var0) {
      HashMap var1 = new HashMap();
      ReflectUtil.checkPackageAccess(var0);
      Method[] var2 = var0.getMethods();
      Method[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Method var6 = var3[var5];
         int var7 = var6.getModifiers();
         if (Modifier.isPublic(var7) && !Modifier.isStatic(var7)) {
            String var8 = var6.getName();
            LinkedList var9 = (LinkedList)var1.get(var8);
            if (var9 == null) {
               var9 = new LinkedList();
               var1.put(var8, var9);
            }

            var9.add(var6);
         }
      }

      return var1;
   }

   private static Object[] convertListToArray(Object var0, Class var1) {
      Class var2 = var1.getComponentType();
      List var3 = (List)BeanAdapter.coerce(var0, List.class);
      return var3.toArray((Object[])((Object[])Array.newInstance(var2, 0)));
   }

   private static Object getDefaultValue(Class var0) {
      return defaultsMap.get(var0);
   }

   static {
      defaultsMap.put(Byte.TYPE, (byte)0);
      defaultsMap.put(Short.TYPE, Short.valueOf((short)0));
      defaultsMap.put(Integer.TYPE, 0);
      defaultsMap.put(Long.TYPE, 0L);
      defaultsMap.put(Integer.TYPE, 0);
      defaultsMap.put(Float.TYPE, 0.0F);
      defaultsMap.put(Double.TYPE, 0.0);
      defaultsMap.put(Character.TYPE, '\u0000');
      defaultsMap.put(Boolean.TYPE, false);
      defaultsMap.put(Object.class, (Object)null);
   }

   private static class AnnotationValue {
      private final String name;
      private final String defaultValue;
      private final Class type;

      public AnnotationValue(String var1, String var2, Class var3) {
         this.name = var1;
         this.defaultValue = var2;
         this.type = var3;
      }

      public String getName() {
         return this.name;
      }

      public String getDefaultValue() {
         return this.defaultValue;
      }

      public Class getType() {
         return this.type;
      }
   }

   private static class Getter extends Property {
      public Getter(Method var1, Class var2) {
         super(var1, var2);
      }

      public void invoke(Object var1, Object var2) throws Exception {
         Collection var3 = (Collection)MethodUtil.invoke(this.method, var1, new Object[0]);
         if (var2 instanceof Collection) {
            Collection var4 = (Collection)var2;
            var3.addAll(var4);
         } else {
            var3.add(var2);
         }

      }
   }

   private static class Setter extends Property {
      public Setter(Method var1, Class var2) {
         super(var1, var2);
      }

      public void invoke(Object var1, Object var2) throws Exception {
         Object[] var3 = new Object[]{BeanAdapter.coerce(var2, this.type)};
         MethodUtil.invoke(this.method, var1, var3);
      }
   }

   private abstract static class Property {
      protected final Method method;
      protected final Class type;

      public Property(Method var1, Class var2) {
         this.method = var1;
         this.type = var2;
      }

      public Class getType() {
         return this.type;
      }

      public abstract void invoke(Object var1, Object var2) throws Exception;
   }

   private static class ArrayListWrapper extends ArrayList {
      private ArrayListWrapper() {
      }

      // $FF: synthetic method
      ArrayListWrapper(Object var1) {
         this();
      }
   }
}
