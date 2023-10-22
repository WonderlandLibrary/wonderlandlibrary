package com.sun.javafx.property.adapter;

import java.lang.reflect.Method;
import sun.reflect.misc.ReflectUtil;

public class ReadOnlyJavaBeanPropertyBuilderHelper {
   private static final String IS_PREFIX = "is";
   private static final String GET_PREFIX = "get";
   private String propertyName;
   private Class beanClass;
   private Object bean;
   private String getterName;
   private Method getter;
   private ReadOnlyPropertyDescriptor descriptor;

   public void name(String var1) {
      if (var1 == null) {
         if (this.propertyName == null) {
            return;
         }
      } else if (var1.equals(this.propertyName)) {
         return;
      }

      this.propertyName = var1;
      this.descriptor = null;
   }

   public void beanClass(Class var1) {
      if (var1 == null) {
         if (this.beanClass == null) {
            return;
         }
      } else if (var1.equals(this.beanClass)) {
         return;
      }

      ReflectUtil.checkPackageAccess(var1);
      this.beanClass = var1;
      this.descriptor = null;
   }

   public void bean(Object var1) {
      this.bean = var1;
      if (var1 != null) {
         Class var2 = var1.getClass();
         if (this.beanClass == null || !this.beanClass.isAssignableFrom(var2)) {
            ReflectUtil.checkPackageAccess(var2);
            this.beanClass = var1.getClass();
            this.descriptor = null;
         }
      }

   }

   public Object getBean() {
      return this.bean;
   }

   public void getterName(String var1) {
      if (var1 == null) {
         if (this.getterName == null) {
            return;
         }
      } else if (var1.equals(this.getterName)) {
         return;
      }

      this.getterName = var1;
      this.descriptor = null;
   }

   public void getter(Method var1) {
      if (var1 == null) {
         if (this.getter == null) {
            return;
         }
      } else if (var1.equals(this.getter)) {
         return;
      }

      this.getter = var1;
      this.descriptor = null;
   }

   public ReadOnlyPropertyDescriptor getDescriptor() throws NoSuchMethodException {
      if (this.descriptor == null) {
         if (this.propertyName == null || this.bean == null) {
            throw new NullPointerException("Bean and property name have to be specified");
         }

         if (this.propertyName.isEmpty()) {
            throw new IllegalArgumentException("Property name cannot be empty");
         }

         String var1 = ReadOnlyPropertyDescriptor.capitalizedName(this.propertyName);
         if (this.getter == null) {
            if (this.getterName != null && !this.getterName.isEmpty()) {
               this.getter = this.beanClass.getMethod(this.getterName);
            } else {
               try {
                  this.getter = this.beanClass.getMethod("is" + var1);
               } catch (NoSuchMethodException var3) {
                  this.getter = this.beanClass.getMethod("get" + var1);
               }
            }
         }

         this.descriptor = new ReadOnlyPropertyDescriptor(this.propertyName, this.beanClass, this.getter);
      }

      return this.descriptor;
   }
}
