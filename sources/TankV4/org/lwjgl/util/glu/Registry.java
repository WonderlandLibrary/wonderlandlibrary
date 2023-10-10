package org.lwjgl.util.glu;

public class Registry extends Util {
   private static final String versionString = "1.3";
   private static final String extensionString = "GLU_EXT_nurbs_tessellator GLU_EXT_object_space_tess ";

   public static String gluGetString(int var0) {
      if (var0 == 100800) {
         return "1.3";
      } else {
         return var0 == 100801 ? "GLU_EXT_nurbs_tessellator GLU_EXT_object_space_tess " : null;
      }
   }

   public static boolean gluCheckExtension(String var0, String var1) {
      if (var1 != null && var0 != null) {
         return var1.indexOf(var0) != -1;
      } else {
         return false;
      }
   }
}
