package org.apache.commons.compress.archivers.zip;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipException;

public class ExtraFieldUtils {
   private static final int WORD = 4;
   private static final Map implementations = new ConcurrentHashMap();

   public static void register(Class var0) {
      try {
         ZipExtraField var1 = (ZipExtraField)var0.newInstance();
         implementations.put(var1.getHeaderId(), var0);
      } catch (ClassCastException var2) {
         throw new RuntimeException(var0 + " doesn't implement ZipExtraField");
      } catch (InstantiationException var3) {
         throw new RuntimeException(var0 + " is not a concrete class");
      } catch (IllegalAccessException var4) {
         throw new RuntimeException(var0 + "'s no-arg constructor is not public");
      }
   }

   public static ZipExtraField createExtraField(ZipShort var0) throws InstantiationException, IllegalAccessException {
      Class var1 = (Class)implementations.get(var0);
      if (var1 != null) {
         return (ZipExtraField)var1.newInstance();
      } else {
         UnrecognizedExtraField var2 = new UnrecognizedExtraField();
         var2.setHeaderId(var0);
         return var2;
      }
   }

   public static ZipExtraField[] parse(byte[] var0) throws ZipException {
      return parse(var0, true, ExtraFieldUtils.UnparseableExtraField.THROW);
   }

   public static ZipExtraField[] parse(byte[] var0, boolean var1) throws ZipException {
      return parse(var0, var1, ExtraFieldUtils.UnparseableExtraField.THROW);
   }

   public static ZipExtraField[] parse(byte[] var0, boolean var1, ExtraFieldUtils.UnparseableExtraField var2) throws ZipException {
      ArrayList var3 = new ArrayList();

      int var6;
      label41:
      for(int var4 = 0; var4 <= var0.length - 4; var4 += var6 + 4) {
         ZipShort var5 = new ZipShort(var0, var4);
         var6 = (new ZipShort(var0, var4 + 2)).getValue();
         if (var4 + 4 + var6 > var0.length) {
            switch(var2.getKey()) {
            case 0:
               throw new ZipException("bad extra field starting at " + var4 + ".  Block length of " + var6 + " bytes exceeds remaining" + " data of " + (var0.length - var4 - 4) + " bytes.");
            case 1:
               break label41;
            case 2:
               UnparseableExtraFieldData var11 = new UnparseableExtraFieldData();
               if (var1) {
                  var11.parseFromLocalFileData(var0, var4, var0.length - var4);
               } else {
                  var11.parseFromCentralDirectoryData(var0, var4, var0.length - var4);
               }

               var3.add(var11);
               break label41;
            default:
               throw new ZipException("unknown UnparseableExtraField key: " + var2.getKey());
            }
         }

         try {
            ZipExtraField var7 = createExtraField(var5);
            if (var1) {
               var7.parseFromLocalFileData(var0, var4 + 4, var6);
            } else {
               var7.parseFromCentralDirectoryData(var0, var4 + 4, var6);
            }

            var3.add(var7);
         } catch (InstantiationException var8) {
            throw (ZipException)(new ZipException(var8.getMessage())).initCause(var8);
         } catch (IllegalAccessException var9) {
            throw (ZipException)(new ZipException(var9.getMessage())).initCause(var9);
         }
      }

      ZipExtraField[] var10 = new ZipExtraField[var3.size()];
      return (ZipExtraField[])var3.toArray(var10);
   }

   public static byte[] mergeLocalFileDataData(ZipExtraField[] var0) {
      boolean var1 = var0.length > 0 && var0[var0.length - 1] instanceof UnparseableExtraFieldData;
      int var2 = var1 ? var0.length - 1 : var0.length;
      int var3 = 4 * var2;
      ZipExtraField[] var4 = var0;
      int var5 = var0.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         ZipExtraField var7 = var4[var6];
         var3 += var7.getLocalFileDataLength().getValue();
      }

      byte[] var8 = new byte[var3];
      var5 = 0;

      for(var6 = 0; var6 < var2; ++var6) {
         System.arraycopy(var0[var6].getHeaderId().getBytes(), 0, var8, var5, 2);
         System.arraycopy(var0[var6].getLocalFileDataLength().getBytes(), 0, var8, var5 + 2, 2);
         var5 += 4;
         byte[] var9 = var0[var6].getLocalFileDataData();
         if (var9 != null) {
            System.arraycopy(var9, 0, var8, var5, var9.length);
            var5 += var9.length;
         }
      }

      if (var1) {
         byte[] var10 = var0[var0.length - 1].getLocalFileDataData();
         if (var10 != null) {
            System.arraycopy(var10, 0, var8, var5, var10.length);
         }
      }

      return var8;
   }

   public static byte[] mergeCentralDirectoryData(ZipExtraField[] var0) {
      boolean var1 = var0.length > 0 && var0[var0.length - 1] instanceof UnparseableExtraFieldData;
      int var2 = var1 ? var0.length - 1 : var0.length;
      int var3 = 4 * var2;
      ZipExtraField[] var4 = var0;
      int var5 = var0.length;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         ZipExtraField var7 = var4[var6];
         var3 += var7.getCentralDirectoryLength().getValue();
      }

      byte[] var8 = new byte[var3];
      var5 = 0;

      for(var6 = 0; var6 < var2; ++var6) {
         System.arraycopy(var0[var6].getHeaderId().getBytes(), 0, var8, var5, 2);
         System.arraycopy(var0[var6].getCentralDirectoryLength().getBytes(), 0, var8, var5 + 2, 2);
         var5 += 4;
         byte[] var9 = var0[var6].getCentralDirectoryData();
         if (var9 != null) {
            System.arraycopy(var9, 0, var8, var5, var9.length);
            var5 += var9.length;
         }
      }

      if (var1) {
         byte[] var10 = var0[var0.length - 1].getCentralDirectoryData();
         if (var10 != null) {
            System.arraycopy(var10, 0, var8, var5, var10.length);
         }
      }

      return var8;
   }

   static {
      register(AsiExtraField.class);
      register(X5455_ExtendedTimestamp.class);
      register(X7875_NewUnix.class);
      register(JarMarker.class);
      register(UnicodePathExtraField.class);
      register(UnicodeCommentExtraField.class);
      register(Zip64ExtendedInformationExtraField.class);
   }

   public static final class UnparseableExtraField {
      public static final int THROW_KEY = 0;
      public static final int SKIP_KEY = 1;
      public static final int READ_KEY = 2;
      public static final ExtraFieldUtils.UnparseableExtraField THROW = new ExtraFieldUtils.UnparseableExtraField(0);
      public static final ExtraFieldUtils.UnparseableExtraField SKIP = new ExtraFieldUtils.UnparseableExtraField(1);
      public static final ExtraFieldUtils.UnparseableExtraField READ = new ExtraFieldUtils.UnparseableExtraField(2);
      private final int key;

      private UnparseableExtraField(int var1) {
         this.key = var1;
      }

      public int getKey() {
         return this.key;
      }
   }
}
