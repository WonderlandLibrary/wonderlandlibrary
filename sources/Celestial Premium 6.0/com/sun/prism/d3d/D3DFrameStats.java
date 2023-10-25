/*
 * Decompiled with CFR 0.150.
 */
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

    D3DFrameStats() {
    }

    static int divr(int n, int n2) {
        return (n + n2 / 2) / n2;
    }

    public String toDebugString(int n) {
        return "D3D Statistics per last " + n + " frame(s) :\n\tnumTrianglesDrawn=" + D3DFrameStats.divr(this.numTrianglesDrawn, n) + ", numDrawCalls=" + D3DFrameStats.divr(this.numDrawCalls, n) + ", numBufferLocks=" + D3DFrameStats.divr(this.numBufferLocks, n) + "\n\tnumTextureLocks=" + D3DFrameStats.divr(this.numTextureLocks, n) + ", numTextureTransferKBytes=" + D3DFrameStats.divr(this.numTextureTransferBytes / 1024, n) + "\n\tnumRenderTargetSwitch=" + D3DFrameStats.divr(this.numRenderTargetSwitch, n) + ", numSetTexture=" + D3DFrameStats.divr(this.numSetTexture, n) + ", numSetPixelShader=" + D3DFrameStats.divr(this.numSetPixelShader, n);
    }
}

