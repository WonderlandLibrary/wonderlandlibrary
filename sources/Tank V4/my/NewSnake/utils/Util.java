package my.NewSnake.utils;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class Util {
   private static String ja;
   private static final Minecraft mc = Minecraft.getMinecraft();
   public static final float doubleClickTimer = 0.3F;

   public static void openWeb(String var0) {
      try {
         Desktop var1 = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
         if (var1 != null && var1.isSupported(Action.BROWSE)) {
            try {
               var1.browse(new URI(var0));
            } catch (Exception var4) {
               var4.printStackTrace();
            }
         }
      } catch (Exception var5) {
      }

   }

   public static float makeFloat(Object var0) {
      try {
         return Float.parseFloat(String.valueOf(var0));
      } catch (Exception var2) {
         return -1.0F;
      }
   }

   public static List usernameHistory(String var0) {
      String var1 = null;
      Object var2 = new ArrayList();

      try {
         var1 = getUUIDFromName(var0);
      } catch (Exception var5) {
      }

      if (var1 == null) {
         return (List)var2;
      } else {
         try {
            var2 = getNamesFromUUID(var1);
         } catch (Exception var4) {
         }

         return (List)var2;
      }
   }

   public static float[] getRotationToBlockPos(BlockPos var0) {
      double var1 = Minecraft.thePlayer.posX;
      double var3 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
      double var5 = Minecraft.thePlayer.posZ;
      double var7 = (double)((float)var0.getX() + 0.5F);
      double var9 = (double)((float)var0.getY() + 0.5F);
      double var11 = (double)((float)var0.getZ() + 0.5F);
      double var13 = var1 - var7;
      double var15 = var3 - var9;
      double var17 = var5 - var11;
      double var19 = Math.sqrt(Math.pow(var13, 2.0D) + Math.pow(var17, 2.0D));
      float var21 = 0.0F;
      float var22 = 0.0F;
      var21 = (float)(Math.toDegrees(Math.atan2(var17, var13)) + 90.0D);
      var22 = (float)Math.toDegrees(Math.atan2(var19, var15));
      return new float[]{var21, 90.0F - var22};
   }

   public boolean isASCII(char var1) {
      return "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(var1) != -1;
   }

   public static int makeInteger(Object var0) {
      try {
         return Integer.parseInt(String.valueOf(prettyFloat((double)makeFloat(var0))));
      } catch (Exception var2) {
         return -1;
      }
   }

   public static String replaceFormat(String var0) {
      return var0.replaceAll("(?i)&([a-f0-9])", "§$1");
   }

   public static List getNamesFromUUID(String var0) throws Exception {
      ArrayList var1 = new ArrayList();
      URL var2 = new URL("https://api.mojang.com/user/profiles/" + var0 + "/names");
      BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));
      String var4 = "";
      String var5 = "";

      while((var4 = var3.readLine()) != null) {
      }

      var3.close();
      ja = null;

      for(int var6 = 0; var6 < ja.length(); ++var6) {
      }

      return var1;
   }

   public static boolean makeBoolean(Object var0) {
      try {
         return Boolean.parseBoolean(String.valueOf(var0));
      } catch (Exception var2) {
         return false;
      }
   }

   public static float angleDifference(float var0, float var1) {
      return ((var0 - var1) % 360.0F + 540.0F) % 360.0F - 180.0F;
   }

   public static int reAlpha(int var0, float var1) {
      Color var2 = new Color(var0);
      float var3 = 0.003921569F * (float)var2.getRed();
      float var4 = 0.003921569F * (float)var2.getGreen();
      float var5 = 0.003921569F * (float)var2.getBlue();
      return (new Color(var3, var4, var5, var1)).getRGB();
   }

   public static float[] getRotationToPos(double var0, double var2, double var4) {
      double var6 = Minecraft.thePlayer.posX;
      double var8 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
      double var10 = Minecraft.thePlayer.posZ;
      double var18 = var6 - var0;
      double var20 = var8 - var2;
      double var22 = var10 - var4;
      double var24 = Math.sqrt(Math.pow(var18, 2.0D) + Math.pow(var22, 2.0D));
      float var26 = 0.0F;
      float var27 = 0.0F;
      var26 = (float)(Math.toDegrees(Math.atan2(var22, var18)) + 90.0D);
      var27 = (float)Math.toDegrees(Math.atan2(var24, var20));
      return new float[]{var26, 90.0F - var27};
   }

   public static String getUUIDFromName(String var0) throws Exception {
      URL var1 = new URL("https://api.mojang.com/users/profiles/minecraft/" + var0);
      BufferedReader var2 = new BufferedReader(new InputStreamReader(var1.openStream()));

      String var3;
      String var4;
      for(var4 = ""; (var3 = var2.readLine()) != null; var4 = var3) {
      }

      var2.close();
      return var4;
   }

   public static double safeDiv(double var0, double var2) {
      try {
         return var0 / var2;
      } catch (Exception var6) {
         return 1.0D;
      }
   }

   public static int blendColor(int var0, int var1, float var2) {
      Color var3 = new Color(var0);
      Color var4 = new Color(var1);
      float var5 = 1.0F - var2;
      float var6 = (float)var3.getRed() * var2 + (float)var4.getRed() * var5;
      float var7 = (float)var3.getGreen() * var2 + (float)var4.getGreen() * var5;
      float var8 = (float)var3.getBlue() * var2 + (float)var4.getBlue() * var5;

      Color var9;
      try {
         var9 = new Color(var6 / 255.0F, var7 / 255.0F, var8 / 255.0F);
      } catch (Exception var11) {
         var9 = new Color(-1);
      }

      return var9.getRGB();
   }

   public static String prettyFloat(double var0) {
      return var0 == (double)((long)var0) ? String.format("%d", (long)var0) : String.format("%s", var0);
   }

   public static boolean canSeeEntity(EntityLivingBase var0, EntityLivingBase var1) {
      if (var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.posX, var1.posY + (double)var1.getEyeHeight(), var1.posZ)) == null) {
         return true;
      } else if (var1.getEntityBoundingBox() == null) {
         return false;
      } else if (var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.getEntityBoundingBox().minX, var1.getEntityBoundingBox().maxY, var1.getEntityBoundingBox().minZ)) == null) {
         return true;
      } else if (var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.getEntityBoundingBox().minX, var1.getEntityBoundingBox().maxY, var1.getEntityBoundingBox().maxZ)) == null) {
         return true;
      } else if (var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.getEntityBoundingBox().maxX, var1.getEntityBoundingBox().maxY, var1.getEntityBoundingBox().maxZ)) == null) {
         return true;
      } else if (var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.getEntityBoundingBox().maxX, var1.getEntityBoundingBox().maxY, var1.getEntityBoundingBox().minZ)) == null) {
         return true;
      } else if (var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.getEntityBoundingBox().minX, var1.getEntityBoundingBox().minY, var1.getEntityBoundingBox().minZ)) == null) {
         return true;
      } else if (var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.getEntityBoundingBox().minX, var1.getEntityBoundingBox().minY, var1.getEntityBoundingBox().maxZ)) == null) {
         return true;
      } else if (var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.getEntityBoundingBox().maxX, var1.getEntityBoundingBox().minY, var1.getEntityBoundingBox().maxZ)) == null) {
         return true;
      } else {
         return var0.worldObj.rayTraceBlocks(new Vec3(var0.posX, var0.posY + (double)var0.getEyeHeight(), var0.posZ), new Vec3(var1.getEntityBoundingBox().maxX, var1.getEntityBoundingBox().minY, var1.getEntityBoundingBox().minZ)) == null;
      }
   }
}
