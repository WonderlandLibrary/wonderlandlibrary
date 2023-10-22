package com.sun.scenario.effect.impl.prism.ps;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.RTTexture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.prism.PrRenderer;
import com.sun.scenario.effect.impl.prism.PrTexture;
import com.sun.scenario.effect.impl.state.RenderState;

public class PPStoPSWDisplacementMapPeer extends EffectPeer {
   PrRenderer softwareRenderer;
   EffectPeer softwarePeer;

   public PPStoPSWDisplacementMapPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
      this.softwareRenderer = (PrRenderer)Renderer.getRenderer(var1);
      this.softwarePeer = this.softwareRenderer.getPeerInstance(var1, "DisplacementMap", 0);
   }

   public ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      ImageData var6 = var5[0];
      PrTexture var7 = (PrTexture)var6.getUntransformedImage();
      RTTexture var8 = (RTTexture)var7.getTextureObject();
      PrDrawable var9 = this.softwareRenderer.createDrawable(var8);
      ImageData var10 = new ImageData(this.getFilterContext(), var9, var6.getUntransformedBounds());
      var10 = var10.transform(var6.getTransform());
      ImageData var11 = this.softwarePeer.filter(var1, var2, var3, var4, var10);
      return var11;
   }
}
