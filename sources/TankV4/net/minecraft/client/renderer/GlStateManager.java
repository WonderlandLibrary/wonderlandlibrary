package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import optifine.Config;
import org.lwjgl.opengl.GL11;

public class GlStateManager {
   private static GlStateManager.BooleanState[] lightState = new GlStateManager.BooleanState[8];
   public static boolean clearEnabled = true;
   private static GlStateManager.FogState fogState = new GlStateManager.FogState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.TextureState[] textureState = new GlStateManager.TextureState[32];
   private static GlStateManager.BlendState blendState = new GlStateManager.BlendState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.StencilState stencilState = new GlStateManager.StencilState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.ColorLogicState colorLogicState = new GlStateManager.ColorLogicState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.Color colorState = new GlStateManager.Color();
   private static int activeShadeModel = 7425;
   private static GlStateManager.BooleanState lightingState = new GlStateManager.BooleanState(2896);
   private static GlStateManager.PolygonOffsetState polygonOffsetState = new GlStateManager.PolygonOffsetState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.BooleanState rescaleNormalState = new GlStateManager.BooleanState(32826);
   private static GlStateManager.ClearState clearState = new GlStateManager.ClearState((GlStateManager.GlStateManager$1)null);
   private static int activeTextureUnit = 0;
   private static GlStateManager.DepthState depthState = new GlStateManager.DepthState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.AlphaState alphaState = new GlStateManager.AlphaState((GlStateManager.GlStateManager$1)null);
   private static final String __OBFID = "CL_00002558";
   private static GlStateManager.CullState cullState = new GlStateManager.CullState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.ColorMask colorMaskState = new GlStateManager.ColorMask((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.ColorMaterialState colorMaterialState = new GlStateManager.ColorMaterialState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.TexGenState texGenState = new GlStateManager.TexGenState((GlStateManager.GlStateManager$1)null);
   private static GlStateManager.BooleanState normalizeState = new GlStateManager.BooleanState(2977);

   public static int generateTexture() {
      return GL11.glGenTextures();
   }

   public static void disableLighting() {
      lightingState.setDisabled();
   }

   public static void disableLight(int var0) {
      lightState[var0].setDisabled();
   }

   public static int getActiveTextureUnit() {
      return OpenGlHelper.defaultTexUnit + activeTextureUnit;
   }

   public static void viewport(int var0, int var1, int var2, int var3) {
      GL11.glViewport(var0, var1, var2, var3);
   }

   public static void disableRescaleNormal() {
      rescaleNormalState.setDisabled();
   }

   public static void disableColorMaterial() {
      colorMaterialState.field_179191_a.setDisabled();
   }

   public static void disableCull() {
      cullState.field_179054_a.setDisabled();
   }

   public static void setFogStart(float var0) {
      if (var0 != fogState.field_179045_d) {
         fogState.field_179045_d = var0;
         GL11.glFogf(2915, var0);
      }

   }

   public static void disablePolygonOffset() {
      polygonOffsetState.field_179044_a.setDisabled();
   }

   public static void enableAlpha() {
      alphaState.field_179208_a.setEnabled();
   }

   public static void blendFunc(int var0, int var1) {
      if (var0 != blendState.srcFactor || var1 != blendState.dstFactor) {
         blendState.srcFactor = var0;
         blendState.dstFactor = var1;
         GL11.glBlendFunc(var0, var1);
      }

   }

   private static GlStateManager.TexGenCoord texGenCoord(GlStateManager.TexGen var0) {
      switch(var0) {
      case S:
         return texGenState.field_179064_a;
      case T:
         return texGenState.field_179062_b;
      case R:
         return texGenState.field_179063_c;
      case Q:
         return texGenState.field_179061_d;
      default:
         return texGenState.field_179064_a;
      }
   }

   public static void pushMatrix() {
      GL11.glPushMatrix();
   }

   public static void enableDepth() {
      depthState.depthTest.setEnabled();
   }

   static {
      int var0;
      for(var0 = 0; var0 < 8; ++var0) {
         lightState[var0] = new GlStateManager.BooleanState(16384 + var0);
      }

      for(var0 = 0; var0 < textureState.length; ++var0) {
         textureState[var0] = new GlStateManager.TextureState((GlStateManager.GlStateManager$1)null);
      }

   }

   public static void disableFog() {
      fogState.field_179049_a.setDisabled();
   }

   public static void bindTexture(int var0) {
      if (var0 != textureState[activeTextureUnit].textureName) {
         textureState[activeTextureUnit].textureName = var0;
         GL11.glBindTexture(3553, var0);
      }

   }

   public static void enableLight(int var0) {
      lightState[var0].setEnabled();
   }

   public static void colorMask(boolean var0, boolean var1, boolean var2, boolean var3) {
      if (var0 != colorMaskState.red || var1 != colorMaskState.green || var2 != colorMaskState.blue || var3 != colorMaskState.alpha) {
         colorMaskState.red = var0;
         colorMaskState.green = var1;
         colorMaskState.blue = var2;
         colorMaskState.alpha = var3;
         GL11.glColorMask(var0, var1, var2, var3);
      }

   }

   public static void clear(int var0) {
      if (clearEnabled) {
         GL11.glClear(var0);
      }

   }

   public static void doPolygonOffset(float var0, float var1) {
      if (var0 != polygonOffsetState.field_179043_c || var1 != polygonOffsetState.field_179041_d) {
         polygonOffsetState.field_179043_c = var0;
         polygonOffsetState.field_179041_d = var1;
         GL11.glPolygonOffset(var0, var1);
      }

   }

   public static int getBoundTexture() {
      return textureState[activeTextureUnit].textureName;
   }

   public static void multMatrix(FloatBuffer var0) {
      GL11.glMultMatrix(var0);
   }

   public static void setFogEnd(float var0) {
      if (var0 != fogState.field_179046_e) {
         fogState.field_179046_e = var0;
         GL11.glFogf(2916, var0);
      }

   }

   public static void colorLogicOp(int var0) {
      if (var0 != colorLogicState.field_179196_b) {
         colorLogicState.field_179196_b = var0;
         GL11.glLogicOp(var0);
      }

   }

   public static void enableColorLogic() {
      colorLogicState.field_179197_a.setEnabled();
   }

   public static void enableFog() {
      fogState.field_179049_a.setEnabled();
   }

   public static void enableColorMaterial() {
      colorMaterialState.field_179191_a.setEnabled();
   }

   public static void disableBlend() {
      blendState.field_179213_a.setDisabled();
   }

   public static void disableTexture2D() {
      textureState[activeTextureUnit].texture2DState.setDisabled();
   }

   public static void pushAttrib() {
      GL11.glPushAttrib(8256);
   }

   public static void popMatrix() {
      GL11.glPopMatrix();
   }

   public static void deleteTextures(IntBuffer var0) {
      var0.rewind();

      while(var0.position() < var0.limit()) {
         int var1 = var0.get();
         deleteTexture(var1);
      }

      var0.rewind();
   }

   public static void popAttrib() {
      GL11.glPopAttrib();
   }

   public static void disableAlpha() {
      alphaState.field_179208_a.setDisabled();
   }

   public static void enableRescaleNormal() {
      rescaleNormalState.setEnabled();
   }

   public static void clearDepth(double var0) {
      if (var0 != clearState.field_179205_a) {
         clearState.field_179205_a = var0;
         GL11.glClearDepth(var0);
      }

   }

   public static void translate(float var0, float var1, float var2) {
      GL11.glTranslatef(var0, var1, var2);
   }

   public static void alphaFunc(int var0, float var1) {
      if (var0 != alphaState.func || var1 != alphaState.ref) {
         alphaState.func = var0;
         alphaState.ref = var1;
         GL11.glAlphaFunc(var0, var1);
      }

   }

   public static void setFog(int var0) {
      if (var0 != fogState.field_179047_b) {
         fogState.field_179047_b = var0;
         GL11.glFogi(2917, var0);
      }

   }

   public static void texGen(GlStateManager.TexGen var0, int var1) {
      GlStateManager.TexGenCoord var2 = texGenCoord(var0);
      if (var1 != var2.field_179066_c) {
         var2.field_179066_c = var1;
         GL11.glTexGeni(var2.field_179065_b, 9472, var1);
      }

   }

   public static void scale(float var0, float var1, float var2) {
      GL11.glScalef(var0, var1, var2);
   }

   public static void cullFace(int var0) {
      if (var0 != cullState.field_179053_b) {
         cullState.field_179053_b = var0;
         GL11.glCullFace(var0);
      }

   }

   public static void enableCull() {
      cullState.field_179054_a.setEnabled();
   }

   public static void enableBlend() {
      blendState.field_179213_a.setEnabled();
   }

   public static void color(float var0, float var1, float var2) {
      color(var0, var1, var2, 1.0F);
   }

   public static void clearColor(float var0, float var1, float var2, float var3) {
      if (var0 != clearState.field_179203_b.red || var1 != clearState.field_179203_b.green || var2 != clearState.field_179203_b.blue || var3 != clearState.field_179203_b.alpha) {
         clearState.field_179203_b.red = var0;
         clearState.field_179203_b.green = var1;
         clearState.field_179203_b.blue = var2;
         clearState.field_179203_b.alpha = var3;
         GL11.glClearColor(var0, var1, var2, var3);
      }

   }

   public static void resetColor() {
      colorState.red = colorState.green = colorState.blue = colorState.alpha = -1.0F;
   }

   public static void getFloat(int var0, FloatBuffer var1) {
      GL11.glGetFloat(var0, var1);
   }

   public static void ortho(double var0, double var2, double var4, double var6, double var8, double var10) {
      GL11.glOrtho(var0, var2, var4, var6, var8, var10);
   }

   public static void disableDepth() {
      depthState.depthTest.setDisabled();
   }

   public static void setActiveTexture(int var0) {
      if (activeTextureUnit != var0 - OpenGlHelper.defaultTexUnit) {
         activeTextureUnit = var0 - OpenGlHelper.defaultTexUnit;
         OpenGlHelper.setActiveTexture(var0);
      }

   }

   public static void checkBoundTexture() {
      if (Config.isMinecraftThread()) {
         int var0 = GL11.glGetInteger(34016);
         int var1 = GL11.glGetInteger(32873);
         int var2 = getActiveTextureUnit();
         int var3 = getBoundTexture();
         if (var3 > 0 && (var0 != var2 || var1 != var3)) {
            Config.dbg("checkTexture: act: " + var2 + ", glAct: " + var0 + ", tex: " + var3 + ", glTex: " + var1);
         }
      }

   }

   public static void enableTexGenCoord(GlStateManager.TexGen var0) {
      texGenCoord(var0).field_179067_a.setEnabled();
   }

   public static void tryBlendFuncSeparate(int var0, int var1, int var2, int var3) {
      if (var0 != blendState.srcFactor || var1 != blendState.dstFactor || var2 != blendState.srcFactorAlpha || var3 != blendState.dstFactorAlpha) {
         blendState.srcFactor = var0;
         blendState.dstFactor = var1;
         blendState.srcFactorAlpha = var2;
         blendState.dstFactorAlpha = var3;
         OpenGlHelper.glBlendFunc(var0, var1, var2, var3);
      }

   }

   public static void depthMask(boolean var0) {
      if (var0 != depthState.maskEnabled) {
         depthState.maskEnabled = var0;
         GL11.glDepthMask(var0);
      }

   }

   public static void enablePolygonOffset() {
      polygonOffsetState.field_179044_a.setEnabled();
   }

   public static void scale(double var0, double var2, double var4) {
      GL11.glScaled(var0, var2, var4);
   }

   public static void loadIdentity() {
      GL11.glLoadIdentity();
   }

   public static void callList(int var0) {
      GL11.glCallList(var0);
   }

   public static void deleteTexture(int var0) {
      if (var0 != 0) {
         GL11.glDeleteTextures(var0);
         GlStateManager.TextureState[] var4;
         int var3 = (var4 = textureState).length;

         for(int var2 = 0; var2 < var3; ++var2) {
            GlStateManager.TextureState var1 = var4[var2];
            if (var1.textureName == var0) {
               var1.textureName = 0;
            }
         }
      }

   }

   public static void enableTexture2D() {
      textureState[activeTextureUnit].texture2DState.setEnabled();
   }

   public static void depthFunc(int var0) {
      if (var0 != depthState.depthFunc) {
         depthState.depthFunc = var0;
         GL11.glDepthFunc(var0);
      }

   }

   public static void setFogDensity(float var0) {
      if (var0 != fogState.field_179048_c) {
         fogState.field_179048_c = var0;
         GL11.glFogf(2914, var0);
      }

   }

   public static void translate(double var0, double var2, double var4) {
      GL11.glTranslated(var0, var2, var4);
   }

   public static void colorMaterial(int var0, int var1) {
      if (var0 != colorMaterialState.field_179189_b || var1 != colorMaterialState.field_179190_c) {
         colorMaterialState.field_179189_b = var0;
         colorMaterialState.field_179190_c = var1;
         GL11.glColorMaterial(var0, var1);
      }

   }

   public static void func_179105_a(GlStateManager.TexGen var0, int var1, FloatBuffer var2) {
      GL11.glTexGen(texGenCoord(var0).field_179065_b, var1, var2);
   }

   public static void matrixMode(int var0) {
      GL11.glMatrixMode(var0);
   }

   public static void enableNormalize() {
      normalizeState.setEnabled();
   }

   public static void disableColorLogic() {
      colorLogicState.field_179197_a.setDisabled();
   }

   public static void disableTexGenCoord(GlStateManager.TexGen var0) {
      texGenCoord(var0).field_179067_a.setDisabled();
   }

   public static void enableLighting() {
      lightingState.setEnabled();
   }

   public static void shadeModel(int var0) {
      if (var0 != activeShadeModel) {
         activeShadeModel = var0;
         GL11.glShadeModel(var0);
      }

   }

   public static void bindCurrentTexture() {
      GL11.glBindTexture(3553, textureState[activeTextureUnit].textureName);
   }

   public static void color(float var0, float var1, float var2, float var3) {
      if (var0 != colorState.red || var1 != colorState.green || var2 != colorState.blue || var3 != colorState.alpha) {
         colorState.red = var0;
         colorState.green = var1;
         colorState.blue = var2;
         colorState.alpha = var3;
         GL11.glColor4f(var0, var1, var2, var3);
      }

   }

   public static void disableNormalize() {
      normalizeState.setDisabled();
   }

   public static void rotate(float var0, float var1, float var2, float var3) {
      GL11.glRotatef(var0, var1, var2, var3);
   }

   static class AlphaState {
      private static final String __OBFID = "CL_00002556";
      public int func;
      public GlStateManager.BooleanState field_179208_a;
      public float ref;

      AlphaState(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private AlphaState() {
         this.field_179208_a = new GlStateManager.BooleanState(3008);
         this.func = 519;
         this.ref = -1.0F;
      }
   }

   static class DepthState {
      private static final String __OBFID = "CL_00002547";
      public GlStateManager.BooleanState depthTest;
      public int depthFunc;
      public boolean maskEnabled;

      private DepthState() {
         this.depthTest = new GlStateManager.BooleanState(2929);
         this.maskEnabled = true;
         this.depthFunc = 513;
      }

      DepthState(GlStateManager.GlStateManager$1 var1) {
         this();
      }
   }

   static class PolygonOffsetState {
      public GlStateManager.BooleanState field_179042_b;
      private static final String __OBFID = "CL_00002545";
      public float field_179043_c;
      public float field_179041_d;
      public GlStateManager.BooleanState field_179044_a;

      PolygonOffsetState(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private PolygonOffsetState() {
         this.field_179044_a = new GlStateManager.BooleanState(32823);
         this.field_179042_b = new GlStateManager.BooleanState(10754);
         this.field_179043_c = 0.0F;
         this.field_179041_d = 0.0F;
      }
   }

   static class TexGenState {
      public GlStateManager.TexGenCoord field_179064_a;
      private static final String __OBFID = "CL_00002540";
      public GlStateManager.TexGenCoord field_179062_b;
      public GlStateManager.TexGenCoord field_179063_c;
      public GlStateManager.TexGenCoord field_179061_d;

      TexGenState(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private TexGenState() {
         this.field_179064_a = new GlStateManager.TexGenCoord(8192, 3168);
         this.field_179062_b = new GlStateManager.TexGenCoord(8193, 3169);
         this.field_179063_c = new GlStateManager.TexGenCoord(8194, 3170);
         this.field_179061_d = new GlStateManager.TexGenCoord(8195, 3171);
      }
   }

   static class ColorLogicState {
      public int field_179196_b;
      public GlStateManager.BooleanState field_179197_a;
      private static final String __OBFID = "CL_00002551";

      ColorLogicState(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private ColorLogicState() {
         this.field_179197_a = new GlStateManager.BooleanState(3058);
         this.field_179196_b = 5379;
      }
   }

   static class BlendState {
      public int srcFactorAlpha;
      public int dstFactor;
      private static final String __OBFID = "CL_00002555";
      public GlStateManager.BooleanState field_179213_a;
      public int srcFactor;
      public int dstFactorAlpha;

      BlendState(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private BlendState() {
         this.field_179213_a = new GlStateManager.BooleanState(3042);
         this.srcFactor = 1;
         this.dstFactor = 0;
         this.srcFactorAlpha = 1;
         this.dstFactorAlpha = 0;
      }
   }

   static class FogState {
      public int field_179047_b;
      public float field_179045_d;
      public GlStateManager.BooleanState field_179049_a;
      public float field_179048_c;
      private static final String __OBFID = "CL_00002546";
      public float field_179046_e;

      FogState(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private FogState() {
         this.field_179049_a = new GlStateManager.BooleanState(2912);
         this.field_179047_b = 2048;
         this.field_179048_c = 1.0F;
         this.field_179045_d = 0.0F;
         this.field_179046_e = 1.0F;
      }
   }

   static final class GlStateManager$1 {
      private static final String __OBFID = "CL_00002557";
      static final int[] field_179175_a = new int[GlStateManager.TexGen.values().length];

      static {
         try {
            field_179175_a[GlStateManager.TexGen.S.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
         }

         try {
            field_179175_a[GlStateManager.TexGen.T.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
         }

         try {
            field_179175_a[GlStateManager.TexGen.R.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
         }

         try {
            field_179175_a[GlStateManager.TexGen.Q.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
         }

      }
   }

   static class StencilFunc {
      private static final String __OBFID = "CL_00002544";
      public int field_179080_c;
      public int field_179081_a;
      public int field_179079_b;

      private StencilFunc() {
         this.field_179081_a = 519;
         this.field_179079_b = 0;
         this.field_179080_c = -1;
      }

      StencilFunc(GlStateManager.GlStateManager$1 var1) {
         this();
      }
   }

   static class StencilState {
      public int field_179074_d;
      public GlStateManager.StencilFunc field_179078_a;
      public int field_179076_b;
      private static final String __OBFID = "CL_00002543";
      public int field_179077_c;
      public int field_179075_e;

      StencilState(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private StencilState() {
         this.field_179078_a = new GlStateManager.StencilFunc((GlStateManager.GlStateManager$1)null);
         this.field_179076_b = -1;
         this.field_179077_c = 7680;
         this.field_179074_d = 7680;
         this.field_179075_e = 7680;
      }
   }

   static class ClearState {
      private static final String __OBFID = "CL_00002553";
      public int field_179204_c;
      public double field_179205_a;
      public GlStateManager.Color field_179203_b;

      private ClearState() {
         this.field_179205_a = 1.0D;
         this.field_179203_b = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);
         this.field_179204_c = 0;
      }

      ClearState(GlStateManager.GlStateManager$1 var1) {
         this();
      }
   }

   static class TextureState {
      public GlStateManager.BooleanState texture2DState;
      public int textureName;
      private static final String __OBFID = "CL_00002539";

      TextureState(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private TextureState() {
         this.texture2DState = new GlStateManager.BooleanState(3553);
         this.textureName = 0;
      }
   }

   public static enum TexGen {
      S("S", 0),
      Q("Q", 3);

      private static final GlStateManager.TexGen[] $VALUES = new GlStateManager.TexGen[]{S, T, R, Q};
      private static final GlStateManager.TexGen[] ENUM$VALUES = new GlStateManager.TexGen[]{S, T, R, Q};
      T("T", 1);

      private static final String __OBFID = "CL_00002542";
      R("R", 2);

      private TexGen(String var3, int var4) {
      }
   }

   static class BooleanState {
      private static final String __OBFID = "CL_00002554";
      private final int capability;
      private boolean currentState = false;

      public void setEnabled() {
         this.setState(true);
      }

      public void setState(boolean var1) {
         if (var1 != this.currentState) {
            this.currentState = var1;
            if (var1) {
               GL11.glEnable(this.capability);
            } else {
               GL11.glDisable(this.capability);
            }
         }

      }

      public BooleanState(int var1) {
         this.capability = var1;
      }

      public void setDisabled() {
         this.setState(false);
      }
   }

   static class ColorMaterialState {
      public GlStateManager.BooleanState field_179191_a;
      public int field_179189_b;
      public int field_179190_c;
      private static final String __OBFID = "CL_00002549";

      private ColorMaterialState() {
         this.field_179191_a = new GlStateManager.BooleanState(2903);
         this.field_179189_b = 1032;
         this.field_179190_c = 5634;
      }

      ColorMaterialState(GlStateManager.GlStateManager$1 var1) {
         this();
      }
   }

   static class ColorMask {
      public boolean green;
      public boolean red;
      public boolean alpha;
      private static final String __OBFID = "CL_00002550";
      public boolean blue;

      ColorMask(GlStateManager.GlStateManager$1 var1) {
         this();
      }

      private ColorMask() {
         this.red = true;
         this.green = true;
         this.blue = true;
         this.alpha = true;
      }
   }

   static class TexGenCoord {
      public int field_179066_c = -1;
      public GlStateManager.BooleanState field_179067_a;
      public int field_179065_b;
      private static final String __OBFID = "CL_00002541";

      public TexGenCoord(int var1, int var2) {
         this.field_179065_b = var1;
         this.field_179067_a = new GlStateManager.BooleanState(var2);
      }
   }

   static class CullState {
      public GlStateManager.BooleanState field_179054_a;
      private static final String __OBFID = "CL_00002548";
      public int field_179053_b;

      private CullState() {
         this.field_179054_a = new GlStateManager.BooleanState(2884);
         this.field_179053_b = 1029;
      }

      CullState(GlStateManager.GlStateManager$1 var1) {
         this();
      }
   }

   static class Color {
      public float red = 1.0F;
      public float green = 1.0F;
      private static final String __OBFID = "CL_00002552";
      public float alpha = 1.0F;
      public float blue = 1.0F;

      public Color(float var1, float var2, float var3, float var4) {
         this.red = var1;
         this.green = var2;
         this.blue = var3;
         this.alpha = var4;
      }

      public Color() {
      }
   }
}
