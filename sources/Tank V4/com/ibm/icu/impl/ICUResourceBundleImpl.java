package com.ibm.icu.impl;

import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import com.ibm.icu.util.UResourceTypeMismatchException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

class ICUResourceBundleImpl extends ICUResourceBundle {
   protected ICUResourceBundleImpl(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundleImpl var5) {
      super(var1, var2, var3, var4, var5);
   }

   protected final ICUResourceBundle createBundleObject(String var1, int var2, HashMap var3, UResourceBundle var4, boolean[] var5) {
      if (var5 != null) {
         var5[0] = false;
      }

      String var6 = this.resPath + "/" + var1;
      switch(ICUResourceBundleReader.RES_GET_TYPE(var2)) {
      case 0:
      case 6:
         return new ICUResourceBundleImpl.ResourceString(this.reader, var1, var6, var2, this);
      case 1:
         return new ICUResourceBundleImpl.ResourceBinary(this.reader, var1, var6, var2, this);
      case 2:
      case 4:
      case 5:
         return new ICUResourceBundleImpl.ResourceTable(this.reader, var1, var6, var2, this);
      case 3:
         if (var5 != null) {
            var5[0] = true;
         }

         return this.findResource(var1, var6, var2, var3, var4);
      case 7:
         return new ICUResourceBundleImpl.ResourceInt(this.reader, var1, var6, var2, this);
      case 8:
      case 9:
         return new ICUResourceBundleImpl.ResourceArray(this.reader, var1, var6, var2, this);
      case 10:
      case 11:
      case 12:
      case 13:
      default:
         throw new IllegalStateException("The resource type is unknown");
      case 14:
         return new ICUResourceBundleImpl.ResourceIntVector(this.reader, var1, var6, var2, this);
      }
   }

   static class ResourceTable extends ICUResourceBundleImpl.ResourceContainer {
      protected String getKey(int var1) {
         return ((ICUResourceBundleReader.Table)this.value).getKey(var1);
      }

      protected Set handleKeySet() {
         TreeSet var1 = new TreeSet();
         ICUResourceBundleReader.Table var2 = (ICUResourceBundleReader.Table)this.value;

         for(int var3 = 0; var3 < var2.getSize(); ++var3) {
            var1.add(var2.getKey(var3));
         }

         return var1;
      }

      protected int getTableResource(String var1) {
         return ((ICUResourceBundleReader.Table)this.value).getTableResource(var1);
      }

      protected int getTableResource(int var1) {
         return this.getContainerResource(var1);
      }

      protected UResourceBundle handleGetImpl(String var1, HashMap var2, UResourceBundle var3, int[] var4, boolean[] var5) {
         int var6 = ((ICUResourceBundleReader.Table)this.value).findTableItem(var1);
         if (var4 != null) {
            var4[0] = var6;
         }

         return var6 < 0 ? null : this.createBundleObject(var6, var1, var2, var3, var5);
      }

      protected UResourceBundle handleGetImpl(int var1, HashMap var2, UResourceBundle var3, boolean[] var4) {
         String var5 = ((ICUResourceBundleReader.Table)this.value).getKey(var1);
         if (var5 == null) {
            throw new IndexOutOfBoundsException();
         } else {
            return this.createBundleObject(var1, var5, var2, var3, var4);
         }
      }

      ResourceTable(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundleImpl var5) {
         super(var1, var2, var3, var4, var5);
         this.value = var1.getTable(var4);
         this.createLookupCache();
      }
   }

   private static class ResourceArray extends ICUResourceBundleImpl.ResourceContainer {
      protected String[] handleGetStringArray() {
         String[] var1 = new String[this.value.getSize()];
         UResourceBundleIterator var2 = this.getIterator();

         for(int var3 = 0; var2.hasNext(); var1[var3++] = var2.next().getString()) {
         }

         return var1;
      }

      public String[] getStringArray() {
         return this.handleGetStringArray();
      }

      protected UResourceBundle handleGetImpl(String var1, HashMap var2, UResourceBundle var3, int[] var4, boolean[] var5) {
         int var6 = var1.length() > 0 ? Integer.valueOf(var1) : -1;
         if (var4 != null) {
            var4[0] = var6;
         }

         if (var6 < 0) {
            throw new UResourceTypeMismatchException("Could not get the correct value for index: " + var1);
         } else {
            return this.createBundleObject(var6, var1, var2, var3, var5);
         }
      }

      protected UResourceBundle handleGetImpl(int var1, HashMap var2, UResourceBundle var3, boolean[] var4) {
         return this.createBundleObject(var1, Integer.toString(var1), var2, var3, var4);
      }

      ResourceArray(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundleImpl var5) {
         super(var1, var2, var3, var4, var5);
         this.value = var1.getArray(var4);
         this.createLookupCache();
      }
   }

   private static class ResourceContainer extends ICUResourceBundleImpl {
      protected ICUResourceBundleReader.Container value;

      public int getSize() {
         return this.value.getSize();
      }

      protected int getContainerResource(int var1) {
         return this.value.getContainerResource(var1);
      }

      protected UResourceBundle createBundleObject(int var1, String var2, HashMap var3, UResourceBundle var4, boolean[] var5) {
         int var6 = this.getContainerResource(var1);
         if (var6 == -1) {
            throw new IndexOutOfBoundsException();
         } else {
            return this.createBundleObject(var2, var6, var3, var4, var5);
         }
      }

      ResourceContainer(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundleImpl var5) {
         super(var1, var2, var3, var4, var5);
      }
   }

   private static final class ResourceIntVector extends ICUResourceBundleImpl {
      private int[] value;

      public int[] getIntVector() {
         return this.value;
      }

      ResourceIntVector(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundleImpl var5) {
         super(var1, var2, var3, var4, var5);
         this.value = var1.getIntVector(var4);
      }
   }

   private static final class ResourceString extends ICUResourceBundleImpl {
      private String value;

      public String getString() {
         return this.value;
      }

      ResourceString(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundleImpl var5) {
         super(var1, var2, var3, var4, var5);
         this.value = var1.getString(var4);
      }
   }

   private static final class ResourceInt extends ICUResourceBundleImpl {
      public int getInt() {
         return ICUResourceBundleReader.RES_GET_INT(this.resource);
      }

      public int getUInt() {
         return ICUResourceBundleReader.RES_GET_UINT(this.resource);
      }

      ResourceInt(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundleImpl var5) {
         super(var1, var2, var3, var4, var5);
      }
   }

   private static final class ResourceBinary extends ICUResourceBundleImpl {
      public ByteBuffer getBinary() {
         return this.reader.getBinary(this.resource);
      }

      public byte[] getBinary(byte[] var1) {
         return this.reader.getBinary(this.resource, var1);
      }

      ResourceBinary(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundleImpl var5) {
         super(var1, var2, var3, var4, var5);
      }
   }
}
