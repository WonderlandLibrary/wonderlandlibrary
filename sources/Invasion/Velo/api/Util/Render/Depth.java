package Velo.api.Util.Render;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class Depth {
   private static final List depth = Lists.newArrayList();

   public static void pre() {
      if (depth.isEmpty()) {
         GlStateManager.clearDepth(1.0D);
         GlStateManager.clear(256);
      }

   }

   public static void mask() {
      depth.add(0, GL11.glGetInteger(2932));
      GlStateManager.enableDepth();
      GlStateManager.depthMask(true);
      GlStateManager.depthFunc(513);
      GlStateManager.colorMask(false, false, false, true);
   }

   public static void render() {
      render(514);
   }

   public static void render(int gl) {
      GlStateManager.depthFunc(gl);
      GlStateManager.colorMask(true, true, true, true);
   }

   public static void post() {
      GlStateManager.depthFunc(((Integer)depth.get(0)).intValue());
      depth.remove(0);
   }
}
