package my.NewSnake.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.opengl.GL11;

public class GLUtil {
   public static Map glCapMap = new HashMap();

   public static void glEnable(int var0) {
      setGLCap(var0, true);
   }

   public static void revertAllCaps() {
      Iterator var1 = glCapMap.keySet().iterator();

      while(var1.hasNext()) {
         int var0 = (Integer)var1.next();
         revertGLCap(var0);
      }

   }

   public static void revertGLCap(int var0) {
      Boolean var1 = (Boolean)glCapMap.get(var0);
      if (var1 != null) {
         if (var1) {
            GL11.glEnable(var0);
         } else {
            GL11.glDisable(var0);
         }
      }

   }

   public static void setGLCap(int var0, boolean var1) {
      glCapMap.put(var0, GL11.glGetBoolean(var0));
      if (var1) {
         GL11.glEnable(var0);
      } else {
         GL11.glDisable(var0);
      }

   }

   public static void glDisable(int var0) {
      setGLCap(var0, false);
   }
}
