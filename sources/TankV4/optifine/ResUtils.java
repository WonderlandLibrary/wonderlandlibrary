package optifine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

public class ResUtils {
   public static String[] collectFiles(String var0, String var1) {
      return collectFiles(new String[]{var0}, new String[]{var1});
   }

   public static String[] collectFiles(String[] var0, String[] var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      IResourcePack[] var3 = Config.getResourcePacks();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         IResourcePack var5 = var3[var4];
         String[] var6 = collectFiles(var5, (String[])var0, (String[])var1, (String[])null);
         var2.addAll(Arrays.asList(var6));
      }

      String[] var7 = (String[])var2.toArray(new String[var2.size()]);
      return var7;
   }

   public static String[] collectFiles(IResourcePack var0, String var1, String var2, String[] var3) {
      return collectFiles(var0, new String[]{var1}, new String[]{var2}, var3);
   }

   public static String[] collectFiles(IResourcePack var0, String[] var1, String[] var2) {
      return collectFiles(var0, (String[])var1, (String[])var2, (String[])null);
   }

   public static String[] collectFiles(IResourcePack var0, String[] var1, String[] var2, String[] var3) {
      if (var0 instanceof DefaultResourcePack) {
         return collectFilesFixed(var0, var3);
      } else if (!(var0 instanceof AbstractResourcePack)) {
         return new String[0];
      } else {
         AbstractResourcePack var4 = (AbstractResourcePack)var0;
         File var5 = var4.resourcePackFile;
         return var5 == null ? new String[0] : (var5.isDirectory() ? collectFilesFolder(var5, "", var1, var2) : (var5.isFile() ? collectFilesZIP(var5, var1, var2) : new String[0]));
      }
   }

   private static String[] collectFilesFixed(IResourcePack var0, String[] var1) {
      if (var1 == null) {
         return new String[0];
      } else {
         ArrayList var2 = new ArrayList();

         for(int var3 = 0; var3 < var1.length; ++var3) {
            String var4 = var1[var3];
            ResourceLocation var5 = new ResourceLocation(var4);
            if (var0.resourceExists(var5)) {
               var2.add(var4);
            }
         }

         String[] var6 = (String[])var2.toArray(new String[var2.size()]);
         return var6;
      }
   }

   private static String[] collectFilesFolder(File var0, String var1, String[] var2, String[] var3) {
      ArrayList var4 = new ArrayList();
      String var5 = "assets/minecraft/";
      File[] var6 = var0.listFiles();
      if (var6 == null) {
         return new String[0];
      } else {
         for(int var7 = 0; var7 < var6.length; ++var7) {
            File var8 = var6[var7];
            String var9;
            if (var8.isFile()) {
               var9 = var1 + var8.getName();
               if (var9.startsWith(var5)) {
                  var9 = var9.substring(var5.length());
                  if (StrUtils.startsWith(var9, var2) && StrUtils.endsWith(var9, var3)) {
                     var4.add(var9);
                  }
               }
            } else if (var8.isDirectory()) {
               var9 = var1 + var8.getName() + "/";
               String[] var10 = collectFilesFolder(var8, var9, var2, var3);

               for(int var11 = 0; var11 < var10.length; ++var11) {
                  String var12 = var10[var11];
                  var4.add(var12);
               }
            }
         }

         String[] var13 = (String[])var4.toArray(new String[var4.size()]);
         return var13;
      }
   }

   private static String[] collectFilesZIP(File var0, String[] var1, String[] var2) {
      ArrayList var3 = new ArrayList();
      String var4 = "assets/minecraft/";

      try {
         ZipFile var5 = new ZipFile(var0);
         Enumeration var6 = var5.entries();

         while(var6.hasMoreElements()) {
            ZipEntry var7 = (ZipEntry)var6.nextElement();
            String var8 = var7.getName();
            if (var8.startsWith(var4)) {
               var8 = var8.substring(var4.length());
               if (StrUtils.startsWith(var8, var1) && StrUtils.endsWith(var8, var2)) {
                  var3.add(var8);
               }
            }
         }

         var5.close();
         String[] var10 = (String[])var3.toArray(new String[var3.size()]);
         return var10;
      } catch (IOException var9) {
         var9.printStackTrace();
         return new String[0];
      }
   }
}
