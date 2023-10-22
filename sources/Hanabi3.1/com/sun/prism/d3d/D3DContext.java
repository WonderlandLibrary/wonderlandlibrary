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
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.ps.Shader;

class D3DContext extends BaseShaderContext {
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
   public static final int NUM_QUADS;

   public static boolean FAILED(int var0) {
      return var0 < 0;
   }

   D3DContext(long var1, Screen var3, D3DResourceFactory var4) {
      super(var3, var4, NUM_QUADS);
      this.pContext = var1;
      this.factory = var4;
   }

   public D3DResourceFactory getResourceFactory() {
      return this.factory;
   }

   protected void initState() {
      this.init();
      this.state = new BaseShaderContext.State();
      validate(nSetBlendEnabled(this.pContext, 2));
      validate(nSetDeviceParametersFor2D(this.pContext));
   }

   long getContextHandle() {
      return this.pContext;
   }

   boolean isLost() {
      return this.isLost;
   }

   static void validate(int var0) {
      if (PrismSettings.verbose && FAILED(var0)) {
         System.out.println("D3D hresult failed :" + hResultToString((long)var0));
         (new Exception("Stack trace")).printStackTrace(System.out);
      }

   }

   private void setLost() {
      this.isLost = true;
   }

   boolean testLostStateAndReset() {
      int var1 = D3DResourceFactory.nTestCooperativeLevel(this.pContext);
      if (var1 == -2005530520) {
         this.setLost();
      }

      if (var1 == -2005530519) {
         boolean var2 = this.isLost();
         this.setLost();
         this.disposeLCDBuffer();
         this.factory.notifyReset();
         var1 = D3DResourceFactory.nResetDevice(this.pContext);
         if (var1 == 0) {
            this.isLost = false;
            this.initState();
            if (!var2) {
               return false;
            }
         }
      }

      return !FAILED(var1);
   }

   boolean validatePresent(int var1) {
      if (var1 != -2005530520 && var1 != -2005530519) {
         validate(var1);
      } else {
         this.setLost();
      }

      return !FAILED(var1);
   }

   private GeneralTransform3D adjustClipSpace(GeneralTransform3D var1) {
      double[] var2 = var1.get(tempAdjustClipSpaceMat);
      var2[8] = (var2[8] + var2[12]) / 2.0;
      var2[9] = (var2[9] + var2[13]) / 2.0;
      var2[10] = (var2[10] + var2[14]) / 2.0;
      var2[11] = (var2[11] + var2[15]) / 2.0;
      var1.set(var2);
      return var1;
   }

   protected BaseShaderContext.State updateRenderTarget(RenderTarget var1, NGCamera var2, boolean var3) {
      long var4 = ((D3DRenderTarget)var1).getResourceHandle();
      int var6 = nSetRenderTarget(this.pContext, var4, var3, var1.isMSAA());
      validate(var6);
      if (var6 == 0) {
         this.resetLastClip(this.state);
      }

      this.targetWidth = var1.getPhysicalWidth();
      this.targetHeight = var1.getPhysicalHeight();
      if (var2 instanceof NGDefaultCamera) {
         ((NGDefaultCamera)var2).validate(this.targetWidth, this.targetHeight);
         this.projViewTx = this.adjustClipSpace(var2.getProjViewTx(this.projViewTx));
      } else {
         this.projViewTx = this.adjustClipSpace(var2.getProjViewTx(this.projViewTx));
         double var7 = var2.getViewWidth();
         double var9 = var2.getViewHeight();
         if ((double)this.targetWidth != var7 || (double)this.targetHeight != var9) {
            this.projViewTx.scale(var7 / (double)this.targetWidth, var9 / (double)this.targetHeight, 1.0);
         }
      }

      var6 = nSetProjViewMatrix(this.pContext, var3, this.projViewTx.get(0), this.projViewTx.get(1), this.projViewTx.get(2), this.projViewTx.get(3), this.projViewTx.get(4), this.projViewTx.get(5), this.projViewTx.get(6), this.projViewTx.get(7), this.projViewTx.get(8), this.projViewTx.get(9), this.projViewTx.get(10), this.projViewTx.get(11), this.projViewTx.get(12), this.projViewTx.get(13), this.projViewTx.get(14), this.projViewTx.get(15));
      validate(var6);
      this.cameraPos = var2.getPositionInWorld(this.cameraPos);
      return this.state;
   }

