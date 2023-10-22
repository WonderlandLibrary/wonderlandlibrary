package com.sun.javafx.property;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javafx.beans.property.ReadOnlyProperty;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

public final class PropertyReference {
   private String name;
   private Method getter;
   private Method setter;
   private Method propertyGetter;
   private Class clazz;
   private Class type;
   private boolean reflected = false;

   public PropertyReference(Class var1, String var2) {
      if (var2 == null) {
         throw new NullPointerException("Name must be specified");
      } else if (var2.trim().length() == 0) {
         throw new IllegalArgumentException("Name must be specified");
      } else if (var1 == null) {
         throw new NullPointerException("Class must be specified");
      } else {
         ReflectUtil.checkPackageAccess(var1);
         this.name = var2;
         this.clazz = var1;
      }
   }

   public boolean isWritable() {
      this.reflect();
      return this.setter != null;
   }

   public boolean isReadable() {
      this.reflect();
      return this.getter != null;
   }

   public boolean hasProperty() {
      this.reflect();
      return this.propertyGetter != null;
   }

   public String getName() {
      return this.name;
   }

   public Class getContainingClass() {
      return this.clazz;
   }

   public Class getType() {
      this.reflect();
      return this.type;
   }

   public void set(Object var1, Object var2) {
      if (!this.isWritable()) {
         throw new IllegalStateException("Cannot write to readonly property " + this.name);
      } else {
         assert this.setter != null;

         try {
            MethodUtil.invoke(this.setter, var1, new Object[]{var2});
         } catch (Exception var4) {
            throw new RuntimeException(var4);
         }
      }
   }

   public Object get(Object var1) {
      if (!this.isReadable()) {
         throw new IllegalStateException("Cannot read from unreadable property " + this.name);
      } else {
         assert this.getter != null;

         try {
            return MethodUtil.invoke(this.getter, var1, (Object[])null);
         } catch (Exception var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   public ReadOnlyProperty getProperty(Object var1) {
      if (!this.hasProperty()) {
         throw new IllegalStateException("Cannot get property " + this.name);
      } else {
         assert this.propertyGetter != null;

         try {
            return (ReadOnlyProperty)MethodUtil.invoke(this.propertyGetter, var1, (Object[])null);
         } catch (Exception var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   public String toString() {
      return this.name;
   }

   private void reflect() {
      if (!this.reflected) {
         this.reflected = true;

         try {
            String var1 = this.name.length() == 1 ? this.name.substring(0, 1).toUpperCase() : Character.toUpperCase(this.name.charAt(0)) + this.name.substring(1);
            this.type = null;
            String var2 = "get" + var1;

            Method var3;
            try {
               var3 = this.clazz.getMethod(var2);
               if (Modifier.isPublic(var3.getModifiers())) {
                  this.getter = var3;
               }
            } catch (NoSuchMethodException var13) {
            }

            if (this.getter == null) {
               var2 = "is" + var1;

               try {
                  var3 = this.clazz.getMethod(var2);
                  if (Modifier.isPublic(var3.getModifiers())) {
                     this.getter = var3;
                  }
               } catch (NoSuchMethodException var12) {
               }
            }

            String var15 = "set" + var1;
            if (this.getter != null) {
               this.type = this.getter.getReturnType();

               try {
                  Method var16 = this.clazz.getMethod(var15, this.type);
                  if (Modifier.isPublic(var16.getModifiers())) {
                     this.setter = var16;
                  }
               } catch (NoSuchMethodException var11) {
               }
            } else {
               Method[] var4 = this.clazz.getMethods();
               Method[] var5 = var4;
               int var6 = var4.length;

               for(int var7 = 0; var7 < var6; ++var7) {
                  Method var8 = var5[var7];
                  Class[] var9 = var8.getParameterTypes();
                  if (var15.equals(var8.getName()) && var9.length == 1 && Modifier.isPublic(var8.getModifiers())) {
                     this.setter = var8;
                     this.type = var9[0];
                     break;
                  }
               }
            }

            String var17 = this.name + "Property";

            try {
               Method var18 = this.clazz.getMethod(var17);
               if (Modifier.isPublic(var18.getModifiers())) {
                  this.propertyGetter = var18;
               } else {
                  this.propertyGetter = null;
               }
            } catch (NoSuchMethodException var10) {
            }
         } catch (RuntimeException var14) {
            System.err.println("Failed to introspect property " + this.name);
         }
      }

   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof PropertyReference)) {
         return false;
      } else {
         PropertyReference var2 = (PropertyReference)var1;
         if (this.name == var2.name || this.name != null && this.name.equals(var2.name)) {
            return this.clazz == var2.clazz || this.clazz != null && this.clazz.equals(var2.clazz);
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = 5;
      var1 = 97 * var1 + (this.name != null ? this.name.hashCode() : 0);
      var1 = 97 * var1 + (this.clazz != null ? this.clazz.hashCode() : 0);
      return var1;
   }
}
