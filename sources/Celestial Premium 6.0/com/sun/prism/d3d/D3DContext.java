/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.GeneralTransform3D;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGDefaultCamera;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.MeshView;
import com.sun.prism.RTTexture;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.d3d.D3DFrameStats;
import com.sun.prism.d3d.D3DPipeline;
import com.sun.prism.d3d.D3DRenderTarget;
import com.sun.prism.d3d.D3DResourceFactory;
import com.sun.prism.d3d.D3DTexture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.ps.Shader;

class D3DContext
extends BaseShaderContext {
    public static final int D3DERR_DEVICEREMOVED = -2005530512;
    public static final int D3DERR_DEVICENOTRESET = -2005530519;
    public static final int D3DERR_DEVICELOST = -2005530520;
    public static final int E_FAIL = -2147467259;
    public static final int D3DERR_OUTOFVIDEOMEMORY = -2005532292;
    public static final int D3D_OK = 0;
    public static final int D3DCOMPMODE_CLEAR = 0;
    public static final int D3DCOMPMODE_SRC = 1;
    public static final int D3DCOMPMODE_SRCOVER = 2;
    public static final int D3DCOMPMODE_DSTOUT = 3;
    public static final int D3DCOMPMODE_ADD = 4;
    public static final int D3DTADDRESS_NOP = 0;
    public static final int D3DTADDRESS_WRAP = 1;
    public static final int D3DTADDRESS_MIRROR = 2;
    public static final int D3DTADDRESS_CLAMP = 3;
    public static final int D3DTADDRESS_BORDER = 4;
    public static final int CULL_BACK = 110;
    public static final int CULL_FRONT = 111;
    public static final int CULL_NONE = 112;
    private static GeneralTransform3D scratchTx = new GeneralTransform3D();
    private static final Affine3D scratchAffine3DTx = new Affine3D();
    private static double[] tempAdjustClipSpaceMat = new double[16];
    private BaseShaderContext.State state;
    private boolean isLost = false;
    private final long pContext;
    private Vec3d cameraPos = new Vec3d();
    private GeneralTransform3D projViewTx = new GeneralTransform3D();
    private int targetWidth = 0;
    private int targetHeight = 0;
    private final D3DResourceFactory factory;
    public static final int NUM_QUADS = PrismSettings.superShader ? 4096 : 256;

    public static boolean FAILED(int n) {
        return n < 0;
    }

    D3DContext(long l, Screen screen, D3DResourceFactory d3DResourceFactory) {
        super(screen, d3DResourceFactory, NUM_QUADS);
        this.pContext = l;
        this.factory = d3DResourceFactory;
    }

    @Override
    public D3DResourceFactory getResourceFactory() {
        return this.factory;
    }

    protected void initState() {
        this.init();
        this.state = new BaseShaderContext.State();
        D3DContext.validate(D3DContext.nSetBlendEnabled(this.pContext, 2));
        D3DContext.validate(D3DContext.nSetDeviceParametersFor2D(this.pContext));
    }

    long getContextHandle() {
        return this.pContext;
    }

    boolean isLost() {
        return this.isLost;
    }

    static void validate(int n) {
        if (PrismSettings.verbose && D3DContext.FAILED(n)) {
            System.err.println("D3D hresult failed :" + D3DContext.hResultToString(n));
            new Exception("Stack trace").printStackTrace(System.err);
        }
    }

    private void setLost() {
        this.isLost = true;
    }

    boolean testLostStateAndReset() {
        if (this.isDisposed()) {
            return false;
        }
        int n = D3DResourceFactory.nTestCooperativeLevel(this.pContext);
        if (PrismSettings.verbose && D3DContext.FAILED(n)) {
            System.err.print("D3DContext::testLostStateAndReset : ");
            switch (n) {
                case 0: {
                    System.err.println("D3D_OK");
                    break;
                }
                case -2005530520: {
                    System.err.println("D3DERR_DEVICELOST");
                    break;
                }
                case -2005530512: {
                    System.err.println("D3DERR_DEVICEREMOVED");
                    break;
                }
                case -2005530519: {
                    System.err.println("D3DERR_DEVICENOTRESET");
                    break;
                }
                case -2147467259: {
                    System.err.println("E_FAIL");
                    break;
                }
                default: {
                    System.err.println(String.format("Unknown D3D error 0x%x", n));
                }
            }
        }
        if (n == -2005530520) {
            this.setLost();
        }
        if (n == -2005530519) {
            boolean bl = this.isLost();
            this.setLost();
            this.disposeLCDBuffer();
            this.factory.notifyReset();
            n = D3DResourceFactory.nResetDevice(this.pContext);
            if (n == 0) {
                this.isLost = false;
                this.initState();
                if (!bl) {
                    return false;
                }
            }
        }
        if (n == -2005530512) {
            this.setLost();
            D3DPipeline.getInstance().reinitialize();
        }
        return !D3DContext.FAILED(n);
    }

    boolean validatePresent(int n) {
        if (n == -2005530520 || n == -2005530519) {
            this.setLost();
        } else {
            D3DContext.validate(n);
        }
        return !D3DContext.FAILED(n);
    }

    @Override
    public void dispose() {
        this.disposeLCDBuffer();
        this.state = null;
        super.dispose();
    }

    private GeneralTransform3D adjustClipSpace(GeneralTransform3D generalTransform3D) {
        double[] arrd = generalTransform3D.get(tempAdjustClipSpaceMat);
        arrd[8] = (arrd[8] + arrd[12]) / 2.0;
        arrd[9] = (arrd[9] + arrd[13]) / 2.0;
        arrd[10] = (arrd[10] + arrd[14]) / 2.0;
        arrd[11] = (arrd[11] + arrd[15]) / 2.0;
        generalTransform3D.set(arrd);
        return generalTransform3D;
    }

    @Override
    protected BaseShaderContext.State updateRenderTarget(RenderTarget renderTarget, NGCamera nGCamera, boolean bl) {
        if (this.checkDisposed()) {
            return null;
        }
        long l = ((D3DRenderTarget)((Object)renderTarget)).getResourceHandle();
        int n = D3DContext.nSetRenderTarget(this.pContext, l, bl, renderTarget.isMSAA());
        D3DContext.validate(n);
        if (n == 0) {
            this.resetLastClip(this.state);
        }
        this.targetWidth = renderTarget.getPhysicalWidth();
        this.targetHeight = renderTarget.getPhysicalHeight();
        if (nGCamera instanceof NGDefaultCamera) {
            ((NGDefaultCamera)nGCamera).validate(this.targetWidth, this.targetHeight);
            this.projViewTx = this.adjustClipSpace(nGCamera.getProjViewTx(this.projViewTx));
        } else {
            this.projViewTx = this.adjustClipSpace(nGCamera.getProjViewTx(this.projViewTx));
            double d = nGCamera.getViewWidth();
            double d2 = nGCamera.getViewHeight();
            if ((double)this.targetWidth != d || (double)this.targetHeight != d2) {
                this.projViewTx.scale(d / (double)this.targetWidth, d2 / (double)this.targetHeight, 1.0);
            }
        }
        n = D3DContext.nSetProjViewMatrix(this.pContext, bl, this.projViewTx.get(0), this.projViewTx.get(1), this.projViewTx.get(2), this.projViewTx.get(3), this.projViewTx.get(4), this.projViewTx.get(5), this.projViewTx.get(6), this.projViewTx.get(7), this.projViewTx.get(8), this.projViewTx.get(9), this.projViewTx.get(10), this.projViewTx.get(11), this.projViewTx.get(12), this.projViewTx.get(13), this.projViewTx.get(14), this.projViewTx.get(15));
        D3DContext.validate(n);
        this.cameraPos = nGCamera.getPositionInWorld(this.cameraPos);
        return this.state;
    }

    @Override
    protected void updateTexture(int n, Texture texture) {
        int n2;
        boolean bl;
        long l;
        if (texture != null) {
            D3DTexture d3DTexture = (D3DTexture)texture;
            l = d3DTexture.getNativeSourceHandle();
            bl = texture.getLinearFiltering();
            switch (texture.getWrapMode()) {
                case CLAMP_NOT_NEEDED: {
                    n2 = 0;
                    break;
                }
                case CLAMP_TO_EDGE: 
                case CLAMP_TO_EDGE_SIMULATED: 
                case CLAMP_TO_ZERO_SIMULATED: {
                    n2 = 3;
                    break;
                }
                case CLAMP_TO_ZERO: {
                    n2 = 4;
                    break;
                }
                case REPEAT: 
                case REPEAT_SIMULATED: {
                    n2 = 1;
                    break;
                }
                default: {
                    throw new InternalError("Unrecognized wrap mode: " + texture.getWrapMode());
                }
            }
        } else {
            l = 0L;
            bl = false;
            n2 = 3;
        }
        D3DContext.validate(D3DContext.nSetTexture(this.pContext, l, n, bl, n2));
    }

    @Override
    protected void updateShaderTransform(Shader shader, BaseTransform baseTransform) {
        int n;
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        GeneralTransform3D generalTransform3D = this.getPerspectiveTransformNoClone();
        if (baseTransform.isIdentity() && generalTransform3D.isIdentity()) {
            n = D3DContext.nResetTransform(this.pContext);
        } else if (generalTransform3D.isIdentity()) {
            n = D3DContext.nSetTransform(this.pContext, baseTransform.getMxx(), baseTransform.getMxy(), baseTransform.getMxz(), baseTransform.getMxt(), baseTransform.getMyx(), baseTransform.getMyy(), baseTransform.getMyz(), baseTransform.getMyt(), baseTransform.getMzx(), baseTransform.getMzy(), baseTransform.getMzz(), baseTransform.getMzt(), 0.0, 0.0, 0.0, 1.0);
        } else {
            scratchTx.setIdentity().mul(baseTransform).mul(generalTransform3D);
            n = D3DContext.nSetTransform(this.pContext, scratchTx.get(0), scratchTx.get(1), scratchTx.get(2), scratchTx.get(3), scratchTx.get(4), scratchTx.get(5), scratchTx.get(6), scratchTx.get(7), scratchTx.get(8), scratchTx.get(9), scratchTx.get(10), scratchTx.get(11), scratchTx.get(12), scratchTx.get(13), scratchTx.get(14), scratchTx.get(15));
        }
        D3DContext.validate(n);
    }

    @Override
    protected void updateWorldTransform(BaseTransform baseTransform) {
        if (baseTransform == null || baseTransform.isIdentity()) {
            D3DContext.nSetWorldTransformToIdentity(this.pContext);
        } else {
            D3DContext.nSetWorldTransform(this.pContext, baseTransform.getMxx(), baseTransform.getMxy(), baseTransform.getMxz(), baseTransform.getMxt(), baseTransform.getMyx(), baseTransform.getMyy(), baseTransform.getMyz(), baseTransform.getMyt(), baseTransform.getMzx(), baseTransform.getMzy(), baseTransform.getMzz(), baseTransform.getMzt(), 0.0, 0.0, 0.0, 1.0);
        }
    }

    @Override
    protected void updateClipRect(Rectangle rectangle) {
        int n;
        if (rectangle == null || rectangle.isEmpty()) {
            n = D3DContext.nResetClipRect(this.pContext);
        } else {
            int n2 = rectangle.x;
            int n3 = rectangle.y;
            int n4 = n2 + rectangle.width;
            int n5 = n3 + rectangle.height;
            n = D3DContext.nSetClipRect(this.pContext, n2, n3, n4, n5);
        }
        D3DContext.validate(n);
    }

    @Override
    protected void updateCompositeMode(CompositeMode compositeMode) {
        int n;
        switch (compositeMode) {
            case CLEAR: {
                n = 0;
                break;
            }
            case SRC: {
                n = 1;
                break;
            }
            case SRC_OVER: {
                n = 2;
                break;
            }
            case DST_OUT: {
                n = 3;
                break;
            }
            case ADD: {
                n = 4;
                break;
            }
            default: {
                throw new InternalError("Unrecognized composite mode: " + compositeMode);
            }
        }
        D3DContext.validate(D3DContext.nSetBlendEnabled(this.pContext, n));
    }

    D3DFrameStats getFrameStats(boolean bl, D3DFrameStats d3DFrameStats) {
        if (d3DFrameStats == null) {
            d3DFrameStats = new D3DFrameStats();
        }
        return D3DContext.nGetFrameStats(this.pContext, d3DFrameStats, bl) ? d3DFrameStats : null;
    }

    private static native int nSetRenderTarget(long var0, long var2, boolean var4, boolean var5);

    private static native int nSetTexture(long var0, long var2, int var4, boolean var5, int var6);

    private static native int nResetTransform(long var0);

    private static native int nSetTransform(long var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24, double var26, double var28, double var30, double var32);

    private static native void nSetWorldTransformToIdentity(long var0);

    private static native void nSetWorldTransform(long var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16, double var18, double var20, double var22, double var24, double var26, double var28, double var30, double var32);

    private static native int nSetCameraPosition(long var0, double var2, double var4, double var6);

    private static native int nSetProjViewMatrix(long var0, boolean var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29, double var31, double var33);

    private static native int nResetClipRect(long var0);

    private static native int nSetClipRect(long var0, int var2, int var3, int var4, int var5);

    private static native int nSetBlendEnabled(long var0, int var2);

    private static native int nSetDeviceParametersFor2D(long var0);

    private static native int nSetDeviceParametersFor3D(long var0);

    private static native long nCreateD3DMesh(long var0);

    private static native void nReleaseD3DMesh(long var0, long var2);

    private static native boolean nBuildNativeGeometryShort(long var0, long var2, float[] var4, int var5, short[] var6, int var7);

    private static native boolean nBuildNativeGeometryInt(long var0, long var2, float[] var4, int var5, int[] var6, int var7);

    private static native long nCreateD3DPhongMaterial(long var0);

    private static native void nReleaseD3DPhongMaterial(long var0, long var2);

    private static native void nSetDiffuseColor(long var0, long var2, float var4, float var5, float var6, float var7);

    private static native void nSetSpecularColor(long var0, long var2, boolean var4, float var5, float var6, float var7, float var8);

    private static native void nSetMap(long var0, long var2, int var4, long var5);

    private static native long nCreateD3DMeshView(long var0, long var2);

    private static native void nReleaseD3DMeshView(long var0, long var2);

    private static native void nSetCullingMode(long var0, long var2, int var4);

    private static native void nSetMaterial(long var0, long var2, long var4);

    private static native void nSetWireframe(long var0, long var2, boolean var4);

    private static native void nSetAmbientLight(long var0, long var2, float var4, float var5, float var6);

    private static native void nSetLight(long var0, long var2, int var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20, float var21);

    private static native void nRenderMeshView(long var0, long var2);

    private static native int nDrawIndexedQuads(long var0, float[] var2, byte[] var3, int var4);

    private static native void nBlit(long var0, long var2, long var4, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13);

    private static native boolean nGetFrameStats(long var0, D3DFrameStats var2, boolean var3);

    private static native boolean nIsRTTVolatile(long var0);

    public boolean isRTTVolatile() {
        if (this.checkDisposed()) {
            return false;
        }
        return D3DContext.nIsRTTVolatile(this.pContext);
    }

    public static String hResultToString(long l) {
        switch ((int)l) {
            case -2005530519: {
                return "D3DERR_DEVICENOTRESET";
            }
            case -2005530520: {
                return "D3DERR_DEVICELOST";
            }
            case -2005532292: {
                return "D3DERR_OUTOFVIDEOMEMORY";
            }
            case -2005530512: {
                return "D3DERR_DEVICEREMOVED";
            }
            case 0: {
                return "D3D_OK";
            }
        }
        return "D3D_ERROR " + Long.toHexString(l);
    }

    @Override
    public void setDeviceParametersFor2D() {
        if (this.checkDisposed()) {
            return;
        }
        D3DContext.nSetDeviceParametersFor2D(this.pContext);
    }

    @Override
    protected void setDeviceParametersFor3D() {
        if (this.checkDisposed()) {
            return;
        }
        D3DContext.nSetDeviceParametersFor3D(this.pContext);
    }

    long createD3DMesh() {
        if (this.checkDisposed()) {
            return 0L;
        }
        return D3DContext.nCreateD3DMesh(this.pContext);
    }

    void releaseD3DMesh(long l) {
        D3DContext.nReleaseD3DMesh(this.pContext, l);
    }

    boolean buildNativeGeometry(long l, float[] arrf, int n, short[] arrs, int n2) {
        return D3DContext.nBuildNativeGeometryShort(this.pContext, l, arrf, n, arrs, n2);
    }

    boolean buildNativeGeometry(long l, float[] arrf, int n, int[] arrn, int n2) {
        return D3DContext.nBuildNativeGeometryInt(this.pContext, l, arrf, n, arrn, n2);
    }

    long createD3DPhongMaterial() {
        return D3DContext.nCreateD3DPhongMaterial(this.pContext);
    }

    void releaseD3DPhongMaterial(long l) {
        D3DContext.nReleaseD3DPhongMaterial(this.pContext, l);
    }

    void setDiffuseColor(long l, float f, float f2, float f3, float f4) {
        D3DContext.nSetDiffuseColor(this.pContext, l, f, f2, f3, f4);
    }

    void setSpecularColor(long l, boolean bl, float f, float f2, float f3, float f4) {
        D3DContext.nSetSpecularColor(this.pContext, l, bl, f, f2, f3, f4);
    }

    void setMap(long l, int n, long l2) {
        D3DContext.nSetMap(this.pContext, l, n, l2);
    }

    long createD3DMeshView(long l) {
        return D3DContext.nCreateD3DMeshView(this.pContext, l);
    }

    void releaseD3DMeshView(long l) {
        D3DContext.nReleaseD3DMeshView(this.pContext, l);
    }

    void setCullingMode(long l, int n) {
        int n2;
        if (n == MeshView.CULL_NONE) {
            n2 = 112;
        } else if (n == MeshView.CULL_BACK) {
            n2 = 110;
        } else if (n == MeshView.CULL_FRONT) {
            n2 = 111;
        } else {
            throw new IllegalArgumentException("illegal value for CullMode: " + n);
        }
        D3DContext.nSetCullingMode(this.pContext, l, n2);
    }

    void setMaterial(long l, long l2) {
        D3DContext.nSetMaterial(this.pContext, l, l2);
    }

    void setWireframe(long l, boolean bl) {
        D3DContext.nSetWireframe(this.pContext, l, bl);
    }

    void setAmbientLight(long l, float f, float f2, float f3) {
        D3DContext.nSetAmbientLight(this.pContext, l, f, f2, f3);
    }

    void setLight(long l, int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17) {
        D3DContext.nSetLight(this.pContext, l, n, f, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17);
    }

    @Override
    protected void renderQuads(float[] arrf, byte[] arrby, int n) {
        int n2 = D3DContext.nDrawIndexedQuads(this.pContext, arrf, arrby, n);
        D3DContext.validate(n2);
    }

    void renderMeshView(long l, Graphics graphics) {
        scratchTx = scratchTx.set(this.projViewTx);
        float f = graphics.getPixelScaleFactorX();
        float f2 = graphics.getPixelScaleFactorY();
        if ((double)f != 1.0 || (double)f2 != 1.0) {
            scratchTx.scale(f, f2, 1.0);
        }
        int n = D3DContext.nSetProjViewMatrix(this.pContext, graphics.isDepthTest(), scratchTx.get(0), scratchTx.get(1), scratchTx.get(2), scratchTx.get(3), scratchTx.get(4), scratchTx.get(5), scratchTx.get(6), scratchTx.get(7), scratchTx.get(8), scratchTx.get(9), scratchTx.get(10), scratchTx.get(11), scratchTx.get(12), scratchTx.get(13), scratchTx.get(14), scratchTx.get(15));
        D3DContext.validate(n);
        n = D3DContext.nSetCameraPosition(this.pContext, this.cameraPos.x, this.cameraPos.y, this.cameraPos.z);
        D3DContext.validate(n);
        BaseTransform baseTransform = graphics.getTransformNoClone();
        if ((double)f != 1.0 || (double)f2 != 1.0) {
            scratchAffine3DTx.setToIdentity();
            scratchAffine3DTx.scale(1.0 / (double)f, 1.0 / (double)f2);
            scratchAffine3DTx.concatenate(baseTransform);
            this.updateWorldTransform(scratchAffine3DTx);
        } else {
            this.updateWorldTransform(baseTransform);
        }
        D3DContext.nRenderMeshView(this.pContext, l);
    }

    @Override
    public void blit(RTTexture rTTexture, RTTexture rTTexture2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        long l = rTTexture2 == null ? 0L : ((D3DTexture)((Object)rTTexture2)).getNativeSourceHandle();
        long l2 = ((D3DTexture)((Object)rTTexture)).getNativeSourceHandle();
        D3DContext.nBlit(this.pContext, l2, l, n, n2, n3, n4, n5, n6, n7, n8);
    }
}

