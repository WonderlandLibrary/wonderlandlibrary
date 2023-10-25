package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import optifine.Config;
import org.lwjgl.opengl.GL11;

public class GlStateManager {
  private static AlphaState alphaState = new AlphaState((GlStateManager$1)null);
  
  private static BooleanState lightingState = new BooleanState(2896);
  
  private static BooleanState[] lightState = new BooleanState[8];
  
  private static ColorMaterialState colorMaterialState = new ColorMaterialState((GlStateManager$1)null);
  
  private static BlendState blendState = new BlendState((GlStateManager$1)null);
  
  private static DepthState depthState = new DepthState((GlStateManager$1)null);
  
  private static FogState fogState = new FogState((GlStateManager$1)null);
  
  private static CullState cullState = new CullState((GlStateManager$1)null);
  
  private static PolygonOffsetState polygonOffsetState = new PolygonOffsetState((GlStateManager$1)null);
  
  private static ColorLogicState colorLogicState = new ColorLogicState((GlStateManager$1)null);
  
  private static TexGenState texGenState = new TexGenState((GlStateManager$1)null);
  
  private static ClearState clearState = new ClearState((GlStateManager$1)null);
  
  private static StencilState stencilState = new StencilState((GlStateManager$1)null);
  
  private static BooleanState normalizeState = new BooleanState(2977);
  
  private static int activeTextureUnit = 0;
  
  private static TextureState[] textureState = new TextureState[32];
  
  private static int activeShadeModel = 7425;
  
  private static BooleanState rescaleNormalState = new BooleanState(32826);
  
  private static ColorMask colorMaskState = new ColorMask((GlStateManager$1)null);
  
  private static Color colorState = new Color();
  
  private static final String __OBFID = "CL_00002558";
  
  public static boolean clearEnabled = true;
  
  public static void pushAttrib() {
    GL11.glPushAttrib(8256);
  }
  
  public static void popAttrib() {
    GL11.glPopAttrib();
  }
  
  public static void disableAlpha() {
    alphaState.field_179208_a.setDisabled();
  }
  
  public static void enableAlpha() {
    alphaState.field_179208_a.setEnabled();
  }
  
  public static void alphaFunc(int func, float ref) {
    if (func != alphaState.func || ref != alphaState.ref) {
      alphaState.func = func;
      alphaState.ref = ref;
      GL11.glAlphaFunc(func, ref);
    } 
  }
  
  public static void enableLighting() {
    lightingState.setEnabled();
  }
  
  public static void disableLighting() {
    lightingState.setDisabled();
  }
  
  public static void enableLight(int light) {
    lightState[light].setEnabled();
  }
  
  public static void disableLight(int light) {
    lightState[light].setDisabled();
  }
  
  public static void enableColorMaterial() {
    colorMaterialState.field_179191_a.setEnabled();
  }
  
  public static void disableColorMaterial() {
    colorMaterialState.field_179191_a.setDisabled();
  }
  
  public static void colorMaterial(int face, int mode) {
    if (face != colorMaterialState.field_179189_b || mode != colorMaterialState.field_179190_c) {
      colorMaterialState.field_179189_b = face;
      colorMaterialState.field_179190_c = mode;
      GL11.glColorMaterial(face, mode);
    } 
  }
  
  public static void disableDepth() {
    depthState.depthTest.setDisabled();
  }
  
  public static void enableDepth() {
    depthState.depthTest.setEnabled();
  }
  
  public static void depthFunc(int depthFunc) {
    if (depthFunc != depthState.depthFunc) {
      depthState.depthFunc = depthFunc;
      GL11.glDepthFunc(depthFunc);
    } 
  }
  
  public static void depthMask(boolean flagIn) {
    if (flagIn != depthState.maskEnabled) {
      depthState.maskEnabled = flagIn;
      GL11.glDepthMask(flagIn);
    } 
  }
  
  public static void disableBlend() {
    blendState.field_179213_a.setDisabled();
  }
  
