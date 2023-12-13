package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public abstract class PrRenderer extends Renderer {
   private static final Set intrinsicPeerNames = new HashSet(4);

   protected PrRenderer() {
   }

   public abstract PrDrawable createDrawable(RTTexture var1);

   public static Renderer createRenderer(FilterContext var0) {
      Object var1 = var0.getReferent();
      if (!(var1 instanceof Screen)) {
         return null;
      } else {
         boolean var2;
         if (((PrFilterContext)var0).isForceSoftware()) {
            var2 = false;
         } else {
            GraphicsPipeline var3 = GraphicsPipeline.getPipeline();
            if (var3 == null) {
               return null;
            }

            var2 = var3.supportsShaderModel(GraphicsPipeline.ShaderModel.SM3);
         }

         return createRenderer(var0, var2);
      }
   }

   private static PrRenderer createRenderer(FilterContext var0, boolean var1) {
      String var2 = var1 ? "com.sun.scenario.effect.impl.prism.ps.PPSRenderer" : "com.sun.scenario.effect.impl.prism.sw.PSWRenderer";

      try {
         Class var3 = Class.forName(var2);
         Method var4 = var3.getMethod("createRenderer", FilterContext.class);
         return (PrRenderer)var4.invoke((Object)null, var0);
      } catch (Throwable var5) {
         return null;
      }
   }

   public static boolean isIntrinsicPeer(String var0) {
      return intrinsicPeerNames.contains(var0);
   }

   static {
      intrinsicPeerNames.add("Crop");
      intrinsicPeerNames.add("Flood");
      intrinsicPeerNames.add("Merge");
      intrinsicPeerNames.add("Reflection");
   }
}
