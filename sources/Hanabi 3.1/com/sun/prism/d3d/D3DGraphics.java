package com.sun.prism.d3d;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ps.BaseShaderGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

class D3DGraphics extends BaseShaderGraphics implements D3DContextSource {
   private D3DContext context;

   private D3DGraphics(D3DContext var1, RenderTarget var2) {
      super(var1, var2);
      this.context = var1;
   }

   public void getPaintShaderTransform(Affine3D var1) {
      super.getPaintShaderTransform(var1);
      var1.preTranslate(-0.5, -0.5, 0.0);
   }

   static Graphics create(RenderTarget var0, D3DContext var1) {
      if (var0 == null) {
         return null;
      } else {
         long var2 = ((D3DRenderTarget)var0).getResourceHandle();
         if (var2 == 0L) {
            return null;
         } else {
            if (PrismSettings.verbose && var1.isLost()) {
               System.err.println("Create graphics while the device is lost");
            }

            return new D3DGraphics(var1, var0);
         }
      }
   }

   public void clearQuad(float var1, float var2, float var3, float var4) {
      this.context.setRenderTarget(this);
      this.context.flushVertexBuffer();
      CompositeMode var5 = this.getCompositeMode();
      this.setCompositeMode(CompositeMode.CLEAR);
      Paint var6 = this.getPaint();
      this.setPaint(Color.BLACK);
      this.fillQuad(var1, var2, var3, var4);
      this.context.flushVertexBuffer();
      this.setPaint(var6);
      this.setCompositeMode(var5);
   }

   public void clear(Color var1) {
      this.context.validateClearOp(this);
      this.getRenderTarget().setOpaque(var1.isOpaque());
      int var2 = nClear(this.context.getContextHandle(), var1.getIntArgbPre(), this.isDepthBuffer(), false);
      D3DContext var10000 = this.context;
      D3DContext.validate(var2);
   }

   public void sync() {
      this.context.flushVertexBuffer();
   }

   public D3DContext getContext() {
      return this.context;
   }

   private static native int nClear(long var0, int var2, boolean var3, boolean var4);
}
