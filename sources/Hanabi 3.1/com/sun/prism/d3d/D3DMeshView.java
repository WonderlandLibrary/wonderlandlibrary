package com.sun.prism.d3d;

import com.sun.prism.Graphics;
import com.sun.prism.Material;
import com.sun.prism.impl.BaseMeshView;
import com.sun.prism.impl.Disposer;

class D3DMeshView extends BaseMeshView {
   static int count = 0;
   private final D3DContext context;
   private final long nativeHandle;
   private final D3DMesh mesh;
   private D3DPhongMaterial material;

   private D3DMeshView(D3DContext var1, long var2, D3DMesh var4, Disposer.Record var5) {
      super(var5);
      this.context = var1;
      this.mesh = var4;
      this.nativeHandle = var2;
      ++count;
   }

   static D3DMeshView create(D3DContext var0, D3DMesh var1) {
      long var2 = var0.createD3DMeshView(var1.getNativeHandle());
      return new D3DMeshView(var0, var2, var1, new D3DMeshViewDisposerRecord(var0, var2));
   }

   public void setCullingMode(int var1) {
      this.context.setCullingMode(this.nativeHandle, var1);
   }

   public void setMaterial(Material var1) {
      this.context.setMaterial(this.nativeHandle, ((D3DPhongMaterial)var1).getNativeHandle());
      this.material = (D3DPhongMaterial)var1;
   }

   public void setWireframe(boolean var1) {
      this.context.setWireframe(this.nativeHandle, var1);
   }

   public void setAmbientLight(float var1, float var2, float var3) {
      this.context.setAmbientLight(this.nativeHandle, var1, var2, var3);
   }

   public void setPointLight(int var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      if (var1 >= 0 && var1 <= 2) {
         this.context.setPointLight(this.nativeHandle, var1, var2, var3, var4, var5, var6, var7, var8);
      }

   }

   public void render(Graphics var1) {
      this.material.lockTextureMaps();
      this.context.renderMeshView(this.nativeHandle, var1);
      this.material.unlockTextureMaps();
   }

   public void dispose() {
      this.material = null;
      this.disposerRecord.dispose();
      --count;
   }

   public int getCount() {
      return count;
   }

   static class D3DMeshViewDisposerRecord implements Disposer.Record {
      private final D3DContext context;
      private long nativeHandle;

      D3DMeshViewDisposerRecord(D3DContext var1, long var2) {
         this.context = var1;
         this.nativeHandle = var2;
      }

      void traceDispose() {
      }

      public void dispose() {
         if (this.nativeHandle != 0L) {
            this.traceDispose();
            this.context.releaseD3DMeshView(this.nativeHandle);
            this.nativeHandle = 0L;
         }

      }
   }
}
