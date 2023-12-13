package com.sun.prism.d3d;

import com.sun.prism.impl.BaseMesh;
import com.sun.prism.impl.Disposer;

class D3DMesh extends BaseMesh {
   static int count = 0;
   private final D3DContext context;
   private final long nativeHandle;

   private D3DMesh(D3DContext var1, long var2, Disposer.Record var4) {
      super(var4);
      this.context = var1;
      this.nativeHandle = var2;
      ++count;
   }

   static D3DMesh create(D3DContext var0) {
      long var1 = var0.createD3DMesh();
      return new D3DMesh(var0, var1, new D3DMeshDisposerRecord(var0, var1));
   }

   long getNativeHandle() {
      return this.nativeHandle;
   }

   public void dispose() {
      this.disposerRecord.dispose();
      --count;
   }

   public int getCount() {
      return count;
   }

   public boolean buildNativeGeometry(float[] var1, int var2, int[] var3, int var4) {
      return this.context.buildNativeGeometry(this.nativeHandle, var1, var2, var3, var4);
   }

   public boolean buildNativeGeometry(float[] var1, int var2, short[] var3, int var4) {
      return this.context.buildNativeGeometry(this.nativeHandle, var1, var2, var3, var4);
   }

   static class D3DMeshDisposerRecord implements Disposer.Record {
      private final D3DContext context;
      private long nativeHandle;

      D3DMeshDisposerRecord(D3DContext var1, long var2) {
         this.context = var1;
         this.nativeHandle = var2;
      }

      void traceDispose() {
      }

      public void dispose() {
         if (this.nativeHandle != 0L) {
            this.traceDispose();
            this.context.releaseD3DMesh(this.nativeHandle);
            this.nativeHandle = 0L;
         }

      }
   }
}