   protected void updateTexture(int var1, Texture var2) {
      long var3;
      boolean var5;
      byte var6;
      if (var2 != null) {
         D3DTexture var7 = (D3DTexture)var2;
         var3 = var7.getNativeSourceHandle();
         var5 = var2.getLinearFiltering();
         switch (var2.getWrapMode()) {
            case CLAMP_NOT_NEEDED:
               var6 = 0;
               break;
            case CLAMP_TO_EDGE:
            case CLAMP_TO_EDGE_SIMULATED:
            case CLAMP_TO_ZERO_SIMULATED:
               var6 = 3;
               break;
            case CLAMP_TO_ZERO:
               var6 = 4;
               break;
            case REPEAT:
            case REPEAT_SIMULATED:
               var6 = 1;
               break;
            default:
               throw new InternalError("Unrecognized wrap mode: " + var2.getWrapMode());
         }
      } else {
         var3 = 0L;
         var5 = false;
         var6 = 3;
      }

      validate(nSetTexture(this.pContext, var3, var1, var5, var6));
   }

   protected void updateShaderTransform(Shader var1, BaseTransform var2) {
      if (var2 == null) {
         var2 = BaseTransform.IDENTITY_TRANSFORM;
      }

      GeneralTransform3D var3 = this.getPerspectiveTransformNoClone();
      int var4;
      if (var2.isIdentity() && var3.isIdentity()) {
         var4 = nResetTransform(this.pContext);
      } else if (var3.isIdentity()) {
         var4 = nSetTransform(this.pContext, var2.getMxx(), var2.getMxy(), var2.getMxz(), var2.getMxt(), var2.getMyx(), var2.getMyy(), var2.getMyz(), var2.getMyt(), var2.getMzx(), var2.getMzy(), var2.getMzz(), var2.getMzt(), 0.0, 0.0, 0.0, 1.0);
      } else {
         scratchTx.setIdentity().mul(var2).mul(var3);
         var4 = nSetTransform(this.pContext, scratchTx.get(0), scratchTx.get(1), scratchTx.get(2), scratchTx.get(3), scratchTx.get(4), scratchTx.get(5), scratchTx.get(6), scratchTx.get(7), scratchTx.get(8), scratchTx.get(9), scratchTx.get(10), scratchTx.get(11), scratchTx.get(12), scratchTx.get(13), scratchTx.get(14), scratchTx.get(15));
      }

      validate(var4);
   }

   protected void updateWorldTransform(BaseTransform var1) {
      if (var1 != null && !var1.isIdentity()) {
         nSetWorldTransform(this.pContext, var1.getMxx(), var1.getMxy(), var1.getMxz(), var1.getMxt(), var1.getMyx(), var1.getMyy(), var1.getMyz(), var1.getMyt(), var1.getMzx(), var1.getMzy(), var1.getMzz(), var1.getMzt(), 0.0, 0.0, 0.0, 1.0);
      } else {
         nSetWorldTransformToIdentity(this.pContext);
      }

   }

   protected void updateClipRect(Rectangle var1) {
      int var2;
      if (var1 != null && !var1.isEmpty()) {
         int var3 = var1.x;
         int var4 = var1.y;
         int var5 = var3 + var1.width;
         int var6 = var4 + var1.height;
         var2 = nSetClipRect(this.pContext, var3, var4, var5, var6);
      } else {
         var2 = nResetClipRect(this.pContext);
      }

      validate(var2);
   }

   protected void updateCompositeMode(CompositeMode var1) {
      byte var2;
      switch (var1) {
         case CLEAR:
            var2 = 0;
            break;
         case SRC:
            var2 = 1;
            break;
         case SRC_OVER:
            var2 = 2;
            break;
         case DST_OUT:
            var2 = 3;
            break;
         case ADD:
            var2 = 4;
            break;
         default:
            throw new InternalError("Unrecognized composite mode: " + var1);
      }

      validate(nSetBlendEnabled(this.pContext, var2));
   }

