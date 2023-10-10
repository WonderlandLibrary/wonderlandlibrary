package org.jboss.errai.reflections.adapters;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import org.jboss.errai.reflections.ReflectionsException;

public class JavassistAdapter implements MetadataAdapter {
   public List getFields(ClassFile var1) {
      return var1.getFields();
   }

   public List getMethods(ClassFile var1) {
      return var1.getMethods();
   }

   public String getMethodName(MethodInfo var1) {
      return var1.getName();
   }

   public List getParameterNames(MethodInfo var1) {
      String var2 = var1.getDescriptor();
      var2 = var2.substring(var2.indexOf("(") + 1, var2.lastIndexOf(")"));
      return this.splitDescriptorToTypeNames(var2);
   }

   public List getClassAnnotationNames(ClassFile var1) {
      AnnotationsAttribute var2 = (AnnotationsAttribute)var1.getAttribute("RuntimeVisibleAnnotations");
      return this.getAnnotationNames(var2);
   }

   public List getFieldAnnotationNames(FieldInfo var1) {
      AnnotationsAttribute var2 = (AnnotationsAttribute)var1.getAttribute("RuntimeVisibleAnnotations");
      return this.getAnnotationNames(var2);
   }

   public List getMethodAnnotationNames(MethodInfo var1) {
      AnnotationsAttribute var2 = (AnnotationsAttribute)var1.getAttribute("RuntimeVisibleAnnotations");
      return this.getAnnotationNames(var2);
   }

   public List getParameterAnnotationNames(MethodInfo var1, int var2) {
      ParameterAnnotationsAttribute var3 = (ParameterAnnotationsAttribute)var1.getAttribute("RuntimeVisibleParameterAnnotations");
      if (var3 != null) {
         Annotation[][] var4 = var3.getAnnotations();
         if (var2 < var4.length) {
            Annotation[] var5 = var4[var2];
            return this.getAnnotationNames(var5);
         }
      }

      return new ArrayList();
   }

   public String getReturnTypeName(MethodInfo var1) {
      String var2 = var1.getDescriptor();
      var2 = var2.substring(var2.lastIndexOf(")") + 1);
      return (String)this.splitDescriptorToTypeNames(var2).get(0);
   }

   public String getFieldName(FieldInfo var1) {
      return var1.getName();
   }

   public ClassFile createClassObject(InputStream var1) throws IOException {
      DataInputStream var2 = null;
      var2 = new DataInputStream(new BufferedInputStream(var1));
      ClassFile var3 = new ClassFile(var2);
      if (var2 != null) {
         try {
            var2.close();
         } catch (IOException var7) {
            throw new ReflectionsException("could not close DataInputStream", var7);
         }
      }

      return var3;
   }

   public String getMethodModifier(MethodInfo var1) {
      int var2 = var1.getAccessFlags();
      return AccessFlag.isPrivate(var2) ? "private" : (AccessFlag.isProtected(var2) ? "protected" : (AccessFlag.isPublic(var2) ? "public" : ""));
   }

   public String getMethodKey(ClassFile var1, MethodInfo var2) {
      return this.getMethodName(var2) + "(" + Joiner.on(", ").join((Iterable)this.getParameterNames(var2)) + ")";
   }

   public String getMethodFullKey(ClassFile var1, MethodInfo var2) {
      return this.getClassName(var1) + "." + this.getMethodKey(var1, var2);
   }

   public String getClassName(ClassFile var1) {
      return var1.getName();
   }

   public String getSuperclassName(ClassFile var1) {
      return var1.getSuperclass();
   }

   public List getInterfacesNames(ClassFile var1) {
      return Arrays.asList(var1.getInterfaces());
   }

   private List getAnnotationNames(AnnotationsAttribute var1) {
      if (var1 == null) {
         return new ArrayList(0);
      } else {
         Annotation[] var2 = var1.getAnnotations();
         return this.getAnnotationNames(var2);
      }
   }

   private List getAnnotationNames(Annotation[] var1) {
      ArrayList var2 = Lists.newArrayList();
      Annotation[] var3 = var1;
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Annotation var6 = var3[var5];
         var2.add(var6.getTypeName());
      }

      return var2;
   }

   private List splitDescriptorToTypeNames(String var1) {
      ArrayList var2 = Lists.newArrayList();
      if (var1 != null && var1.length() != 0) {
         ArrayList var3 = Lists.newArrayList();
         Descriptor.Iterator var4 = new Descriptor.Iterator(var1);

         while(var4.hasNext()) {
            var3.add(var4.next());
         }

         var3.add(var1.length());

         for(int var5 = 0; var5 < var3.size() - 1; ++var5) {
            String var6 = Descriptor.toString(var1.substring((Integer)var3.get(var5), (Integer)var3.get(var5 + 1)));
            var2.add(var6);
         }
      }

      return var2;
   }

   public String getMethodFullKey(Object var1, Object var2) {
      return this.getMethodFullKey((ClassFile)var1, (MethodInfo)var2);
   }

   public String getMethodKey(Object var1, Object var2) {
      return this.getMethodKey((ClassFile)var1, (MethodInfo)var2);
   }

   public String getMethodModifier(Object var1) {
      return this.getMethodModifier((MethodInfo)var1);
   }

   public Object createClassObject(InputStream var1) throws IOException {
      return this.createClassObject(var1);
   }

   public String getFieldName(Object var1) {
      return this.getFieldName((FieldInfo)var1);
   }

   public String getReturnTypeName(Object var1) {
      return this.getReturnTypeName((MethodInfo)var1);
   }

   public List getParameterAnnotationNames(Object var1, int var2) {
      return this.getParameterAnnotationNames((MethodInfo)var1, var2);
   }

   public List getMethodAnnotationNames(Object var1) {
      return this.getMethodAnnotationNames((MethodInfo)var1);
   }

   public List getFieldAnnotationNames(Object var1) {
      return this.getFieldAnnotationNames((FieldInfo)var1);
   }

   public List getClassAnnotationNames(Object var1) {
      return this.getClassAnnotationNames((ClassFile)var1);
   }

   public List getParameterNames(Object var1) {
      return this.getParameterNames((MethodInfo)var1);
   }

   public String getMethodName(Object var1) {
      return this.getMethodName((MethodInfo)var1);
   }

   public List getMethods(Object var1) {
      return this.getMethods((ClassFile)var1);
   }

   public List getFields(Object var1) {
      return this.getFields((ClassFile)var1);
   }

   public List getInterfacesNames(Object var1) {
      return this.getInterfacesNames((ClassFile)var1);
   }

   public String getSuperclassName(Object var1) {
      return this.getSuperclassName((ClassFile)var1);
   }

   public String getClassName(Object var1) {
      return this.getClassName((ClassFile)var1);
   }
}
