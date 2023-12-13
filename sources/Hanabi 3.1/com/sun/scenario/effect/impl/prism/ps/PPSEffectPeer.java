package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.ps.Shader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class PPSEffectPeer extends EffectPeer {
   protected PPSEffectPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public final ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      this.setEffect(var1);
      this.setRenderState(var2);
      this.setDestBounds(this.getResultBounds(var3, var4, var5));
      return this.filterImpl(var5);
   }

   abstract ImageData filterImpl(ImageData... var1);

   protected abstract boolean isSamplerLinear(int var1);

   protected abstract Shader createShader();

   protected abstract void updateShader(Shader var1);

   public abstract void dispose();

   protected final PPSRenderer getRenderer() {
      return (PPSRenderer)super.getRenderer();
   }

   protected final String getShaderName() {
      return this.getUniqueName();
   }
}