  public static void enableBlend() {
    blendState.field_179213_a.setEnabled();
  }
  
  public static void blendFunc(int srcFactor, int dstFactor) {
    if (srcFactor != blendState.srcFactor || dstFactor != blendState.dstFactor) {
      blendState.srcFactor = srcFactor;
      blendState.dstFactor = dstFactor;
      GL11.glBlendFunc(srcFactor, dstFactor);
    } 
  }
  
  public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
    if (srcFactor != blendState.srcFactor || dstFactor != blendState.dstFactor || srcFactorAlpha != blendState.srcFactorAlpha || dstFactorAlpha != blendState.dstFactorAlpha) {
      blendState.srcFactor = srcFactor;
      blendState.dstFactor = dstFactor;
      blendState.srcFactorAlpha = srcFactorAlpha;
      blendState.dstFactorAlpha = dstFactorAlpha;
      OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
    } 
  }
  
  public static void enableFog() {
    fogState.field_179049_a.setEnabled();
  }
  
  public static void disableFog() {
    fogState.field_179049_a.setDisabled();
  }
  
  public static void setFog(int param) {
    if (param != fogState.field_179047_b) {
      fogState.field_179047_b = param;
      GL11.glFogi(2917, param);
    } 
  }
  
  public static void setFogDensity(float param) {
    if (param != fogState.field_179048_c) {
      fogState.field_179048_c = param;
      GL11.glFogf(2914, param);
    } 
  }
  
  public static void setFogStart(float param) {
    if (param != fogState.field_179045_d) {
      fogState.field_179045_d = param;
      GL11.glFogf(2915, param);
    } 
  }
  
  public static void setFogEnd(float param) {
    if (param != fogState.field_179046_e) {
      fogState.field_179046_e = param;
      GL11.glFogf(2916, param);
    } 
  }
  
  public static void enableCull() {
    cullState.field_179054_a.setEnabled();
  }
  
  public static void disableCull() {
    cullState.field_179054_a.setDisabled();
  }
  
  public static void cullFace(int mode) {
    if (mode != cullState.field_179053_b) {
      cullState.field_179053_b = mode;
      GL11.glCullFace(mode);
    } 
  }
  
  public static void enablePolygonOffset() {
    polygonOffsetState.field_179044_a.setEnabled();
  }
  
  public static void disablePolygonOffset() {
    polygonOffsetState.field_179044_a.setDisabled();
  }
  
  public static void doPolygonOffset(float factor, float units) {
    if (factor != polygonOffsetState.field_179043_c || units != polygonOffsetState.field_179041_d) {
      polygonOffsetState.field_179043_c = factor;
      polygonOffsetState.field_179041_d = units;
      GL11.glPolygonOffset(factor, units);
    } 
  }
  
  public static void enableColorLogic() {
    colorLogicState.field_179197_a.setEnabled();
  }
  
  public static void disableColorLogic() {
    colorLogicState.field_179197_a.setDisabled();
  }
  
  public static void colorLogicOp(int opcode) {
    if (opcode != colorLogicState.field_179196_b) {
      colorLogicState.field_179196_b = opcode;
      GL11.glLogicOp(opcode);
    } 
  }
  
  public static void enableTexGenCoord(TexGen p_179087_0_) {
    (texGenCoord(p_179087_0_)).field_179067_a.setEnabled();
  }
  
  public static void disableTexGenCoord(TexGen p_179100_0_) {
    (texGenCoord(p_179100_0_)).field_179067_a.setDisabled();
  }
  
  public static void texGen(TexGen p_179149_0_, int p_179149_1_) {
    TexGenCoord glstatemanager$texgencoord = texGenCoord(p_179149_0_);
    if (p_179149_1_ != glstatemanager$texgencoord.field_179066_c) {
      glstatemanager$texgencoord.field_179066_c = p_179149_1_;
      GL11.glTexGeni(glstatemanager$texgencoord.field_179065_b, 9472, p_179149_1_);
    } 
  }
  
  public static void func_179105_a(TexGen p_179105_0_, int pname, FloatBuffer params) {
    GL11.glTexGen((texGenCoord(p_179105_0_)).field_179065_b, pname, params);
  }
  
  private static TexGenCoord texGenCoord(TexGen p_179125_0_) {
    switch (GlStateManager$1.field_179175_a[p_179125_0_.ordinal()]) {
      case 1:
        return texGenState.field_179064_a;
      case 2:
        return texGenState.field_179062_b;
      case 3:
        return texGenState.field_179063_c;
      case 4:
        return texGenState.field_179061_d;
    } 
    return texGenState.field_179064_a;
  }
  
  public static void setActiveTexture(int texture) {
    if (activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
      activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
      OpenGlHelper.setActiveTexture(texture);
    } 
  }
  
  public static void enableTexture2D() {
    (textureState[activeTextureUnit]).texture2DState.setEnabled();
  }
  
  public static void disableTexture2D() {
    (textureState[activeTextureUnit]).texture2DState.setDisabled();
  }
  
  public static int generateTexture() {
    return GL11.glGenTextures();
  }
  
  public static void deleteTexture(int texture) {
    if (texture != 0) {
      GL11.glDeleteTextures(texture);
      for (TextureState glstatemanager$texturestate : textureState) {
        if (glstatemanager$texturestate.textureName == texture)
          glstatemanager$texturestate.textureName = 0; 
      } 
    } 
  }
  
  public static void bindTexture(int texture) {
    if (texture != (textureState[activeTextureUnit]).textureName) {
      (textureState[activeTextureUnit]).textureName = texture;
      GL11.glBindTexture(3553, texture);
    } 
  }
  
  public static void bindCurrentTexture() {
    GL11.glBindTexture(3553, (textureState[activeTextureUnit]).textureName);
  }
  
  public static void enableNormalize() {
    normalizeState.setEnabled();
  }
  
  public static void disableNormalize() {
    normalizeState.setDisabled();
  }
  
  public static void shadeModel(int mode) {
    if (mode != activeShadeModel) {
      activeShadeModel = mode;
      GL11.glShadeModel(mode);
    } 
  }
  
  public static void enableRescaleNormal() {
    rescaleNormalState.setEnabled();
  }
  
  public static void disableRescaleNormal() {
    rescaleNormalState.setDisabled();
  }
  
  public static void viewport(int x, int y, int width, int height) {
    GL11.glViewport(x, y, width, height);
  }
  
  public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    if (red != colorMaskState.red || green != colorMaskState.green || blue != colorMaskState.blue || alpha != colorMaskState.alpha) {
      colorMaskState.red = red;
      colorMaskState.green = green;
      colorMaskState.blue = blue;
      colorMaskState.alpha = alpha;
      GL11.glColorMask(red, green, blue, alpha);
    } 
  }
  
  public static void clearDepth(double depth) {
    if (depth != clearState.field_179205_a) {
      clearState.field_179205_a = depth;
      GL11.glClearDepth(depth);
    } 
  }
  
  public static void clearColor(float red, float green, float blue, float alpha) {
    if (red != clearState.field_179203_b.red || green != clearState.field_179203_b.green || blue != clearState.field_179203_b.blue || alpha != clearState.field_179203_b.alpha) {
      clearState.field_179203_b.red = red;
      clearState.field_179203_b.green = green;
      clearState.field_179203_b.blue = blue;
      clearState.field_179203_b.alpha = alpha;
      GL11.glClearColor(red, green, blue, alpha);
    } 
  }
  
  public static void clear(int mask) {
    if (clearEnabled)
      GL11.glClear(mask); 
  }
  
  public static void matrixMode(int mode) {
    GL11.glMatrixMode(mode);
  }
  
  public static void loadIdentity() {
    GL11.glLoadIdentity();
  }
  
  public static void pushMatrix() {
    GL11.glPushMatrix();
  }
  
  public static void popMatrix() {
    GL11.glPopMatrix();
  }
  
  public static void getFloat(int pname, FloatBuffer params) {
    GL11.glGetFloat(pname, params);
  }
  
  public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar) {
    GL11.glOrtho(left, right, bottom, top, zNear, zFar);
  }
  
  public static void rotate(float angle, float x, float y, float z) {
    GL11.glRotatef(angle, x, y, z);
  }
  
  public static void scale(float x, float y, float z) {
    GL11.glScalef(x, y, z);
  }
  
  public static void scale(double x, double y, double z) {
    GL11.glScaled(x, y, z);
  }
  
  public static void translate(float x, float y, float z) {
    GL11.glTranslatef(x, y, z);
  }
  
  public static void translate(double x, double y, double z) {
    GL11.glTranslated(x, y, z);
  }
  
  public static void multMatrix(FloatBuffer matrix) {
    GL11.glMultMatrix(matrix);
  }
  
  public static void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
    if (colorRed != colorState.red || colorGreen != colorState.green || colorBlue != colorState.blue || colorAlpha != colorState.alpha) {
      colorState.red = colorRed;
      colorState.green = colorGreen;
      colorState.blue = colorBlue;
      colorState.alpha = colorAlpha;
      GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
    } 
  }
  
  public static void color(float colorRed, float colorGreen, float colorBlue) {
    color(colorRed, colorGreen, colorBlue, 1.0F);
  }
  
  public static void resetColor() {
    colorState.red = colorState.green = colorState.blue = colorState.alpha = -1.0F;
  }
  
  public static void callList(int list) {
    GL11.glCallList(list);
  }
  
  public static int getActiveTextureUnit() {
    return OpenGlHelper.defaultTexUnit + activeTextureUnit;
  }
  
  public static int getBoundTexture() {
    return (textureState[activeTextureUnit]).textureName;
  }
  
  public static void checkBoundTexture() {
    if (Config.isMinecraftThread()) {
      int i = GL11.glGetInteger(34016);
      int j = GL11.glGetInteger(32873);
      int k = getActiveTextureUnit();
      int l = getBoundTexture();
      if (l > 0)
        if (i != k || j != l)
          Config.dbg("checkTexture: act: " + k + ", glAct: " + i + ", tex: " + l + ", glTex: " + j);  
    } 
  }
  
  public static void deleteTextures(IntBuffer p_deleteTextures_0_) {
    p_deleteTextures_0_.rewind();
    while (p_deleteTextures_0_.position() < p_deleteTextures_0_.limit()) {
      int i = p_deleteTextures_0_.get();
      deleteTexture(i);
    } 
    p_deleteTextures_0_.rewind();
  }
  
  static {
    for (int i = 0; i < 8; i++)
      lightState[i] = new BooleanState(16384 + i); 
    for (int j = 0; j < textureState.length; j++)
      textureState[j] = new TextureState((GlStateManager$1)null); 
  }
  
  static final class GlStateManager$1 {
    static final int[] field_179175_a = new int[(GlStateManager.TexGen.values()).length];
    
    private static final String __OBFID = "CL_00002557";
    
    static {
      try {
        field_179175_a[GlStateManager.TexGen.S.ordinal()] = 1;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179175_a[GlStateManager.TexGen.T.ordinal()] = 2;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179175_a[GlStateManager.TexGen.R.ordinal()] = 3;
      } catch (NoSuchFieldError noSuchFieldError) {}
      try {
        field_179175_a[GlStateManager.TexGen.Q.ordinal()] = 4;
      } catch (NoSuchFieldError noSuchFieldError) {}
    }
  }
  
  static class AlphaState {
    public GlStateManager.BooleanState field_179208_a = new GlStateManager.BooleanState(3008);
    
    public int func = 519;
    
    public float ref = -1.0F;
    
    private static final String __OBFID = "CL_00002556";
    
    private AlphaState() {}
    
    AlphaState(GlStateManager.GlStateManager$1 p_i46489_1_) {
      this();
    }
  }
  
  static class BlendState {
    public GlStateManager.BooleanState field_179213_a = new GlStateManager.BooleanState(3042);
    
    public int srcFactor = 1;
    
    public int dstFactor = 0;
    
    public int srcFactorAlpha = 1;
    
    public int dstFactorAlpha = 0;
    
    private static final String __OBFID = "CL_00002555";
    
    private BlendState() {}
    
    BlendState(GlStateManager.GlStateManager$1 p_i46488_1_) {
      this();
    }
  }
  
  static class BooleanState {
    private final int capability;
    
    private boolean currentState = false;
    
    private static final String __OBFID = "CL_00002554";
    
    public BooleanState(int capabilityIn) {
      this.capability = capabilityIn;
    }
    
    public void setDisabled() {
      setState(false);
    }
    
    public void setEnabled() {
      setState(true);
    }
    
    public void setState(boolean state) {
      if (state != this.currentState) {
        this.currentState = state;
        if (state) {
          GL11.glEnable(this.capability);
        } else {
          GL11.glDisable(this.capability);
        } 
      } 
    }
  }
  
  static class ClearState {
    public double field_179205_a = 1.0D;
    
    public GlStateManager.Color field_179203_b = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);
    
    public int field_179204_c = 0;
    
    private static final String __OBFID = "CL_00002553";
    
    private ClearState() {}
    
    ClearState(GlStateManager.GlStateManager$1 p_i46487_1_) {
      this();
    }
  }
  
  static class Color {
    public float red = 1.0F;
    
    public float green = 1.0F;
    
    public float blue = 1.0F;
    
    public float alpha = 1.0F;
    
    private static final String __OBFID = "CL_00002552";
    
    public Color() {}
    
    public Color(float redIn, float greenIn, float blueIn, float alphaIn) {
      this.red = redIn;
      this.green = greenIn;
      this.blue = blueIn;
      this.alpha = alphaIn;
    }
  }
  
  static class ColorLogicState {
    public GlStateManager.BooleanState field_179197_a = new GlStateManager.BooleanState(3058);
    
    public int field_179196_b = 5379;
    
    private static final String __OBFID = "CL_00002551";
    
    private ColorLogicState() {}
    
    ColorLogicState(GlStateManager.GlStateManager$1 p_i46486_1_) {
      this();
    }
  }
  
  static class ColorMask {
    public boolean red = true;
    
    public boolean green = true;
    
    public boolean blue = true;
    
    public boolean alpha = true;
    
    private static final String __OBFID = "CL_00002550";
    
    private ColorMask() {}
    
    ColorMask(GlStateManager.GlStateManager$1 p_i46485_1_) {
      this();
    }
  }
  
  static class ColorMaterialState {
    public GlStateManager.BooleanState field_179191_a = new GlStateManager.BooleanState(2903);
    
    public int field_179189_b = 1032;
    
    public int field_179190_c = 5634;
    
    private static final String __OBFID = "CL_00002549";
    
    private ColorMaterialState() {}
    
    ColorMaterialState(GlStateManager.GlStateManager$1 p_i46484_1_) {
      this();
    }
  }
  
  static class CullState {
    public GlStateManager.BooleanState field_179054_a = new GlStateManager.BooleanState(2884);
    
    public int field_179053_b = 1029;
    
    private static final String __OBFID = "CL_00002548";
    
    private CullState() {}
    
    CullState(GlStateManager.GlStateManager$1 p_i46483_1_) {
      this();
    }
  }
  
  static class DepthState {
    public GlStateManager.BooleanState depthTest = new GlStateManager.BooleanState(2929);
    
    public boolean maskEnabled = true;
    
    public int depthFunc = 513;
    
    private static final String __OBFID = "CL_00002547";
    
    private DepthState() {}
    
    DepthState(GlStateManager.GlStateManager$1 p_i46482_1_) {
      this();
    }
  }
  
  static class FogState {
    public GlStateManager.BooleanState field_179049_a = new GlStateManager.BooleanState(2912);
    
    public int field_179047_b = 2048;
    
    public float field_179048_c = 1.0F;
    
    public float field_179045_d = 0.0F;
    
    public float field_179046_e = 1.0F;
    
    private static final String __OBFID = "CL_00002546";
    
    private FogState() {}
    
    FogState(GlStateManager.GlStateManager$1 p_i46481_1_) {
      this();
    }
  }
  
  static class PolygonOffsetState {
    public GlStateManager.BooleanState field_179044_a = new GlStateManager.BooleanState(32823);
    
    public GlStateManager.BooleanState field_179042_b = new GlStateManager.BooleanState(10754);
    
    public float field_179043_c = 0.0F;
    
    public float field_179041_d = 0.0F;
    
    private static final String __OBFID = "CL_00002545";
    
    private PolygonOffsetState() {}
    
    PolygonOffsetState(GlStateManager.GlStateManager$1 p_i46480_1_) {
      this();
    }
  }
  
  static class StencilFunc {
    public int field_179081_a = 519;
    
    public int field_179079_b = 0;
    
    public int field_179080_c = -1;
    
    private static final String __OBFID = "CL_00002544";
    
    private StencilFunc() {}
    
    StencilFunc(GlStateManager.GlStateManager$1 p_i46479_1_) {
      this();
    }
  }
  
  static class StencilState {
    public GlStateManager.StencilFunc field_179078_a = new GlStateManager.StencilFunc((GlStateManager.GlStateManager$1)null);
    
    public int field_179076_b = -1;
    
    public int field_179077_c = 7680;
    
    public int field_179074_d = 7680;
    
    public int field_179075_e = 7680;
    
    private static final String __OBFID = "CL_00002543";
    
    private StencilState() {}
    
    StencilState(GlStateManager.GlStateManager$1 p_i46478_1_) {
      this();
    }
  }
  
  public enum TexGen {
    S("S", 0),
    T("T", 1),
    R("R", 2),
    Q("Q", 3);
    
    private static final TexGen[] $VALUES = new TexGen[] { S, T, R, Q };
    
    private static final String __OBFID = "CL_00002542";
    
    static {
    
    }
  }
  
  static class TexGenCoord {
    public GlStateManager.BooleanState field_179067_a;
    
    public int field_179065_b;
    
    public int field_179066_c = -1;
    
    private static final String __OBFID = "CL_00002541";
    
    public TexGenCoord(int p_i46254_1_, int p_i46254_2_) {
      this.field_179065_b = p_i46254_1_;
      this.field_179067_a = new GlStateManager.BooleanState(p_i46254_2_);
    }
  }
  
  static class TexGenState {
    public GlStateManager.TexGenCoord field_179064_a = new GlStateManager.TexGenCoord(8192, 3168);
    
    public GlStateManager.TexGenCoord field_179062_b = new GlStateManager.TexGenCoord(8193, 3169);
    
    public GlStateManager.TexGenCoord field_179063_c = new GlStateManager.TexGenCoord(8194, 3170);
    
    public GlStateManager.TexGenCoord field_179061_d = new GlStateManager.TexGenCoord(8195, 3171);
    
    private static final String __OBFID = "CL_00002540";
    
    private TexGenState() {}
    
    TexGenState(GlStateManager.GlStateManager$1 p_i46477_1_) {
      this();
    }
  }
  
  static class TextureState {
    public GlStateManager.BooleanState texture2DState = new GlStateManager.BooleanState(3553);
    
    public int textureName = 0;
    
    private static final String __OBFID = "CL_00002539";
    
    private TextureState() {}
    
    TextureState(GlStateManager.GlStateManager$1 p_i46476_1_) {
      this();
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\GlStateManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */