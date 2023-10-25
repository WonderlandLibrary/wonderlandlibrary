/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class GlStateManager {
    private static AlphaState alphaState = new AlphaState();
    private static BooleanState normalizeState;
    private static CullState cullState;
    private static int activeShadeModel;
    private static BooleanState[] lightState;
    private static FogState fogState;
    private static ColorLogicState colorLogicState;
    private static BooleanState rescaleNormalState;
    private static DepthState depthState;
    private static TextureState[] textureState;
    private static PolygonOffsetState polygonOffsetState;
    private static BooleanState lightingState;
    private static TexGenState texGenState;
    private static ClearState clearState;
    private static Color colorState;
    private static ColorMask colorMaskState;
    private static BlendState blendState;
    private static ColorMaterialState colorMaterialState;
    private static StencilState stencilState;
    private static int activeTextureUnit;

    public static void colorLogicOp(int n) {
        if (n != GlStateManager.colorLogicState.field_179196_b) {
            GlStateManager.colorLogicState.field_179196_b = n;
            GL11.glLogicOp((int)n);
        }
    }

    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setEnabled();
    }

    public static void disableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setDisabled();
    }

    public static void depthMask(boolean bl) {
        if (bl != GlStateManager.depthState.maskEnabled) {
            GlStateManager.depthState.maskEnabled = bl;
            GL11.glDepthMask((boolean)bl);
        }
    }

    public static void popMatrix() {
        GL11.glPopMatrix();
    }

    public static void loadIdentity() {
        GL11.glLoadIdentity();
    }

    public static void disableBlend() {
        GlStateManager.blendState.field_179213_a.setDisabled();
    }

    public static void viewport(int n, int n2, int n3, int n4) {
        GL11.glViewport((int)n, (int)n2, (int)n3, (int)n4);
    }

    public static void colorMask(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        if (bl != GlStateManager.colorMaskState.red || bl2 != GlStateManager.colorMaskState.green || bl3 != GlStateManager.colorMaskState.blue || bl4 != GlStateManager.colorMaskState.alpha) {
            GlStateManager.colorMaskState.red = bl;
            GlStateManager.colorMaskState.green = bl2;
            GlStateManager.colorMaskState.blue = bl3;
            GlStateManager.colorMaskState.alpha = bl4;
            GL11.glColorMask((boolean)bl, (boolean)bl2, (boolean)bl3, (boolean)bl4);
        }
    }

    public static void disableFog() {
        GlStateManager.fogState.field_179049_a.setDisabled();
    }

    public static void shadeModel(int n) {
        if (n != activeShadeModel) {
            activeShadeModel = n;
            GL11.glShadeModel((int)n);
        }
    }

    public static void deleteTexture(int n) {
        GL11.glDeleteTextures((int)n);
        TextureState[] textureStateArray = textureState;
        int n2 = textureState.length;
        int n3 = 0;
        while (n3 < n2) {
            TextureState textureState = textureStateArray[n3];
            if (textureState.textureName == n) {
                textureState.textureName = -1;
            }
            ++n3;
        }
    }

    public static void rotate(float f, float f2, float f3, float f4) {
        GL11.glRotatef((float)f, (float)f2, (float)f3, (float)f4);
    }

    public static void setFogEnd(float f) {
        if (f != GlStateManager.fogState.field_179046_e) {
            GlStateManager.fogState.field_179046_e = f;
            GL11.glFogf((int)2916, (float)f);
        }
    }

    public static void tryBlendFuncSeparate(int n, int n2, int n3, int n4) {
        if (n != GlStateManager.blendState.srcFactor || n2 != GlStateManager.blendState.dstFactor || n3 != GlStateManager.blendState.srcFactorAlpha || n4 != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = n;
            GlStateManager.blendState.dstFactor = n2;
            GlStateManager.blendState.srcFactorAlpha = n3;
            GlStateManager.blendState.dstFactorAlpha = n4;
            OpenGlHelper.glBlendFunc(n, n2, n3, n4);
        }
    }

    public static void scale(double d, double d2, double d3) {
        GL11.glScaled((double)d, (double)d2, (double)d3);
    }

    public static void popAttrib() {
        GL11.glPopAttrib();
    }

    public static void multMatrix(FloatBuffer floatBuffer) {
        GL11.glMultMatrix((FloatBuffer)floatBuffer);
    }

    public static void clearColor(float f, float f2, float f3, float f4) {
        if (f != GlStateManager.clearState.field_179203_b.red || f2 != GlStateManager.clearState.field_179203_b.green || f3 != GlStateManager.clearState.field_179203_b.blue || f4 != GlStateManager.clearState.field_179203_b.alpha) {
            GlStateManager.clearState.field_179203_b.red = f;
            GlStateManager.clearState.field_179203_b.green = f2;
            GlStateManager.clearState.field_179203_b.blue = f3;
            GlStateManager.clearState.field_179203_b.alpha = f4;
            GL11.glClearColor((float)f, (float)f2, (float)f3, (float)f4);
        }
    }

    public static void matrixMode(int n) {
        GL11.glMatrixMode((int)n);
    }

    public static void disableTexGenCoord(TexGen texGen) {
        GlStateManager.texGenCoord((TexGen)texGen).field_179067_a.setDisabled();
    }

    public static void blendFunc(int n, int n2) {
        if (n != GlStateManager.blendState.srcFactor || n2 != GlStateManager.blendState.dstFactor) {
            GlStateManager.blendState.srcFactor = n;
            GlStateManager.blendState.dstFactor = n2;
            GL11.glBlendFunc((int)n, (int)n2);
        }
    }

    public static void pushAttrib() {
        GL11.glPushAttrib((int)8256);
    }

    public static void translate(double d, double d2, double d3) {
        GL11.glTranslated((double)d, (double)d2, (double)d3);
    }

    public static void resetColor() {
        GlStateManager.colorState.alpha = -1.0f;
        GlStateManager.colorState.blue = -1.0f;
        GlStateManager.colorState.green = -1.0f;
        GlStateManager.colorState.red = -1.0f;
    }

    public static void pushMatrix() {
        GL11.glPushMatrix();
    }

    public static void alphaFunc(int n, float f) {
        if (n != GlStateManager.alphaState.func || f != GlStateManager.alphaState.ref) {
            GlStateManager.alphaState.func = n;
            GlStateManager.alphaState.ref = f;
            GL11.glAlphaFunc((int)n, (float)f);
        }
    }

    private static TexGenCoord texGenCoord(TexGen texGen) {
        switch (texGen) {
            case S: {
                return GlStateManager.texGenState.field_179064_a;
            }
            case T: {
                return GlStateManager.texGenState.field_179062_b;
            }
            case R: {
                return GlStateManager.texGenState.field_179063_c;
            }
            case Q: {
                return GlStateManager.texGenState.field_179061_d;
            }
        }
        return GlStateManager.texGenState.field_179064_a;
    }

    public static void enableDepth() {
        GlStateManager.depthState.depthTest.setEnabled();
    }

    public static void enableTexGenCoord(TexGen texGen) {
        GlStateManager.texGenCoord((TexGen)texGen).field_179067_a.setEnabled();
    }

    public static void enableCull() {
        GlStateManager.cullState.field_179054_a.setEnabled();
    }

    public static void enableLight(int n) {
        lightState[n].setEnabled();
    }

    public static void enableNormalize() {
        normalizeState.setEnabled();
    }

    public static void disableAlpha() {
        GlStateManager.alphaState.field_179208_a.setDisabled();
    }

    public static void disableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setDisabled();
    }

    public static int generateTexture() {
        return GL11.glGenTextures();
    }

    public static void disableRescaleNormal() {
        rescaleNormalState.setDisabled();
    }

    public static void setFogStart(float f) {
        if (f != GlStateManager.fogState.field_179045_d) {
            GlStateManager.fogState.field_179045_d = f;
            GL11.glFogf((int)2915, (float)f);
        }
    }

    public static void colorMaterial(int n, int n2) {
        if (n != GlStateManager.colorMaterialState.field_179189_b || n2 != GlStateManager.colorMaterialState.field_179190_c) {
            GlStateManager.colorMaterialState.field_179189_b = n;
            GlStateManager.colorMaterialState.field_179190_c = n2;
            GL11.glColorMaterial((int)n, (int)n2);
        }
    }

    public static void depthFunc(int n) {
        if (n != GlStateManager.depthState.depthFunc) {
            GlStateManager.depthState.depthFunc = n;
            GL11.glDepthFunc((int)n);
        }
    }

    public static void disableLighting() {
        lightingState.setDisabled();
    }

    public static void enableRescaleNormal() {
        rescaleNormalState.setEnabled();
    }

    public static void disableNormalize() {
        normalizeState.setDisabled();
    }

    public static void getFloat(int n, FloatBuffer floatBuffer) {
        GL11.glGetFloat((int)n, (FloatBuffer)floatBuffer);
    }

    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setEnabled();
    }

    public static void color(float f, float f2, float f3, float f4) {
        if (f != GlStateManager.colorState.red || f2 != GlStateManager.colorState.green || f3 != GlStateManager.colorState.blue || f4 != GlStateManager.colorState.alpha) {
            GlStateManager.colorState.red = f;
            GlStateManager.colorState.green = f2;
            GlStateManager.colorState.blue = f3;
            GlStateManager.colorState.alpha = f4;
            GL11.glColor4f((float)f, (float)f2, (float)f3, (float)f4);
        }
    }

    public static void cullFace(int n) {
        if (n != GlStateManager.cullState.field_179053_b) {
            GlStateManager.cullState.field_179053_b = n;
            GL11.glCullFace((int)n);
        }
    }

    public static void disableLight(int n) {
        lightState[n].setDisabled();
    }

    static {
        lightingState = new BooleanState(2896);
        lightState = new BooleanState[8];
        colorMaterialState = new ColorMaterialState();
        blendState = new BlendState();
        depthState = new DepthState();
        fogState = new FogState();
        cullState = new CullState();
        polygonOffsetState = new PolygonOffsetState();
        colorLogicState = new ColorLogicState();
        texGenState = new TexGenState();
        clearState = new ClearState();
        stencilState = new StencilState();
        normalizeState = new BooleanState(2977);
        activeTextureUnit = 0;
        textureState = new TextureState[8];
        activeShadeModel = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask();
        colorState = new Color();
        int n = 0;
        while (n < 8) {
            GlStateManager.lightState[n] = new BooleanState(16384 + n);
            ++n;
        }
        n = 0;
        while (n < 8) {
            GlStateManager.textureState[n] = new TextureState();
            ++n;
        }
    }

    public static void disableDepth() {
        GlStateManager.depthState.depthTest.setDisabled();
    }

    public static void scale(float f, float f2, float f3) {
        GL11.glScalef((float)f, (float)f2, (float)f3);
    }

    public static void texGen(TexGen texGen, int n) {
        TexGenCoord texGenCoord = GlStateManager.texGenCoord(texGen);
        if (n != texGenCoord.field_179066_c) {
            texGenCoord.field_179066_c = n;
            GL11.glTexGeni((int)texGenCoord.field_179065_b, (int)9472, (int)n);
        }
    }

    public static void enableBlend() {
        GlStateManager.blendState.field_179213_a.setEnabled();
    }

    public static void color(float f, float f2, float f3) {
        GlStateManager.color(f, f2, f3, 1.0f);
    }

    public static void setFog(int n) {
        if (n != GlStateManager.fogState.field_179047_b) {
            GlStateManager.fogState.field_179047_b = n;
            GL11.glFogi((int)2917, (int)n);
        }
    }

    public static void translate(float f, float f2, float f3) {
        GL11.glTranslatef((float)f, (float)f2, (float)f3);
    }

    public static void bindTexture(int n) {
        if (n != GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName) {
            GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = n;
            GL11.glBindTexture((int)3553, (int)n);
        }
    }

    public static void ortho(double d, double d2, double d3, double d4, double d5, double d6) {
        GL11.glOrtho((double)d, (double)d2, (double)d3, (double)d4, (double)d5, (double)d6);
    }

    public static void func_179105_a(TexGen texGen, int n, FloatBuffer floatBuffer) {
        GL11.glTexGen((int)GlStateManager.texGenCoord((TexGen)texGen).field_179065_b, (int)n, (FloatBuffer)floatBuffer);
    }

    public static void enableLighting() {
        lightingState.setEnabled();
    }

    public static void setFogDensity(float f) {
        if (f != GlStateManager.fogState.field_179048_c) {
            GlStateManager.fogState.field_179048_c = f;
            GL11.glFogf((int)2914, (float)f);
        }
    }

    public static void clear(int n) {
        GL11.glClear((int)n);
    }

    public static void enableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setEnabled();
    }

    public static void doPolygonOffset(float f, float f2) {
        if (f != GlStateManager.polygonOffsetState.field_179043_c || f2 != GlStateManager.polygonOffsetState.field_179041_d) {
            GlStateManager.polygonOffsetState.field_179043_c = f;
            GlStateManager.polygonOffsetState.field_179041_d = f2;
            GL11.glPolygonOffset((float)f, (float)f2);
        }
    }

    public static void callList(int n) {
        GL11.glCallList((int)n);
    }

    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setDisabled();
    }

    public static void enableAlpha() {
        GlStateManager.alphaState.field_179208_a.setEnabled();
    }

    public static void clearDepth(double d) {
        if (d != GlStateManager.clearState.field_179205_a) {
            GlStateManager.clearState.field_179205_a = d;
            GL11.glClearDepth((double)d);
        }
    }

    public static void disableCull() {
        GlStateManager.cullState.field_179054_a.setDisabled();
    }

    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setDisabled();
    }

    public static void setActiveTexture(int n) {
        if (activeTextureUnit != n - OpenGlHelper.defaultTexUnit) {
            activeTextureUnit = n - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(n);
        }
    }

    public static void enableFog() {
        GlStateManager.fogState.field_179049_a.setEnabled();
    }

    public static void enableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setEnabled();
    }

    static class AlphaState {
        public int func = 519;
        public BooleanState field_179208_a = new BooleanState(3008);
        public float ref = -1.0f;

        private AlphaState() {
        }
    }

    static class Color {
        public float red = 1.0f;
        public float alpha = 1.0f;
        public float blue = 1.0f;
        public float green = 1.0f;

        public Color() {
        }

        public Color(float f, float f2, float f3, float f4) {
            this.red = f;
            this.green = f2;
            this.blue = f3;
            this.alpha = f4;
        }
    }

    static class DepthState {
        public BooleanState depthTest = new BooleanState(2929);
        public int depthFunc = 513;
        public boolean maskEnabled = true;

        private DepthState() {
        }
    }

    static class PolygonOffsetState {
        public BooleanState field_179044_a = new BooleanState(32823);
        public float field_179043_c = 0.0f;
        public BooleanState field_179042_b = new BooleanState(10754);
        public float field_179041_d = 0.0f;

        private PolygonOffsetState() {
        }
    }

    static class ColorLogicState {
        public BooleanState field_179197_a = new BooleanState(3058);
        public int field_179196_b = 5379;

        private ColorLogicState() {
        }
    }

    static class ColorMaterialState {
        public BooleanState field_179191_a = new BooleanState(2903);
        public int field_179190_c = 5634;
        public int field_179189_b = 1032;

        private ColorMaterialState() {
        }
    }

    static class BooleanState {
        private boolean currentState = false;
        private final int capability;

        public void setDisabled() {
            this.setState(false);
        }

        public BooleanState(int n) {
            this.capability = n;
        }

        public void setState(boolean bl) {
            if (bl != this.currentState) {
                this.currentState = bl;
                if (bl) {
                    GL11.glEnable((int)this.capability);
                } else {
                    GL11.glDisable((int)this.capability);
                }
            }
        }

        public void setEnabled() {
            this.setState(true);
        }
    }

    static class CullState {
        public int field_179053_b = 1029;
        public BooleanState field_179054_a = new BooleanState(2884);

        private CullState() {
        }
    }

    static class ClearState {
        public Color field_179203_b = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        public double field_179205_a = 1.0;
        public int field_179204_c = 0;

        private ClearState() {
        }
    }

    static class TextureState {
        public int textureName = 0;
        public BooleanState texture2DState = new BooleanState(3553);

        private TextureState() {
        }
    }

    static class BlendState {
        public int srcFactorAlpha = 1;
        public int dstFactorAlpha = 0;
        public int dstFactor = 0;
        public int srcFactor = 1;
        public BooleanState field_179213_a = new BooleanState(3042);

        private BlendState() {
        }
    }

    static class TexGenCoord {
        public int field_179066_c = -1;
        public int field_179065_b;
        public BooleanState field_179067_a;

        public TexGenCoord(int n, int n2) {
            this.field_179065_b = n;
            this.field_179067_a = new BooleanState(n2);
        }
    }

    static class StencilState {
        public int field_179075_e = 7680;
        public int field_179074_d = 7680;
        public StencilFunc field_179078_a = new StencilFunc();
        public int field_179076_b = -1;
        public int field_179077_c = 7680;

        private StencilState() {
        }
    }

    static class ColorMask {
        public boolean red = true;
        public boolean blue = true;
        public boolean green = true;
        public boolean alpha = true;

        private ColorMask() {
        }
    }

    public static enum TexGen {
        S,
        T,
        R,
        Q;

    }

    static class TexGenState {
        public TexGenCoord field_179064_a = new TexGenCoord(8192, 3168);
        public TexGenCoord field_179061_d;
        public TexGenCoord field_179062_b = new TexGenCoord(8193, 3169);
        public TexGenCoord field_179063_c = new TexGenCoord(8194, 3170);

        private TexGenState() {
            this.field_179061_d = new TexGenCoord(8195, 3171);
        }
    }

    static class StencilFunc {
        public int field_179080_c = -1;
        public int field_179081_a = 519;
        public int field_179079_b = 0;

        private StencilFunc() {
        }
    }

    static class FogState {
        public float field_179048_c = 1.0f;
        public float field_179046_e = 1.0f;
        public int field_179047_b = 2048;
        public float field_179045_d = 0.0f;
        public BooleanState field_179049_a = new BooleanState(2912);

        private FogState() {
        }
    }
}

