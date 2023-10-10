package org.reflections.adapters;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import org.reflections.ReflectionUtils;
import org.reflections.util.Utils;
import org.reflections.vfs.Vfs;

public class JavaReflectionAdapter implements MetadataAdapter {
   public List getFields(Class var1) {
      return Lists.newArrayList((Object[])var1.getDeclaredFields());
   }

   public List getMethods(Class var1) {
      ArrayList var2 = Lists.newArrayList();
      var2.addAll(Arrays.asList(var1.getDeclaredMethods()));
      var2.addAll(Arrays.asList(var1.getDeclaredConstructors()));
      return var2;
   }

   public String getMethodName(Member var1) {
      return var1 instanceof Method ? var1.getName() : (var1 instanceof Constructor ? "<init>" : null);
   }

   public List getParameterNames(Member var1) {
      ArrayList var2 = Lists.newArrayList();
      Class[] var3 = var1 instanceof Method ? ((Method)var1).getParameterTypes() : (var1 instanceof Constructor ? ((Constructor)var1).getParameterTypes() : null);
      if (var3 != null) {
         Class[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class var7 = var4[var6];
            String var8 = getName(var7);
            var2.add(var8);
         }
      }

      return var2;
   }

   public List getClassAnnotationNames(Class var1) {
      return this.getAnnotationNames(var1.getDeclaredAnnotations());
   }

   public List getFieldAnnotationNames(Field var1) {
      return this.getAnnotationNames(var1.getDeclaredAnnotations());
   }

   public List getMethodAnnotationNames(Member var1) {
      Annotation[] var2 = var1 instanceof Method ? ((Method)var1).getDeclaredAnnotations() : (var1 instanceof Constructor ? ((Constructor)var1).getDeclaredAnnotations() : null);
      return this.getAnnotationNames(var2);
   }

   public List getParameterAnnotationNames(Member var1, int var2) {
      Annotation[][] var3 = var1 instanceof Method ? ((Method)var1).getParameterAnnotations() : (var1 instanceof Constructor ? ((Constructor)var1).getParameterAnnotations() : (Annotation[][])null);
      return this.getAnnotationNames(var3 != null ? var3[var2] : null);
   }

   public String getReturnTypeName(Member var1) {
      return ((Method)var1).getReturnType().getName();
   }

   public String getFieldName(Field var1) {
      return var1.getName();
   }

   public Class getOfCreateClassObject(Vfs.File var1) throws Exception {
      return this.getOfCreateClassObject(var1, (ClassLoader[])null);
   }

   public Class getOfCreateClassObject(Vfs.File var1, @Nullable ClassLoader... var2) throws Exception {
      String var3 = var1.getRelativePath().replace("/", ".").replace(".class", "");
      return ReflectionUtils.forName(var3, var2);
   }

   public String getMethodModifier(Member var1) {
      return Modifier.toString(var1.getModifiers());
   }

   public String getMethodKey(Class var1, Member var2) {
      return this.getMethodName(var2) + "(" + Joiner.on(", ").join((Iterable)this.getParameterNames(var2)) + ")";
   }

   public String getMethodFullKey(Class var1, Member var2) {
      return this.getClassName(var1) + "." + this.getMethodKey(var1, var2);
   }

   public boolean isPublic(Object var1) {
      Integer var2 = var1 instanceof Class ? ((Class)var1).getModifiers() : var1 instanceof Member ? ((Member)var1).getModifiers() : null;
      return var2 != null && Modifier.isPublic(var2);
   }

   public String getClassName(Class var1) {
      return var1.getName();
   }

   public String getSuperclassName(Class var1) {
      Class var2 = var1.getSuperclass();
      return var2 != null ? var2.getName() : "";
   }

   public List getInterfacesNames(Class var1) {
      Class[] var2 = var1.getInterfaces();
      ArrayList var3 = new ArrayList(var2 != null ? var2.length : 0);
      if (var2 != null) {
         Class[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Class var7 = var4[var6];
            var3.add(var7.getName());
         }
      }

      return var3;
   }

   public boolean acceptsInput(String var1) {
      return var1.endsWith(".class");
   }

   private List getAnnotationNames(Annotation[] var1) {
      ArrayList var2 = new ArrayList(var1.length);
      Annotation[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Annotation var6 = var3[var5];
         var2.add(var6.annotationType().getName());
      }

      return var2;
   }

   public static String getName(Class var0) {
      if (var0.isArray()) {
         try {
            Class var1 = var0;

            int var2;
            for(var2 = 0; var1.isArray(); var1 = var1.getComponentType()) {
               ++var2;
            }

            return var1.getName() + Utils.repeat("[]", var2);
         } catch (Throwable var3) {
         }
      }

      return var0.getName();
   }

   public String getMethodFullKey(Object var1, Object var2) {
      return this.getMethodFullKey((Class)var1, (Member)var2);
   }

   public String getMethodKey(Object var1, Object var2) {
      return this.getMethodKey((Class)var1, (Member)var2);
   }

   public String getMethodModifier(Object var1) {
      return this.getMethodModifier((Member)var1);
   }

   public Object getOfCreateClassObject(Vfs.File var1) throws Exception {
      return this.getOfCreateClassObject(var1);
   }

   public String getFieldName(Object var1) {
      return this.getFieldName((Field)var1);
   }

   public String getReturnTypeName(Object var1) {
      return this.getReturnTypeName((Member)var1);
   }

   public List getParameterAnnotationNames(Object var1, int var2) {
      return this.getParameterAnnotationNames((Member)var1, var2);
   }

   public List getMethodAnnotationNames(Object var1) {
      return this.getMethodAnnotationNames((Member)var1);
   }

   public List getFieldAnnotationNames(Object var1) {
      return this.getFieldAnnotationNames((Field)var1);
   }

   public List getClassAnnotationNames(Object var1) {
      return this.getClassAnnotationNames((Class)var1);
   }

   public List getParameterNames(Object var1) {
      return this.getParameterNames((Member)var1);
   }

   public String getMethodName(Object var1) {
      return this.getMethodName((Member)var1);
   }

   public List getMethods(Object var1) {
      return this.getMethods((Class)var1);
   }

   public List getFields(Object var1) {
      return this.getFields((Class)var1);
   }

   public List getInterfacesNames(Object var1) {
      return this.getInterfacesNames((Class)var1);
   }

   public String getSuperclassName(Object var1) {
      return this.getSuperclassName((Class)var1);
   }

   public String getClassName(Object var1) {
      return this.getClassName((Class)var1);
   }
}
