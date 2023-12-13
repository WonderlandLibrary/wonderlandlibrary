package com.sun.glass.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CommonDialogs {
   private CommonDialogs() {
   }

   public static FileChooserResult showFileChooser(Window var0, File var1, String var2, String var3, int var4, boolean var5, List var6, int var7) {
      Application.checkEventThread();
      String var8 = convertFolder(var1);
      if (var2 == null) {
         var2 = "";
      }

      if (var4 != 0 && var4 != 1) {
         throw new IllegalArgumentException("Type parameter must be equal to one of the constants from Type");
      } else {
         ExtensionFilter[] var9 = null;
         if (var6 != null) {
            var9 = (ExtensionFilter[])var6.toArray(new ExtensionFilter[var6.size()]);
         }

         if (var6 == null || var6.isEmpty() || var7 < 0 || var7 >= var6.size()) {
            var7 = 0;
         }

         return Application.GetApplication().staticCommonDialogs_showFileChooser(var0, var8, var2, convertTitle(var3), var4, var5, var9, var7);
      }
   }

   public static File showFolderChooser(Window var0, File var1, String var2) {
      Application.checkEventThread();
      return Application.GetApplication().staticCommonDialogs_showFolderChooser(var0, convertFolder(var1), convertTitle(var2));
   }

   private static String convertFolder(File var0) {
      if (var0 != null) {
         if (var0.isDirectory()) {
            try {
               return var0.getCanonicalPath();
            } catch (IOException var2) {
               throw new IllegalArgumentException("Unable to get a canonical path for folder", var2);
            }
         } else {
            throw new IllegalArgumentException("Folder parameter must be a valid folder");
         }
      } else {
         return "";
      }
   }

   private static String convertTitle(String var0) {
      return var0 != null ? var0 : "";
   }

   protected static FileChooserResult createFileChooserResult(String[] var0, ExtensionFilter[] var1, int var2) {
      ArrayList var3 = new ArrayList();
      String[] var4 = var0;
      int var5 = var0.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         if (var7 != null) {
            var3.add(new File(var7));
         }
      }

      return new FileChooserResult(var3, var1 != null && var2 >= 0 && var2 < var1.length ? var1[var2] : null);
   }

   public static final class FileChooserResult {
      private final List files;
      private final ExtensionFilter filter;

      public FileChooserResult(List var1, ExtensionFilter var2) {
         if (var1 == null) {
            throw new NullPointerException("files should not be null");
         } else {
            this.files = var1;
            this.filter = var2;
         }
      }

      public FileChooserResult() {
         this(new ArrayList(), (ExtensionFilter)null);
      }

      public List getFiles() {
         return this.files;
      }

      public ExtensionFilter getExtensionFilter() {
         return this.filter;
      }
   }

   public static final class ExtensionFilter {
      private final String description;
      private final List extensions;

      public ExtensionFilter(String var1, List var2) {
         Application.checkEventThread();
         if (var1 != null && !var1.trim().isEmpty()) {
            if (var2 != null && !var2.isEmpty()) {
               Iterator var3 = var2.iterator();

               String var4;
               do {
                  if (!var3.hasNext()) {
                     this.description = var1;
                     this.extensions = var2;
                     return;
                  }

                  var4 = (String)var3.next();
               } while(var4 != null && var4.length() != 0);

               throw new IllegalArgumentException("Each extension must be non-null and not empty");
            } else {
               throw new IllegalArgumentException("Extensions parameter must be non-null and not empty");
            }
         } else {
            throw new IllegalArgumentException("Description parameter must be non-null and not empty");
         }
      }

      public String getDescription() {
         Application.checkEventThread();
         return this.description;
      }

      public List getExtensions() {
         Application.checkEventThread();
         return this.extensions;
      }

      private String[] extensionsToArray() {
         Application.checkEventThread();
         return (String[])this.extensions.toArray(new String[this.extensions.size()]);
      }
   }

   public static final class Type {
      public static final int OPEN = 0;
      public static final int SAVE = 1;
   }
}
