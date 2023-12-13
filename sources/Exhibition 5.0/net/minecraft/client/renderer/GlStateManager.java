// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;

public class GlStateManager
{
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
    private static int textureIndex;
    private static TextureState[] textureStates;
    private static int field_179173_q;
    private static BooleanState rescaleNormalState;
    private static ColorMask colorMaskState;
    private static Color colorState;
    private static Viewport field_179169_u;
    private static final String __OBFID = "CL_00002558";
    
    public static void pushAttrib() {
        GL11.glPushAttrib(8256);
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
    
    public static void alphaFunc(final int p_179092_0_, final float p_179092_1_) {
        if (p_179092_0_ != GlStateManager.alphaState.field_179206_b || p_179092_1_ != GlStateManager.alphaState.field_179207_c) {
            GL11.glAlphaFunc(GlStateManager.alphaState.field_179206_b = p_179092_0_, GlStateManager.alphaState.field_179207_c = p_179092_1_);
        }
    }
    
    public static void enableLighting() {
        GlStateManager.lightingState.setEnabled();
    }
    
    public static void disableLighting() {
        GlStateManager.lightingState.setDisabled();
    }
    
    public static void enableBooleanStateAt(final int p_179085_0_) {
        GlStateManager.field_179159_c[p_179085_0_].setEnabled();
    }
    
    public static void disableBooleanStateAt(final int p_179122_0_) {
        GlStateManager.field_179159_c[p_179122_0_].setDisabled();
    }
    
    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setEnabled();
    }
    
    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.field_179191_a.setDisabled();
    }
    
    public static void colorMaterial(final int p_179104_0_, final int p_179104_1_) {
        if (p_179104_0_ != GlStateManager.colorMaterialState.field_179189_b || p_179104_1_ != GlStateManager.colorMaterialState.field_179190_c) {
            GL11.glColorMaterial(GlStateManager.colorMaterialState.field_179189_b = p_179104_0_, GlStateManager.colorMaterialState.field_179190_c = p_179104_1_);
        }
    }
    
    public static void disableDepth() {
        GlStateManager.depthState.field_179052_a.setDisabled();
    }
    
    public static void enableDepth() {
        GlStateManager.depthState.field_179052_a.setEnabled();
    }
    
    public static void depthFunc(final int p_179143_0_) {
        if (p_179143_0_ != GlStateManager.depthState.field_179051_c) {
            GL11.glDepthFunc(GlStateManager.depthState.field_179051_c = p_179143_0_);
        }
    }
    
    public static void depthMask(final boolean p_179132_0_) {
        if (p_179132_0_ != GlStateManager.depthState.field_179050_b) {
            GL11.glDepthMask(GlStateManager.depthState.field_179050_b = p_179132_0_);
        }
    }
    
    public static void disableBlend() {
        GlStateManager.blendState.field_179213_a.setDisabled();
    }
    
    public static void enableBlend() {
        GlStateManager.blendState.field_179213_a.setEnabled();
    }
    
    public static void blendFunc(final int p_179112_0_, final int p_179112_1_) {
        if (p_179112_0_ != GlStateManager.blendState.field_179211_b || p_179112_1_ != GlStateManager.blendState.field_179212_c) {
            GL11.glBlendFunc(GlStateManager.blendState.field_179211_b = p_179112_0_, GlStateManager.blendState.field_179212_c = p_179112_1_);
        }
    }
    
    public static void tryBlendFuncSeparate(final int p_179120_0_, final int p_179120_1_, final int p_179120_2_, final int p_179120_3_) {
        if (p_179120_0_ != GlStateManager.blendState.field_179211_b || p_179120_1_ != GlStateManager.blendState.field_179212_c || p_179120_2_ != GlStateManager.blendState.field_179209_d || p_179120_3_ != GlStateManager.blendState.field_179210_e) {
            OpenGlHelper.glBlendFunc(GlStateManager.blendState.field_179211_b = p_179120_0_, GlStateManager.blendState.field_179212_c = p_179120_1_, GlStateManager.blendState.field_179209_d = p_179120_2_, GlStateManager.blendState.field_179210_e = p_179120_3_);
        }
    }
    
    public static void enableFog() {
        GlStateManager.fogState.field_179049_a.setEnabled();
    }
    
    public static void disableFog() {
        GlStateManager.fogState.field_179049_a.setDisabled();
    }
    
    public static void setFog(final int p_179093_0_) {
        if (p_179093_0_ != GlStateManager.fogState.field_179047_b) {
            GL11.glFogi(2917, GlStateManager.fogState.field_179047_b = p_179093_0_);
        }
    }
    
    public static void setFogDensity(final float p_179095_0_) {
        if (p_179095_0_ != GlStateManager.fogState.field_179048_c) {
            GL11.glFogf(2914, GlStateManager.fogState.field_179048_c = p_179095_0_);
        }
    }
    
    public static void setFogStart(final float p_179102_0_) {
        if (p_179102_0_ != GlStateManager.fogState.field_179045_d) {
            GL11.glFogf(2915, GlStateManager.fogState.field_179045_d = p_179102_0_);
        }
    }
    
    public static void setFogEnd(final float p_179153_0_) {
        if (p_179153_0_ != GlStateManager.fogState.field_179046_e) {
            GL11.glFogf(2916, GlStateManager.fogState.field_179046_e = p_179153_0_);
        }
    }
    
    public static void enableCull() {
        GlStateManager.cullState.field_179054_a.setEnabled();
    }
    
    public static void disableCull() {
        GlStateManager.cullState.field_179054_a.setDisabled();
    }
    
    public static void cullFace(final int p_179107_0_) {
        if (p_179107_0_ != GlStateManager.cullState.field_179053_b) {
            GL11.glCullFace(GlStateManager.cullState.field_179053_b = p_179107_0_);
        }
    }
    
    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setEnabled();
    }
    
    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.field_179044_a.setDisabled();
    }
    
    public static void doPolygonOffset(final float p_179136_0_, final float p_179136_1_) {
        if (p_179136_0_ != GlStateManager.polygonOffsetState.field_179043_c || p_179136_1_ != GlStateManager.polygonOffsetState.field_179041_d) {
            GL11.glPolygonOffset(GlStateManager.polygonOffsetState.field_179043_c = p_179136_0_, GlStateManager.polygonOffsetState.field_179041_d = p_179136_1_);
        }
    }
    
    public static void enableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setEnabled();
    }
    
    public static void disableColorLogic() {
        GlStateManager.colorLogicState.field_179197_a.setDisabled();
    }
    
    public static void colorLogicOp(final int p_179116_0_) {
        if (p_179116_0_ != GlStateManager.colorLogicState.field_179196_b) {
            GL11.glLogicOp(GlStateManager.colorLogicState.field_179196_b = p_179116_0_);
        }
    }
    
    public static void enableTexGenCoord(final TexGen p_179087_0_) {
        texGenCoord(p_179087_0_).field_179067_a.setEnabled();
    }
    
    public static void disableTexGenCoord(final TexGen p_179100_0_) {
        texGenCoord(p_179100_0_).field_179067_a.setDisabled();
    }
    
    public static void texGen(final TexGen p_179149_0_, final int p_179149_1_) {
        final TexGenCoord var2 = texGenCoord(p_179149_0_);
        if (p_179149_1_ != var2.field_179066_c) {
            var2.field_179066_c = p_179149_1_;
            GL11.glTexGeni(var2.field_179065_b, 9472, p_179149_1_);
        }
    }
    
    public static void func_179105_a(final TexGen p_179105_0_, final int p_179105_1_, final FloatBuffer p_179105_2_) {
        GL11.glTexGen(texGenCoord(p_179105_0_).field_179065_b, p_179105_1_, p_179105_2_);
    }
    
    private static TexGenCoord texGenCoord(final TexGen p_179125_0_) {
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
            default: {
                return GlStateManager.texGenState.field_179064_a;
            }
        }
    }
    
    public static void setActiveTexture(final int p_179138_0_) {
        if (GlStateManager.textureIndex != p_179138_0_ - OpenGlHelper.defaultTexUnit) {
            GlStateManager.textureIndex = p_179138_0_ - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(p_179138_0_);
        }
    }
    
    public static void enableTextures() {
        GlStateManager.textureStates[GlStateManager.textureIndex].status.setEnabled();
    }
    
    public static void disableTextures() {
        GlStateManager.textureStates[GlStateManager.textureIndex].status.setDisabled();
    }
    
    public static int func_179146_y() {
        return GL11.glGenTextures();
    }
    
    public static void func_179150_h(final int p_179150_0_) {
        GL11.glDeleteTextures(p_179150_0_);
        for (final TextureState var4 : GlStateManager.textureStates) {
            if (var4.field_179059_b == p_179150_0_) {
                var4.field_179059_b = -1;
            }
        }
    }
    
    public static void func_179144_i(final int p_179144_0_) {
        if (p_179144_0_ != GlStateManager.textureStates[GlStateManager.textureIndex].field_179059_b) {
            GL11.glBindTexture(3553, GlStateManager.textureStates[GlStateManager.textureIndex].field_179059_b = p_179144_0_);
        }
    }
    
    public static void enableNormalize() {
        GlStateManager.normalizeState.setEnabled();
    }
    
    public static void disableNormalize() {
        GlStateManager.normalizeState.setDisabled();
    }
    
    public static void shadeModel(final int p_179103_0_) {
        if (p_179103_0_ != GlStateManager.field_179173_q) {
            GL11.glShadeModel(GlStateManager.field_179173_q = p_179103_0_);
        }
    }
    
    public static void enableRescaleNormal() {
        GlStateManager.rescaleNormalState.setEnabled();
    }
    
    public static void disableRescaleNormal() {
        GlStateManager.rescaleNormalState.setDisabled();
    }
    
    public static void viewport(final int p_179083_0_, final int p_179083_1_, final int p_179083_2_, final int p_179083_3_) {
        if (p_179083_0_ != GlStateManager.field_179169_u.field_179058_a || p_179083_1_ != GlStateManager.field_179169_u.field_179056_b || p_179083_2_ != GlStateManager.field_179169_u.field_179057_c || p_179083_3_ != GlStateManager.field_179169_u.field_179055_d) {
            GL11.glViewport(GlStateManager.field_179169_u.field_179058_a = p_179083_0_, GlStateManager.field_179169_u.field_179056_b = p_179083_1_, GlStateManager.field_179169_u.field_179057_c = p_179083_2_, GlStateManager.field_179169_u.field_179055_d = p_179083_3_);
        }
    }
    
    public static void colorMask(final boolean p_179135_0_, final boolean p_179135_1_, final boolean p_179135_2_, final boolean p_179135_3_) {
        if (p_179135_0_ != GlStateManager.colorMaskState.field_179188_a || p_179135_1_ != GlStateManager.colorMaskState.field_179186_b || p_179135_2_ != GlStateManager.colorMaskState.field_179187_c || p_179135_3_ != GlStateManager.colorMaskState.field_179185_d) {
            GL11.glColorMask(GlStateManager.colorMaskState.field_179188_a = p_179135_0_, GlStateManager.colorMaskState.field_179186_b = p_179135_1_, GlStateManager.colorMaskState.field_179187_c = p_179135_2_, GlStateManager.colorMaskState.field_179185_d = p_179135_3_);
        }
    }
    
    public static void clearDepth(final double p_179151_0_) {
        if (p_179151_0_ != GlStateManager.clearState.field_179205_a) {
            GL11.glClearDepth(GlStateManager.clearState.field_179205_a = p_179151_0_);
        }
    }
    
    public static void clearColor(final float p_179082_0_, final float p_179082_1_, final float p_179082_2_, final float p_179082_3_) {
        if (p_179082_0_ != GlStateManager.clearState.field_179203_b.field_179195_a || p_179082_1_ != GlStateManager.clearState.field_179203_b.green || p_179082_2_ != GlStateManager.clearState.field_179203_b.blue || p_179082_3_ != GlStateManager.clearState.field_179203_b.alpha) {
            GL11.glClearColor(GlStateManager.clearState.field_179203_b.field_179195_a = p_179082_0_, GlStateManager.clearState.field_179203_b.green = p_179082_1_, GlStateManager.clearState.field_179203_b.blue = p_179082_2_, GlStateManager.clearState.field_179203_b.alpha = p_179082_3_);
        }
    }
    
    public static void clear(final int p_179086_0_) {
        GL11.glClear(p_179086_0_);
    }
    
    public static void matrixMode(final int p_179128_0_) {
        GL11.glMatrixMode(p_179128_0_);
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
    
    public static void getFloat(final int p_179111_0_, final FloatBuffer p_179111_1_) {
        GL11.glGetFloat(p_179111_0_, p_179111_1_);
    }
    
    public static void ortho(final double p_179130_0_, final double p_179130_2_, final double p_179130_4_, final double p_179130_6_, final double p_179130_8_, final double p_179130_10_) {
        GL11.glOrtho(p_179130_0_, p_179130_2_, p_179130_4_, p_179130_6_, p_179130_8_, p_179130_10_);
    }
    
    public static void rotate(final float p_179114_0_, final float p_179114_1_, final float p_179114_2_, final float p_179114_3_) {
        GL11.glRotatef(p_179114_0_, p_179114_1_, p_179114_2_, p_179114_3_);
    }
    
    public static void scale(final float p_179152_0_, final float p_179152_1_, final float p_179152_2_) {
        GL11.glScalef(p_179152_0_, p_179152_1_, p_179152_2_);
    }
    
    public static void scale(final double p_179139_0_, final double p_179139_2_, final double p_179139_4_) {
        GL11.glScaled(p_179139_0_, p_179139_2_, p_179139_4_);
    }
    
    public static void translate(final float p_179109_0_, final float p_179109_1_, final float p_179109_2_) {
        GL11.glTranslatef(p_179109_0_, p_179109_1_, p_179109_2_);
    }
    
    public static void translate(final double p_179137_0_, final double p_179137_2_, final double p_179137_4_) {
        GL11.glTranslated(p_179137_0_, p_179137_2_, p_179137_4_);
    }
    
    public static void multMatrix(final FloatBuffer p_179110_0_) {
        GL11.glMultMatrix(p_179110_0_);
    }
    
    public static void color(final float p_179131_0_, final float p_179131_1_, final float p_179131_2_, final float p_179131_3_) {
        if (p_179131_0_ != GlStateManager.colorState.field_179195_a || p_179131_1_ != GlStateManager.colorState.green || p_179131_2_ != GlStateManager.colorState.blue || p_179131_3_ != GlStateManager.colorState.alpha) {
            GL11.glColor4f(GlStateManager.colorState.field_179195_a = p_179131_0_, GlStateManager.colorState.green = p_179131_1_, GlStateManager.colorState.blue = p_179131_2_, GlStateManager.colorState.alpha = p_179131_3_);
        }
    }
    
    public static void color(final float p_179124_0_, final float p_179124_1_, final float p_179124_2_) {
        color(p_179124_0_, p_179124_1_, p_179124_2_, 1.0f);
    }
    
    public static void func_179117_G() {
        final Color colorState = GlStateManager.colorState;
        final Color colorState2 = GlStateManager.colorState;
        final Color colorState3 = GlStateManager.colorState;
        final Color colorState4 = GlStateManager.colorState;
        final float n = -1.0f;
        colorState4.alpha = n;
        colorState3.blue = n;
        colorState2.green = n;
        colorState.field_179195_a = n;
    }
    
    public static void callList(final int p_179148_0_) {
        GL11.glCallList(p_179148_0_);
    }
    
    static {
        GlStateManager.alphaState = new AlphaState(null);
        GlStateManager.lightingState = new BooleanState(2896);
        GlStateManager.field_179159_c = new BooleanState[8];
        GlStateManager.colorMaterialState = new ColorMaterialState(null);
        GlStateManager.blendState = new BlendState(null);
        GlStateManager.depthState = new DepthState(null);
        GlStateManager.fogState = new FogState(null);
        GlStateManager.cullState = new CullState(null);
        GlStateManager.polygonOffsetState = new PolygonOffsetState(null);
        GlStateManager.colorLogicState = new ColorLogicState(null);
        GlStateManager.texGenState = new TexGenState(null);
        GlStateManager.clearState = new ClearState(null);
        GlStateManager.stencilState = new StencilState(null);
        GlStateManager.normalizeState = new BooleanState(2977);
        GlStateManager.textureIndex = 0;
        GlStateManager.textureStates = new TextureState[8];
        GlStateManager.field_179173_q = 7425;
        GlStateManager.rescaleNormalState = new BooleanState(32826);
        GlStateManager.colorMaskState = new ColorMask(null);
        GlStateManager.colorState = new Color();
        GlStateManager.field_179169_u = new Viewport(null);
        for (int var0 = 0; var0 < 8; ++var0) {
            GlStateManager.field_179159_c[var0] = new BooleanState(16384 + var0);
        }
        for (int var0 = 0; var0 < 8; ++var0) {
            GlStateManager.textureStates[var0] = new TextureState(null);
        }
    }
    
    static class AlphaState
    {
        public BooleanState field_179208_a;
        public int field_179206_b;
        public float field_179207_c;
        private static final String __OBFID = "CL_00002556";
        
        private AlphaState() {
            this.field_179208_a = new BooleanState(3008);
            this.field_179206_b = 519;
            this.field_179207_c = -1.0f;
        }
        
        AlphaState(final SwitchTexGen p_i46269_1_) {
            this();
        }
    }
    
    static class BlendState
    {
        public BooleanState field_179213_a;
        public int field_179211_b;
        public int field_179212_c;
        public int field_179209_d;
        public int field_179210_e;
        private static final String __OBFID = "CL_00002555";
        
        private BlendState() {
            this.field_179213_a = new BooleanState(3042);
            this.field_179211_b = 1;
            this.field_179212_c = 0;
            this.field_179209_d = 1;
            this.field_179210_e = 0;
        }
        
        BlendState(final SwitchTexGen p_i46268_1_) {
            this();
        }
    }
    
    static class BooleanState
    {
        private final int capability;
        private boolean currentState;
        private static final String __OBFID = "CL_00002554";
        
        public BooleanState(final int p_i46267_1_) {
            this.currentState = false;
            this.capability = p_i46267_1_;
        }
        
        public void setDisabled() {
            this.setState(false);
        }
        
        public void setEnabled() {
            this.setState(true);
        }
        
        public void setState(final boolean p_179199_1_) {
            if (p_179199_1_ != this.currentState) {
                this.currentState = p_179199_1_;
                if (p_179199_1_) {
                    GL11.glEnable(this.capability);
                }
                else {
                    GL11.glDisable(this.capability);
                }
            }
        }
    }
    
    static class ClearState
    {
        public double field_179205_a;
        public Color field_179203_b;
        public int field_179204_c;
        private static final String __OBFID = "CL_00002553";
        
        private ClearState() {
            this.field_179205_a = 1.0;
            this.field_179203_b = new Color(0.0f, 0.0f, 0.0f, 0.0f);
            this.field_179204_c = 0;
        }
        
        ClearState(final SwitchTexGen p_i46266_1_) {
            this();
        }
    }
    
    static class Color
    {
        public float field_179195_a;
        public float green;
        public float blue;
        public float alpha;
        private static final String __OBFID = "CL_00002552";
        
        public Color() {
            this.field_179195_a = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
        }
        
        public Color(final float p_i46265_1_, final float p_i46265_2_, final float p_i46265_3_, final float p_i46265_4_) {
            this.field_179195_a = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
            this.field_179195_a = p_i46265_1_;
            this.green = p_i46265_2_;
            this.blue = p_i46265_3_;
            this.alpha = p_i46265_4_;
        }
    }
    
    static class ColorLogicState
    {
        public BooleanState field_179197_a;
        public int field_179196_b;
        private static final String __OBFID = "CL_00002551";
        
        private ColorLogicState() {
            this.field_179197_a = new BooleanState(3058);
            this.field_179196_b = 5379;
        }
        
        ColorLogicState(final SwitchTexGen p_i46264_1_) {
            this();
        }
    }
    
    static class ColorMask
    {
        public boolean field_179188_a;
        public boolean field_179186_b;
        public boolean field_179187_c;
        public boolean field_179185_d;
        private static final String __OBFID = "CL_00002550";
        
        private ColorMask() {
            this.field_179188_a = true;
            this.field_179186_b = true;
            this.field_179187_c = true;
            this.field_179185_d = true;
        }
        
        ColorMask(final SwitchTexGen p_i46263_1_) {
            this();
        }
    }
    
    static class ColorMaterialState
    {
        public BooleanState field_179191_a;
        public int field_179189_b;
        public int field_179190_c;
        private static final String __OBFID = "CL_00002549";
        
        private ColorMaterialState() {
            this.field_179191_a = new BooleanState(2903);
            this.field_179189_b = 1032;
            this.field_179190_c = 5634;
        }
        
        ColorMaterialState(final SwitchTexGen p_i46262_1_) {
            this();
        }
    }
    
    static class CullState
    {
        public BooleanState field_179054_a;
        public int field_179053_b;
        private static final String __OBFID = "CL_00002548";
        
        private CullState() {
            this.field_179054_a = new BooleanState(2884);
            this.field_179053_b = 1029;
        }
        
        CullState(final SwitchTexGen p_i46261_1_) {
            this();
        }
    }
    
    static class DepthState
    {
        public BooleanState field_179052_a;
        public boolean field_179050_b;
        public int field_179051_c;
        private static final String __OBFID = "CL_00002547";
        
        private DepthState() {
            this.field_179052_a = new BooleanState(2929);
            this.field_179050_b = true;
            this.field_179051_c = 513;
        }
        
        DepthState(final SwitchTexGen p_i46260_1_) {
            this();
        }
    }
    
    static class FogState
    {
        public BooleanState field_179049_a;
        public int field_179047_b;
        public float field_179048_c;
        public float field_179045_d;
        public float field_179046_e;
        private static final String __OBFID = "CL_00002546";
        
        private FogState() {
            this.field_179049_a = new BooleanState(2912);
            this.field_179047_b = 2048;
            this.field_179048_c = 1.0f;
            this.field_179045_d = 0.0f;
            this.field_179046_e = 1.0f;
        }
        
        FogState(final SwitchTexGen p_i46259_1_) {
            this();
        }
    }
    
    static class PolygonOffsetState
    {
        public BooleanState field_179044_a;
        public BooleanState field_179042_b;
        public float field_179043_c;
        public float field_179041_d;
        private static final String __OBFID = "CL_00002545";
        
        private PolygonOffsetState() {
            this.field_179044_a = new BooleanState(32823);
            this.field_179042_b = new BooleanState(10754);
            this.field_179043_c = 0.0f;
            this.field_179041_d = 0.0f;
        }
        
        PolygonOffsetState(final SwitchTexGen p_i46258_1_) {
            this();
        }
    }
    
    static class StencilFunc
    {
        public int field_179081_a;
        public int field_179079_b;
        public int field_179080_c;
        private static final String __OBFID = "CL_00002544";
        
        private StencilFunc() {
            this.field_179081_a = 519;
            this.field_179079_b = 0;
            this.field_179080_c = -1;
        }
        
        StencilFunc(final SwitchTexGen p_i46257_1_) {
            this();
        }
    }
    
    static class StencilState
    {
        public StencilFunc field_179078_a;
        public int field_179076_b;
        public int field_179077_c;
        public int field_179074_d;
        public int field_179075_e;
        private static final String __OBFID = "CL_00002543";
        
        private StencilState() {
            this.field_179078_a = new StencilFunc(null);
            this.field_179076_b = -1;
            this.field_179077_c = 7680;
            this.field_179074_d = 7680;
            this.field_179075_e = 7680;
        }
        
        StencilState(final SwitchTexGen p_i46256_1_) {
            this();
        }
    }
    
    static final class SwitchTexGen
    {
        static final int[] field_179175_a;
        private static final String __OBFID = "CL_00002557";
        
        static {
            field_179175_a = new int[TexGen.values().length];
            try {
                SwitchTexGen.field_179175_a[TexGen.S.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchTexGen.field_179175_a[TexGen.T.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchTexGen.field_179175_a[TexGen.R.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchTexGen.field_179175_a[TexGen.Q.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
    
    public enum TexGen
    {
        S("S", 0), 
        T("T", 1), 
        R("R", 2), 
        Q("Q", 3);
        
        private static final TexGen[] $VALUES;
        private static final String __OBFID = "CL_00002542";
        
        private TexGen(final String p_i46255_1_, final int p_i46255_2_) {
        }
        
        static {
            $VALUES = new TexGen[] { TexGen.S, TexGen.T, TexGen.R, TexGen.Q };
        }
    }
    
    static class TexGenCoord
    {
        public BooleanState field_179067_a;
        public int field_179065_b;
        public int field_179066_c;
        private static final String __OBFID = "CL_00002541";
        
        public TexGenCoord(final int p_i46254_1_, final int p_i46254_2_) {
            this.field_179066_c = -1;
            this.field_179065_b = p_i46254_1_;
            this.field_179067_a = new BooleanState(p_i46254_2_);
        }
    }
    
    static class TexGenState
    {
        public TexGenCoord field_179064_a;
        public TexGenCoord field_179062_b;
        public TexGenCoord field_179063_c;
        public TexGenCoord field_179061_d;
        private static final String __OBFID = "CL_00002540";
        
        private TexGenState() {
            this.field_179064_a = new TexGenCoord(8192, 3168);
            this.field_179062_b = new TexGenCoord(8193, 3169);
            this.field_179063_c = new TexGenCoord(8194, 3170);
            this.field_179061_d = new TexGenCoord(8195, 3171);
        }
        
        TexGenState(final SwitchTexGen p_i46253_1_) {
            this();
        }
    }
    
    static class TextureState
    {
        public BooleanState status;
        public int field_179059_b;
        private static final String __OBFID = "CL_00002539";
        
        private TextureState() {
            this.status = new BooleanState(3553);
            this.field_179059_b = 0;
        }
        
        TextureState(final SwitchTexGen p_i46252_1_) {
            this();
        }
    }
    
    static class Viewport
    {
        public int field_179058_a;
        public int field_179056_b;
        public int field_179057_c;
        public int field_179055_d;
        private static final String __OBFID = "CL_00002538";
        
        private Viewport() {
            this.field_179058_a = 0;
            this.field_179056_b = 0;
            this.field_179057_c = 0;
            this.field_179055_d = 0;
        }
        
        Viewport(final SwitchTexGen p_i46251_1_) {
            this();
        }
    }
}