   D3DFrameStats getFrameStats(boolean var1, D3DFrameStats var2) {
      if (var2 == null) {
         var2 = new D3DFrameStats();
      }

      return nGetFrameStats(this.pContext, var2, var1) ? var2 : null;
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

   private static native void nSetPointLight(long var0, long var2, int var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11);

   private static native void nRenderMeshView(long var0, long var2);

   private static native int nDrawIndexedQuads(long var0, float[] var2, byte[] var3, int var4);

   private static native void nBlit(long var0, long var2, long var4, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13);

   private static native boolean nGetFrameStats(long var0, D3DFrameStats var2, boolean var3);

   private static native boolean nIsRTTVolatile(long var0);

   public boolean isRTTVolatile() {
      return nIsRTTVolatile(this.pContext);
   }

   public static String hResultToString(long var0) {
      switch ((int)var0) {
         case -2005532292:
            return "D3DERR_OUTOFVIDEOMEMORY";
         case -2005530520:
            return "D3DERR_DEVICELOST";
         case -2005530519:
            return "D3DERR_DEVICENOTRESET";
         case 0:
            return "D3D_OK";
         default:
            return "D3D_ERROR " + Long.toHexString(var0);
      }
   }

   public void setDeviceParametersFor2D() {
      nSetDeviceParametersFor2D(this.pContext);
   }

   protected void setDeviceParametersFor3D() {
      nSetDeviceParametersFor3D(this.pContext);
   }

   long createD3DMesh() {
      return nCreateD3DMesh(this.pContext);
   }

   void releaseD3DMesh(long var1) {
      nReleaseD3DMesh(this.pContext, var1);
   }

   boolean buildNativeGeometry(long var1, float[] var3, int var4, short[] var5, int var6) {
      return nBuildNativeGeometryShort(this.pContext, var1, var3, var4, var5, var6);
   }

   boolean buildNativeGeometry(long var1, float[] var3, int var4, int[] var5, int var6) {
      return nBuildNativeGeometryInt(this.pContext, var1, var3, var4, var5, var6);
   }

   long createD3DPhongMaterial() {
      return nCreateD3DPhongMaterial(this.pContext);
   }

   void releaseD3DPhongMaterial(long var1) {
      nReleaseD3DPhongMaterial(this.pContext, var1);
   }

   void setDiffuseColor(long var1, float var3, float var4, float var5, float var6) {
      nSetDiffuseColor(this.pContext, var1, var3, var4, var5, var6);
   }

   void setSpecularColor(long var1, boolean var3, float var4, float var5, float var6, float var7) {
      nSetSpecularColor(this.pContext, var1, var3, var4, var5, var6, var7);
   }

   void setMap(long var1, int var3, long var4) {
      nSetMap(this.pContext, var1, var3, var4);
   }

   long createD3DMeshView(long var1) {
      return nCreateD3DMeshView(this.pContext, var1);
   }

   void releaseD3DMeshView(long var1) {
      nReleaseD3DMeshView(this.pContext, var1);
   }

   void setCullingMode(long var1, int var3) {
      byte var4;
      if (var3 == MeshView.CULL_NONE) {
         var4 = 112;
      } else if (var3 == MeshView.CULL_BACK) {
         var4 = 110;
      } else {
         if (var3 != MeshView.CULL_FRONT) {
            throw new IllegalArgumentException("illegal value for CullMode: " + var3);
         }

         var4 = 111;
      }

      nSetCullingMode(this.pContext, var1, var4);
   }

   void setMaterial(long var1, long var3) {
      nSetMaterial(this.pContext, var1, var3);
   }

   void setWireframe(long var1, boolean var3) {
      nSetWireframe(this.pContext, var1, var3);
   }

   void setAmbientLight(long var1, float var3, float var4, float var5) {
      nSetAmbientLight(this.pContext, var1, var3, var4, var5);
   }

   void setPointLight(long var1, int var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      nSetPointLight(this.pContext, var1, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   protected void renderQuads(float[] var1, byte[] var2, int var3) {
      int var4 = nDrawIndexedQuads(this.pContext, var1, var2, var3);
      validate(var4);
   }

   void renderMeshView(long var1, Graphics var3) {
      scratchTx = scratchTx.set(this.projViewTx);
      float var4 = var3.getPixelScaleFactorX();
      float var5 = var3.getPixelScaleFactorY();
      if ((double)var4 != 1.0 || (double)var5 != 1.0) {
         scratchTx.scale((double)var4, (double)var5, 1.0);
      }

      int var6 = nSetProjViewMatrix(this.pContext, var3.isDepthTest(), scratchTx.get(0), scratchTx.get(1), scratchTx.get(2), scratchTx.get(3), scratchTx.get(4), scratchTx.get(5), scratchTx.get(6), scratchTx.get(7), scratchTx.get(8), scratchTx.get(9), scratchTx.get(10), scratchTx.get(11), scratchTx.get(12), scratchTx.get(13), scratchTx.get(14), scratchTx.get(15));
      validate(var6);
      var6 = nSetCameraPosition(this.pContext, this.cameraPos.x, this.cameraPos.y, this.cameraPos.z);
      validate(var6);
      BaseTransform var7 = var3.getTransformNoClone();
      if ((double)var4 == 1.0 && (double)var5 == 1.0) {
         this.updateWorldTransform(var7);
      } else {
         scratchAffine3DTx.setToIdentity();
         scratchAffine3DTx.scale(1.0 / (double)var4, 1.0 / (double)var5);
         scratchAffine3DTx.concatenate(var7);
         this.updateWorldTransform(scratchAffine3DTx);
      }

      nRenderMeshView(this.pContext, var1);
   }

   public void blit(RTTexture var1, RTTexture var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      long var11 = var2 == null ? 0L : ((D3DTexture)var2).getNativeSourceHandle();
      long var13 = ((D3DTexture)var1).getNativeSourceHandle();
      nBlit(this.pContext, var13, var11, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   static {
      NUM_QUADS = PrismSettings.superShader ? 4096 : 256;
   }
}
