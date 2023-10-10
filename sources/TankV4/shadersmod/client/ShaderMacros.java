package shadersmod.client;

import net.minecraft.util.Util;
import optifine.Config;

public class ShaderMacros {
   private static String PREFIX_MACRO = "MC_";
   public static final String MC_VERSION = "MC_VERSION";
   public static final String MC_GL_VERSION = "MC_GL_VERSION";
   public static final String MC_GLSL_VERSION = "MC_GLSL_VERSION";
   public static final String MC_OS_WINDOWS = "MC_OS_WINDOWS";
   public static final String MC_OS_MAC = "MC_OS_MAC";
   public static final String MC_OS_LINUX = "MC_OS_LINUX";
   public static final String MC_OS_OTHER = "MC_OS_OTHER";
   public static final String MC_GL_VENDOR_ATI = "MC_GL_VENDOR_ATI";
   public static final String MC_GL_VENDOR_INTEL = "MC_GL_VENDOR_INTEL";
   public static final String MC_GL_VENDOR_NVIDIA = "MC_GL_VENDOR_NVIDIA";
   public static final String MC_GL_VENDOR_XORG = "MC_GL_VENDOR_XORG";
   public static final String MC_GL_VENDOR_OTHER = "MC_GL_VENDOR_OTHER";
   public static final String MC_GL_RENDERER_RADEON = "MC_GL_RENDERER_RADEON";
   public static final String MC_GL_RENDERER_GEFORCE = "MC_GL_RENDERER_GEFORCE";
   public static final String MC_GL_RENDERER_QUADRO = "MC_GL_RENDERER_QUADRO";
   public static final String MC_GL_RENDERER_INTEL = "MC_GL_RENDERER_INTEL";
   public static final String MC_GL_RENDERER_GALLIUM = "MC_GL_RENDERER_GALLIUM";
   public static final String MC_GL_RENDERER_MESA = "MC_GL_RENDERER_MESA";
   public static final String MC_GL_RENDERER_OTHER = "MC_GL_RENDERER_OTHER";
   private static String[] extensionMacros;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$Util$EnumOS;

   public static String getOs() {
      Util.EnumOS var0 = Util.getOSType();
      switch($SWITCH_TABLE$net$minecraft$util$Util$EnumOS()[var0.ordinal()]) {
      case 1:
         return "MC_OS_LINUX";
      case 2:
      default:
         return "MC_OS_OTHER";
      case 3:
         return "MC_OS_WINDOWS";
      case 4:
         return "MC_OS_MAC";
      }
   }

   public static String getVendor() {
      String var0 = Config.openGlVendor;
      if (var0 == null) {
         return "MC_GL_VENDOR_OTHER";
      } else {
         var0 = var0.toLowerCase();
         return var0.startsWith("ati") ? "MC_GL_VENDOR_ATI" : (var0.startsWith("intel") ? "MC_GL_VENDOR_INTEL" : (var0.startsWith("nvidia") ? "MC_GL_VENDOR_NVIDIA" : (var0.startsWith("x.org") ? "MC_GL_VENDOR_XORG" : "MC_GL_VENDOR_OTHER")));
      }
   }

   public static String getRenderer() {
      String var0 = Config.openGlRenderer;
      if (var0 == null) {
         return "MC_GL_RENDERER_OTHER";
      } else {
         var0 = var0.toLowerCase();
         return var0.startsWith("amd") ? "MC_GL_RENDERER_RADEON" : (var0.startsWith("ati") ? "MC_GL_RENDERER_RADEON" : (var0.startsWith("radeon") ? "MC_GL_RENDERER_RADEON" : (var0.startsWith("gallium") ? "MC_GL_RENDERER_GALLIUM" : (var0.startsWith("intel") ? "MC_GL_RENDERER_INTEL" : (var0.startsWith("geforce") ? "MC_GL_RENDERER_GEFORCE" : (var0.startsWith("nvidia") ? "MC_GL_RENDERER_GEFORCE" : (var0.startsWith("quadro") ? "MC_GL_RENDERER_QUADRO" : (var0.startsWith("nvs") ? "MC_GL_RENDERER_QUADRO" : (var0.startsWith("mesa") ? "MC_GL_RENDERER_MESA" : "MC_GL_RENDERER_OTHER")))))))));
      }
   }

   public static String getPrefixMacro() {
      return PREFIX_MACRO;
   }

   public static String[] getExtensions() {
      if (extensionMacros == null) {
         String[] var0 = Config.getOpenGlExtensions();
         String[] var1 = new String[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = PREFIX_MACRO + var0[var2];
         }

         extensionMacros = var1;
      }

      return extensionMacros;
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$Util$EnumOS() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$Util$EnumOS;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[Util.EnumOS.values().length];

         try {
            var0[Util.EnumOS.LINUX.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[Util.EnumOS.OSX.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[Util.EnumOS.SOLARIS.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[Util.EnumOS.UNKNOWN.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[Util.EnumOS.WINDOWS.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$Util$EnumOS = var0;
         return var0;
      }
   }
}
