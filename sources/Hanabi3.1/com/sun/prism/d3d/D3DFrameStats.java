package com.sun.prism.d3d;

class D3DFrameStats {
   public int numTrianglesDrawn;
   public int numDrawCalls;
   public int numBufferLocks;
   public int numTextureLocks;
   public int numTextureTransferBytes;
   public int numSetTexture;
   public int numSetPixelShader;
   public int numRenderTargetSwitch;

   static int divr(int var0, int var1) {
      return (var0 + var1 / 2) / var1;
   }

   public String toDebugString(int var1) {
      return "D3D Statistics per last " + var1 + " frame(s) :\n\tnumTrianglesDrawn=" + divr(this.numTrianglesDrawn, var1) + ", numDrawCalls=" + divr(this.numDrawCalls, var1) + ", numBufferLocks=" + divr(this.numBufferLocks, var1) + "\n\tnumTextureLocks=" + divr(this.numTextureLocks, var1) + ", numTextureTransferKBytes=" + divr(this.numTextureTransferBytes / 1024, var1) + "\n\tnumRenderTargetSwitch=" + divr(this.numRenderTargetSwitch, var1) + ", numSetTexture=" + divr(this.numSetTexture, var1) + ", numSetPixelShader=" + divr(this.numSetPixelShader, var1);
   }
}
