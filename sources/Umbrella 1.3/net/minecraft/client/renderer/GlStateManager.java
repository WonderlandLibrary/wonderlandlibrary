/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import optifine.Config;
import org.lwjgl.opengl.GL11;

public class GlStateManager {
    private static AlphaState alphaState;
    private static BooleanState lightingState;
    private static BooleanState[] field_179159_c;
    private static ColorMaterialState colorMaterialState;
    private static BlendState blendState;
    private static DepthState depthState;
    private static FogState fogState;
    private static CullState cullState;
    private static PolygonOffsetState polygonOffsetState;
    private static ColorLogicState colorLogicState;
    private static TexGenState texGenState;
    private static ClearState clearState;
    private static StencilState stencilState;
    private static BooleanState normalizeState;
    private static int field_179162_o;
    private static TextureState[] field_179174_p;
    private static int field_179173_q;
    private static BooleanState rescaleNormalState;
    private static ColorMask colorMaskState;
    private static Color colorState;
    private static Viewport field_179169_u;
    private static final String __OBFID = "CL_00002558";
    public static boolean clearEnabled;

    static {
        int var0;
        alphaState = new AlphaState(null);
        lightingState = new BooleanState(2896);
        field_179159_c = new BooleanState[8];
        colorMaterialState = new ColorMaterialState(null);
        blendState = new BlendState(null);
        depthState = new DepthState(null);
        fogState = new FogState(null);
        cullState = new CullState(null);
        polygonOffsetState = new PolygonOffsetState(null);
        colorLogicState = new ColorLogicState(null);
        texGenState = new TexGenState(null);
        clearState = new ClearState(null);
        stencilState = new StencilState(null);
        normalizeState = new BooleanState(2977);
        field_179162_o = 0;
        field_179174_p = new TextureState[32];
        field_179173_q = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask(null);
        colorState = new Color();
        field_179169_u = new Viewport(null);
        clearEnabled = true;
        for (var0 = 0; var0 < 8; ++var0) {
            GlStateManager.field_179159_c[var0] = new BooleanState(16384 + var0);
        }
        for (var0 = 0; var0 < field_179174_p.length; ++var0) {
            GlStateManager.field_179174_p[var0] = new TextureState(null);
        }
    }

    public static void pushAttrib() {
        GL11.glPushAttrib((int)8256);
    }

    public static void popAttrib() {
        GL11.glPopAttrib();
    }

    public static void disableAlpha() {
        GlStateManager.alphaState.field_179208_a.setDisabled();
    }

    public static void enableAlpha() {
        GlStateManager.alphaState.field_179208_a.setEnabled();
    }

    public static void alphaFunc(int p_179092_0_, float p_179092_1_) {
        if (p_179092_0_ != GlStateManager.alphaState.field_179206_b || p_179092_1_ != GlStateManager.alphaState.field_179207_c) {
            GlStateManager.alphaState.field_179206_b = p_179092_0_;
            GlStateManager.alphaState.field_179207_c = p_179092_1_;
            GL11.glAlphaFunc((int)p_179092_0_, (float)p_179092_1_);
        }
    }

    public static void enableLighting() {
        lightingState.setEnabled();
    }

    public static void disableLighting() {
        lightingState.setDisabled();
    }

    public static void enableBooleanStateAt(int p_179085_0_) {
        field_179159_c[p_179085_0_].setEnabled();
    }

    public static void disableBooleanStateAt(int p_179122_0_) {
        field_179159_c[p_179122_0_].setDisabled();
    }

    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setEnabled();
    }

    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setDisabled();
    }

    public static void colorMaterial(int p_179104_0_, int p_179104_1_) {
        if (p_179104_0_ != GlStateManager.colorMaterialState.field_179189_b || p_179104_1_ != GlStateManager.colorMaterialState.field_179190_c) {
            GlStateManager.colorMaterialState.field_179189_b = p_179104_0_;
            GlStateManager.colorMaterialState.field_179190_c = p_179104_1_;
            GL11.glColorMaterial((int)p_179104_0_, (int)p_179104_1_);
        }
    }

    public static void disableDepth() {
        GlStateManager.depthState.field_179052_a.setDisabled();
    }

    public static void enableDepth() {
        GlStateManager.depthState.field_179052_a.setEnabled();
    }

    public static void depthFunc(int p_179143_0_) {
        if (p_179143_0_ != GlStateManager.depthState.field_179051_c) {
            GlStateManager.depthState.field_179051_c = p_179143_0_;
            GL11.glDepthFunc((int)p_179143_0_);
        }
    }

    public static void depthMask(boolean p_179132_0_) {
        if (p_179132_0_ != GlStateManager.depthState.field_179050_b) {
            GlStateManager.depthState.field_179050_b = p_179132_0_;
            GL11.glDepthMask((boolean)p_179132_0_);
        }
    }

    public static void disableBlend() {
        GlStateManager.blendState.field_179213_a.setDisabled();
    }

    public static void enableBlend() {
        GlStateManager.blendState.field_179213_a.setEnabled();
    }

    public static void blendFunc(int p_179112_0_, int p_179112_1_) {
        if (p_179112_0_ != GlStateManager.blendState.field_179211_b || p_179112_1_ != GlStateManager.blendState.field_179212_c) {
            GlStateManager.blendState.field_179211_b = p_179112_0_;
            GlStateManager.blendState.field_179212_c = p_179112_1_;
            GL11.glBlendFunc((int)p_179112_0_, (int)p_179112_1_);
        }
    }

    public static void tryBlendFuncSeparate(int p_179120_0_, int p_179120_1_, int p_179120_2_, int p_179120_3_) {
        if (p_179120_0_ != GlStateManager.blendState.field_179211_b || p_179120_1_ != GlStateManager.blendState.field_179212_c || p_179120_2_ != GlStateManager.blendState.field_179209_d || p_179120_3_ != GlStateManager.blendState.field_179210_e) {
            GlStateManager.blendState.field_179211_b = p_179120_0_;
            GlStateManager.blendState.field_179212_c = p_179120_1_;
            GlStateManager.blendState.field_179209_d = p_179120_2_;
            GlStateManager.blendState.field_179210_e = p_179120_3_;
            OpenGlHelper.glBlendFunc(p_179120_0_, p_179120_1_, p_179120_2_, p_179120_3_);
        }
    }

    public static void enableFog() {
        GlStateManager.fogState.field_179049_a.setEnabled();
    }

    public static void disableFog() {
        GlStateManager.fogState.field_179049_a.setDisabled();
    }

    public static void setFog(int p_179093_0_) {
        if (p_179093_0_ != GlStateManager.fogState.field_179047_b) {
            GlStateManager.fogState.field_179047_b = p_179093_0_;
            GL11.glFogi((int)2917, (int)p_179093_0_);
        }
    }

    public static void setFogDensity(float p_179095_0_) {
        if (p_179095_0_ != GlStateManager.fogState.field_179048_c) {
            GlStateManager.fogState.field_179048_c = p_179095_0_;
            GL11.glFogf((int)2914, (float)p_179095_0_);
        }
    }

    public static void setFogStart(float p_179102_0_) {
        if (p_179102_0_ != GlStateManager.fogState.field_179045_d) {
            GlStateManager.fogState.field_179045_d = p_179102_0_;
            GL11.glFogf((int)2915, (float)p_179102_0_);
        }
    }

    public static void setFogEnd(float p_179153_0_) {
        if (p_179153_0_ != GlStateManager.fogState.field_179046_e) {
            GlStateManager.fogState.field_179046_e = p_179153_0_;
            GL11.glFogf((int)2916, (float)p_179153_0_);
        }
    }

    public static void enableCull() {
        GlStateManager.cullState.field_179054_a.setEnabled();
    }

    public static void disableCull() {
        GlStateManager.cullState.field_179054_a.setDisabled();
    }

    public static void cullFace(int p_179107_0_) {
        if (p_179107_0_ != GlStateManager.cullState.field_179053_b) {
            GlStateManager.cullState.field_179053_b = p_179107_0_;
            GL11.glCullFace((int)p_179107_0_);
        }
    }

    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setEnabled();
    }

    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setDisabled();
    }

    public static void doPolygonOffset(float p_179136_0_, float p_179136_1_) {
        if (p_179136_0_ != GlStateManager.polygonOffsetState.field_179043_c || p_179136_1_ != GlStateManager.polygonOffsetState.field_179041_d) {
            GlStateManager.polygonOffsetState.field_179043_c = p_179136_0_;
            GlStateManager.polygonOffsetState.field_179041_d = p_179136_1_;
            GL11.glPolygonOffset((float)p_179136_0_, (float)p_179136_1_);
        }
    }

    public static void enableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setEnabled();
    }

    public static void disableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setDisabled();
    }

    public static void colorLogicOp(int p_179116_0_) {
        if (p_179116_0_ != GlStateManager.colorLogicState.field_179196_b) {
            GlStateManager.colorLogicState.field_179196_b = p_179116_0_;
            GL11.glLogicOp((int)p_179116_0_);
        }
    }

    public static void enableTexGenCoord(TexGen p_179087_0_) {
        GlStateManager.texGenCoord((TexGen)p_179087_0_).field_179067_a.setEnabled();
    }

    public static void disableTexGenCoord(TexGen p_179100_0_) {
        GlStateManager.texGenCoord((TexGen)p_179100_0_).field_179067_a.setDisabled();
    }

    public static void texGen(TexGen p_179149_0_, int p_179149_1_) {
        TexGenCoord var2 = GlStateManager.texGenCoord(p_179149_0_);
        if (p_179149_1_ != var2.field_179066_c) {
            var2.field_179066_c = p_179149_1_;
            GL11.glTexGeni((int)var2.field_179065_b, (int)9472, (int)p_179149_1_);
        }
    }

    public static void func_179105_a(TexGen p_179105_0_, int p_179105_1_, FloatBuffer p_179105_2_) {
        GL11.glTexGen((int)GlStateManager.texGenCoord((TexGen)p_179105_0_).field_179065_b, (int)p_179105_1_, (FloatBuffer)p_179105_2_);
    }

    private static TexGenCoord texGenCoord(TexGen p_179125_0_) {
        switch (SwitchTexGen.field_179175_a[p_179125_0_.ordinal()]) {
            case 1: {
                return GlStateManager.texGenState.field_179064_a;
            }
            case 2: {
                return GlStateManager.texGenState.field_179062_b;
            }
            case 3: {
                return GlStateManager.texGenState.field_179063_c;
            }
            case 4: {
                return GlStateManager.texGenState.field_179061_d;
            }
        }
        return GlStateManager.texGenState.field_179064_a;
    }

    public static void setActiveTexture(int p_179138_0_) {
        if (field_179162_o != p_179138_0_ - OpenGlHelper.defaultTexUnit) {
            field_179162_o = p_179138_0_ - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(p_179138_0_);
        }
    }

    public static void func_179098_w() {
        GlStateManager.field_179174_p[GlStateManager.field_179162_o].field_179060_a.setEnabled();
    }

    public static void func_179090_x() {
        GlStateManager.field_179174_p[GlStateManager.field_179162_o].field_179060_a.setDisabled();
    }

    public static int func_179146_y() {
        return GL11.glGenTextures();
    }

    public static void func_179150_h(int p_179150_0_) {
        if (p_179150_0_ != 0) {
            GL11.glDeleteTextures((int)p_179150_0_);
            for (TextureState var4 : field_179174_p) {
                if (var4.field_179059_b != p_179150_0_) continue;
                var4.field_179059_b = 0;
            }
        }
    }

    public static void func_179144_i(int p_179144_0_) {
        if (p_179144_0_ != GlStateManager.field_179174_p[GlStateManager.field_179162_o].field_179059_b) {
            GlStateManager.field_179174_p[GlStateManager.field_179162_o].field_179059_b = p_179144_0_;
            GL11.glBindTexture((int)3553, (int)p_179144_0_);
        }
    }

    public static void bindCurrentTexture() {
        GL11.glBindTexture((int)3553, (int)GlStateManager.field_179174_p[GlStateManager.field_179162_o].field_179059_b);
    }

    public static void enableNormalize() {
        normalizeState.setEnabled();
    }

    public static void disableNormalize() {
        normalizeState.setDisabled();
    }

    public static void shadeModel(int p_179103_0_) {
        if (p_179103_0_ != field_179173_q) {
            field_179173_q = p_179103_0_;
            GL11.glShadeModel((int)p_179103_0_);
        }
    }

    public static void enableRescaleNormal() {
        rescaleNormalState.setEnabled();
    }

    public static void disableRescaleNormal() {
        rescaleNormalState.setDisabled();
    }

    public static void viewport(int p_179083_0_, int p_179083_1_, int p_179083_2_, int p_179083_3_) {
        if (p_179083_0_ != GlStateManager.field_179169_u.field_179058_a || p_179083_1_ != GlStateManager.field_179169_u.field_179056_b || p_179083_2_ != GlStateManager.field_179169_u.field_179057_c || p_179083_3_ != GlStateManager.field_179169_u.field_179055_d) {
            GlStateManager.field_179169_u.field_179058_a = p_179083_0_;
            GlStateManager.field_179169_u.field_179056_b = p_179083_1_;
            GlStateManager.field_179169_u.field_179057_c = p_179083_2_;
            GlStateManager.field_179169_u.field_179055_d = p_179083_3_;
            GL11.glViewport((int)p_179083_0_, (int)p_179083_1_, (int)p_179083_2_, (int)p_179083_3_);
        }
    }

    public static void colorMask(boolean p_179135_0_, boolean p_179135_1_, boolean p_179135_2_, boolean p_179135_3_) {
        if (p_179135_0_ != GlStateManager.colorMaskState.field_179188_a || p_179135_1_ != GlStateManager.colorMaskState.field_179186_b || p_179135_2_ != GlStateManager.colorMaskState.field_179187_c || p_179135_3_ != GlStateManager.colorMaskState.field_179185_d) {
            GlStateManager.colorMaskState.field_179188_a = p_179135_0_;
            GlStateManager.colorMaskState.field_179186_b = p_179135_1_;
            GlStateManager.colorMaskState.field_179187_c = p_179135_2_;
            GlStateManager.colorMaskState.field_179185_d = p_179135_3_;
            GL11.glColorMask((boolean)p_179135_0_, (boolean)p_179135_1_, (boolean)p_179135_2_, (boolean)p_179135_3_);
        }
    }

    public static void clearDepth(double p_179151_0_) {
        if (p_179151_0_ != GlStateManager.clearState.field_179205_a) {
            GlStateManager.clearState.field_179205_a = p_179151_0_;
            GL11.glClearDepth((double)p_179151_0_);
        }
    }

    public static void clearColor(float p_179082_0_, float p_179082_1_, float p_179082_2_, float p_179082_3_) {
        if (p_179082_0_ != GlStateManager.clearState.field_179203_b.field_179195_a || p_179082_1_ != GlStateManager.clearState.field_179203_b.green || p_179082_2_ != GlStateManager.clearState.field_179203_b.blue || p_179082_3_ != GlStateManager.clearState.field_179203_b.alpha) {
            GlStateManager.clearState.field_179203_b.field_179195_a = p_179082_0_;
            GlStateManager.clearState.field_179203_b.green = p_179082_1_;
            GlStateManager.clearState.field_179203_b.blue = p_179082_2_;
            GlStateManager.clearState.field_179203_b.alpha = p_179082_3_;
            GL11.glClearColor((float)p_179082_0_, (float)p_179082_1_, (float)p_179082_2_, (float)p_179082_3_);
        }
    }

    public static void clear(int p_179086_0_) {
        if (clearEnabled) {
            GL11.glClear((int)p_179086_0_);
        }
    }

    public static void matrixMode(int p_179128_0_) {
        GL11.glMatrixMode((int)p_179128_0_);
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

    public static void getFloat(int p_179111_0_, FloatBuffer p_179111_1_) {
        GL11.glGetFloat((int)p_179111_0_, (FloatBuffer)p_179111_1_);
    }

    public static void ortho(double p_179130_0_, double p_179130_2_, double p_179130_4_, double p_179130_6_, double p_179130_8_, double p_179130_10_) {
        GL11.glOrtho((double)p_179130_0_, (double)p_179130_2_, (double)p_179130_4_, (double)p_179130_6_, (double)p_179130_8_, (double)p_179130_10_);
    }

    public static void rotate(float p_179114_0_, float p_179114_1_, float p_179114_2_, float p_179114_3_) {
        GL11.glRotatef((float)p_179114_0_, (float)p_179114_1_, (float)p_179114_2_, (float)p_179114_3_);
    }

    public static void scale(float p_179152_0_, float p_179152_1_, float p_179152_2_) {
        GL11.glScalef((float)p_179152_0_, (float)p_179152_1_, (float)p_179152_2_);
    }

    public static void scale(double p_179139_0_, double p_179139_2_, double p_179139_4_) {
        GL11.glScaled((double)p_179139_0_, (double)p_179139_2_, (double)p_179139_4_);
    }

    public static void translate(float p_179109_0_, float p_179109_1_, float p_179109_2_) {
        GL11.glTranslatef((float)p_179109_0_, (float)p_179109_1_, (float)p_179109_2_);
    }

    public static void translate(double p_179137_0_, double p_179137_2_, double p_179137_4_) {
        GL11.glTranslated((double)p_179137_0_, (double)p_179137_2_, (double)p_179137_4_);
    }

    public static void multMatrix(FloatBuffer p_179110_0_) {
        GL11.glMultMatrix((FloatBuffer)p_179110_0_);
    }

    public static void color(float p_179131_0_, float p_179131_1_, float p_179131_2_, float p_179131_3_) {
        if (p_179131_0_ != GlStateManager.colorState.field_179195_a || p_179131_1_ != GlStateManager.colorState.green || p_179131_2_ != GlStateManager.colorState.blue || p_179131_3_ != GlStateManager.colorState.alpha) {
            GlStateManager.colorState.field_179195_a = p_179131_0_;
            GlStateManager.colorState.green = p_179131_1_;
            GlStateManager.colorState.blue = p_179131_2_;
            GlStateManager.colorState.alpha = p_179131_3_;
            GL11.glColor4f((float)p_179131_0_, (float)p_179131_1_, (float)p_179131_2_, (float)p_179131_3_);
        }
    }

    public static void color(float p_179124_0_, float p_179124_1_, float p_179124_2_) {
        GlStateManager.color(p_179124_0_, p_179124_1_, p_179124_2_, 1.0f);
    }

    public static void func_179117_G() {
        GlStateManager.colorState.alpha = -1.0f;
        GlStateManager.colorState.blue = -1.0f;
        GlStateManager.colorState.green = -1.0f;
        GlStateManager.colorState.field_179195_a = -1.0f;
    }

    public static void callList(int p_179148_0_) {
        GL11.glCallList((int)p_179148_0_);
    }

    public static int getActiveTextureUnit() {
        return OpenGlHelper.defaultTexUnit + field_179162_o;
    }

    public static int getBoundTexture() {
        return GlStateManager.field_179174_p[GlStateManager.field_179162_o].field_179059_b;
    }

    public static void checkBoundTexture() {
        if (Config.isMinecraftThread()) {
            int glAct = GL11.glGetInteger((int)34016);
            int glTex = GL11.glGetInteger((int)32873);
            int act = GlStateManager.getActiveTextureUnit();
            int tex = GlStateManager.getBoundTexture();
            if (tex > 0 && (glAct != act || glTex != tex)) {
                Config.dbg("checkTexture: act: " + act + ", glAct: " + glAct + ", tex: " + tex + ", glTex: " + glTex);
            }
        }
    }

    public static void deleteTextures(IntBuffer buf) {
        buf.rewind();
        while (buf.position() < buf.limit()) {
            int texId = buf.get();
            GlStateManager.func_179150_h(texId);
        }
        buf.rewind();
    }

    static class AlphaState {
        public BooleanState field_179208_a = new BooleanState(3008);
        public int field_179206_b = 519;
        public float field_179207_c = -1.0f;
        private static final String __OBFID = "CL_00002556";

        private AlphaState() {
        }

        AlphaState(SwitchTexGen p_i46269_1_) {
            this();
        }
    }

    static class BlendState {
        public BooleanState field_179213_a = new BooleanState(3042);
        public int field_179211_b = 1;
        public int field_179212_c = 0;
        public int field_179209_d = 1;
        public int field_179210_e = 0;

        private BlendState() {
        }

        BlendState(SwitchTexGen p_i46268_1_) {
            this();
        }
    }

    static class BooleanState {
        private final int capability;
        private boolean currentState = false;

        public BooleanState(int p_i46267_1_) {
            this.capability = p_i46267_1_;
        }

        public void setDisabled() {
            this.setState(false);
        }

        public void setEnabled() {
            this.setState(true);
        }

        public void setState(boolean p_179199_1_) {
            if (p_179199_1_ != this.currentState) {
                this.currentState = p_179199_1_;
                if (p_179199_1_) {
                    GL11.glEnable((int)this.capability);
                } else {
                    GL11.glDisable((int)this.capability);
                }
            }
        }
    }

    static class ClearState {
        public double field_179205_a = 1.0;
        public Color field_179203_b = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        public int field_179204_c = 0;

        private ClearState() {
        }

        ClearState(SwitchTexGen p_i46266_1_) {
            this();
        }
    }

    static class Color {
        public float field_179195_a = 1.0f;
        public float green = 1.0f;
        public float blue = 1.0f;
        public float alpha = 1.0f;

        public Color() {
        }

        public Color(float p_i46265_1_, float p_i46265_2_, float p_i46265_3_, float p_i46265_4_) {
            this.field_179195_a = p_i46265_1_;
            this.green = p_i46265_2_;
            this.blue = p_i46265_3_;
            this.alpha = p_i46265_4_;
        }
    }

    static class ColorLogicState {
        public BooleanState field_179197_a = new BooleanState(3058);
        public int field_179196_b = 5379;

        private ColorLogicState() {
        }

        ColorLogicState(SwitchTexGen p_i46264_1_) {
            this();
        }
    }

    static class ColorMask {
        public boolean field_179188_a = true;
        public boolean field_179186_b = true;
        public boolean field_179187_c = true;
        public boolean field_179185_d = true;

        private ColorMask() {
        }

        ColorMask(SwitchTexGen p_i46263_1_) {
            this();
        }
    }

    static class ColorMaterialState {
        public BooleanState field_179191_a = new BooleanState(2903);
        public int field_179189_b = 1032;
        public int field_179190_c = 5634;

        private ColorMaterialState() {
        }

        ColorMaterialState(SwitchTexGen p_i46262_1_) {
            this();
        }
    }

    static class CullState {
        public BooleanState field_179054_a = new BooleanState(2884);
        public int field_179053_b = 1029;

        private CullState() {
        }

        CullState(SwitchTexGen p_i46261_1_) {
            this();
        }
    }

    static class DepthState {
        public BooleanState field_179052_a = new BooleanState(2929);
        public boolean field_179050_b = true;
        public int field_179051_c = 513;

        private DepthState() {
        }

        DepthState(SwitchTexGen p_i46260_1_) {
            this();
        }
    }

    static class FogState {
        public BooleanState field_179049_a = new BooleanState(2912);
        public int field_179047_b = 2048;
        public float field_179048_c = 1.0f;
        public float field_179045_d = 0.0f;
        public float field_179046_e = 1.0f;

        private FogState() {
        }

        FogState(SwitchTexGen p_i46259_1_) {
            this();
        }
    }

    static class PolygonOffsetState {
        public BooleanState field_179044_a = new BooleanState(32823);
        public BooleanState field_179042_b = new BooleanState(10754);
        public float field_179043_c = 0.0f;
        public float field_179041_d = 0.0f;

        private PolygonOffsetState() {
        }

        PolygonOffsetState(SwitchTexGen p_i46258_1_) {
            this();
        }
    }

    static class StencilFunc {
        public int field_179081_a = 519;
        public int field_179079_b = 0;
        public int field_179080_c = -1;

        private StencilFunc() {
        }

        StencilFunc(SwitchTexGen p_i46257_1_) {
            this();
        }
    }

    static class StencilState {
        public StencilFunc field_179078_a = new StencilFunc(null);
        public int field_179076_b = -1;
        public int field_179077_c = 7680;
        public int field_179074_d = 7680;
        public int field_179075_e = 7680;

        private StencilState() {
        }

        StencilState(SwitchTexGen p_i46256_1_) {
            this();
        }
    }

    static final class SwitchTexGen {
        static final int[] field_179175_a = new int[TexGen.values().length];

        static {
            try {
                SwitchTexGen.field_179175_a[TexGen.S.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchTexGen.field_179175_a[TexGen.T.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchTexGen.field_179175_a[TexGen.R.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchTexGen.field_179175_a[TexGen.Q.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchTexGen() {
        }
    }

    public static enum TexGen {
        S("S", 0, "S", 0),
        T("T", 1, "T", 1),
        R("R", 2, "R", 2),
        Q("Q", 3, "Q", 3);

        private static final TexGen[] $VALUES;
        private static final String __OBFID = "CL_00002542";

        static {
            $VALUES = new TexGen[]{S, T, R, Q};
        }

        private TexGen(String p_i46378_1_, int p_i46378_2_, String p_i46255_1_, int p_i46255_2_) {
        }
    }

    static class TexGenCoord {
        public BooleanState field_179067_a;
        public int field_179065_b;
        public int field_179066_c = -1;

        public TexGenCoord(int p_i46254_1_, int p_i46254_2_) {
            this.field_179065_b = p_i46254_1_;
            this.field_179067_a = new BooleanState(p_i46254_2_);
        }
    }

    static class TexGenState {
        public TexGenCoord field_179064_a = new TexGenCoord(8192, 3168);
        public TexGenCoord field_179062_b = new TexGenCoord(8193, 3169);
        public TexGenCoord field_179063_c = new TexGenCoord(8194, 3170);
        public TexGenCoord field_179061_d = new TexGenCoord(8195, 3171);

        private TexGenState() {
        }

        TexGenState(SwitchTexGen p_i46253_1_) {
            this();
        }
    }

    static class TextureState {
        public BooleanState field_179060_a = new BooleanState(3553);
        public int field_179059_b = 0;

        private TextureState() {
        }

        TextureState(SwitchTexGen p_i46252_1_) {
            this();
        }
    }

    static class Viewport {
        public int field_179058_a = 0;
        public int field_179056_b = 0;
        public int field_179057_c = 0;
        public int field_179055_d = 0;

        private Viewport() {
        }

        Viewport(SwitchTexGen p_i46251_1_) {
            this();
        }
    }
}

