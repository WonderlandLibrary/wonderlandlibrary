package org.apache.commons.lang3.builder;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang3.ArrayUtils;

public class ReflectionToStringBuilder extends ToStringBuilder {
   private boolean appendStatics = false;
   private boolean appendTransients = false;
   protected String[] excludeFieldNames;
   private Class upToClass = null;

   public static String toString(Object var0) {
      return toString(var0, (ToStringStyle)null, false, false, (Class)null);
   }

   public static String toString(Object var0, ToStringStyle var1) {
      return toString(var0, var1, false, false, (Class)null);
   }

   public static String toString(Object var0, ToStringStyle var1, boolean var2) {
      return toString(var0, var1, var2, false, (Class)null);
   }

   public static String toString(Object var0, ToStringStyle var1, boolean var2, boolean var3) {
      return toString(var0, var1, var2, var3, (Class)null);
   }

   public static String toString(Object var0, ToStringStyle var1, boolean var2, boolean var3, Class var4) {
      return (new ReflectionToStringBuilder(var0, var1, (StringBuffer)null, var4, var2, var3)).toString();
   }

   public static String toStringExclude(Object var0, Collection var1) {
      return toStringExclude(var0, toNoNullStringArray(var1));
   }

   static String[] toNoNullStringArray(Collection var0) {
      return var0 == null ? ArrayUtils.EMPTY_STRING_ARRAY : toNoNullStringArray(var0.toArray());
   }

   static String[] toNoNullStringArray(Object[] var0) {
      ArrayList var1 = new ArrayList(var0.length);
      Object[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Object var5 = var2[var4];
         if (var5 != null) {
            var1.add(var5.toString());
         }
      }

      return (String[])var1.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
   }

   public static String toStringExclude(Object var0, String... var1) {
      return (new ReflectionToStringBuilder(var0)).setExcludeFieldNames(var1).toString();
   }

   public ReflectionToStringBuilder(Object var1) {
      super(var1);
   }

   public ReflectionToStringBuilder(Object var1, ToStringStyle var2) {
      super(var1, var2);
   }

   public ReflectionToStringBuilder(Object var1, ToStringStyle var2, StringBuffer var3) {
      super(var1, var2, var3);
   }

   public ReflectionToStringBuilder(Object var1, ToStringStyle var2, StringBuffer var3, Class var4, boolean var5, boolean var6) {
      super(var1, var2, var3);
      this.setUpToClass(var4);
      this.setAppendTransients(var5);
      this.setAppendStatics(var6);
   }

   protected void appendFieldsIn(Class var1) {
      if (var1.isArray()) {
         this.reflectionAppendArray(this.getObject());
      } else {
         Field[] var2 = var1.getDeclaredFields();
         AccessibleObject.setAccessible(var2, true);
         Field[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Field var6 = var3[var5];
            String var7 = var6.getName();
            if (this != var6) {
               try {
                  Object var8 = this.getValue(var6);
                  this.append(var7, var8);
               } catch (IllegalAccessException var9) {
                  throw new InternalError("Unexpected IllegalAccessException: " + var9.getMessage());
               }
            }
         }

      }
   }

   public String[] getExcludeFieldNames() {
      return (String[])this.excludeFieldNames.clone();
   }

   public Class getUpToClass() {
      return this.upToClass;
   }

   protected Object getValue(Field var1) throws IllegalArgumentException, IllegalAccessException {
      return var1.get(this.getObject());
   }

   public boolean isAppendStatics() {
      return this.appendStatics;
   }

   public boolean isAppendTransients() {
      return this.appendTransients;
   }

   public ReflectionToStringBuilder reflectionAppendArray(Object var1) {
      this.getStyle().reflectionAppendArrayDetail(this.getStringBuffer(), (String)null, var1);
      return this;
   }

   public void setAppendStatics(boolean var1) {
      this.appendStatics = var1;
   }

   public void setAppendTransients(boolean var1) {
      this.appendTransients = var1;
   }

   public ReflectionToStringBuilder setExcludeFieldNames(String... var1) {
      if (var1 == null) {
         this.excludeFieldNames = null;
      } else {
         this.excludeFieldNames = toNoNullStringArray((Object[])var1);
         Arrays.sort(this.excludeFieldNames);
      }

      return this;
   }

   public void setUpToClass(Class var1) {
      if (var1 != null) {
         Object var2 = this.getObject();
         if (var2 != null && !var1.isInstance(var2)) {
            throw new IllegalArgumentException("Specified class is not a superclass of the object");
         }
      }

      this.upToClass = var1;
   }

   public String toString() {
      if (this.getObject() == null) {
         return this.getStyle().getNullText();
      } else {
         Class var1 = this.getObject().getClass();
         this.appendFieldsIn(var1);

         while(var1.getSuperclass() != null && var1 != this.getUpToClass()) {
            var1 = var1.getSuperclass();
            this.appendFieldsIn(var1);
         }

         return super.toString();
      }
   }
}
