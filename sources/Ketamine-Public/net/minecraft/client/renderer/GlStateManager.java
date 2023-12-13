package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class GlStateManager
{
    private static GlStateManager.AlphaState alphaState = new GlStateManager.AlphaState();
    private static GlStateManager.BooleanState[] lightState = new GlStateManager.BooleanState[8];
    private static GlStateManager.FogState fogState = new GlStateManager.FogState();
    private static GlStateManager.PolygonOffsetState polygonOffsetState = new GlStateManager.PolygonOffsetState();
    private static GlStateManager.ColorLogicState colorLogicState = new GlStateManager.ColorLogicState();
    private static GlStateManager.TexGenState texGenState = new GlStateManager.TexGenState();
    private static GlStateManager.BooleanState normalizeState = new GlStateManager.BooleanState(2977);
    private static int activeTextureUnit = 0;
    private static GlStateManager.TextureState[] textureState = new GlStateManager.TextureState[8];
    private static GlStateManager.BooleanState rescaleNormalState = new GlStateManager.BooleanState(32826);

    public static void pushAttrib()
    {
        GL11.glPushAttrib(8256);
    }

    public static void popAttrib()
    {
        GL11.glPopAttrib();
    }

    public static void disableAlpha()
    {
        alphaState.field_179208_a.setDisabled();
    }

    public static void enableAlpha()
    {
        alphaState.field_179208_a.setEnabled();
    }

    public static void enableLight(int light)
    {
        lightState[light].setEnabled();
    }

    public static void disableLight(int light)
    {
        lightState[light].setDisabled();
    }

    public static void enableFog()
    {
        fogState.field_179049_a.setEnabled();
    }

    public static void disableFog()
    {
        fogState.field_179049_a.setDisabled();
    }

    public static void setFog(int param)
    {
        if (param != fogState.field_179047_b)
        {
            fogState.field_179047_b = param;
            GL11.glFogi(GL11.GL_FOG_MODE, param);
        }
    }

    public static void setFogDensity(float param)
    {
        if (param != fogState.field_179048_c)
        {
            fogState.field_179048_c = param;
            GL11.glFogf(GL11.GL_FOG_DENSITY, param);
        }
    }

    public static void setFogStart(float param)
    {
        if (param != fogState.field_179045_d)
        {
            fogState.field_179045_d = param;
            GL11.glFogf(GL11.GL_FOG_START, param);
        }
    }

    public static void setFogEnd(float param)
    {
        if (param != fogState.field_179046_e)
        {
            fogState.field_179046_e = param;
            GL11.glFogf(GL11.GL_FOG_END, param);
        }
    }

    public static void enablePolygonOffset()
    {
        polygonOffsetState.field_179044_a.setEnabled();
    }

    public static void disablePolygonOffset()
    {
        polygonOffsetState.field_179044_a.setDisabled();
    }

    public static void doPolygonOffset(float factor, float units)
    {
        if (factor != polygonOffsetState.field_179043_c || units != polygonOffsetState.field_179041_d)
        {
            polygonOffsetState.field_179043_c = factor;
            polygonOffsetState.field_179041_d = units;
            GL11.glPolygonOffset(factor, units);
        }
    }

    public static void enableColorLogic()
    {
        colorLogicState.field_179197_a.setEnabled();
    }

    public static void disableColorLogic()
    {
        colorLogicState.field_179197_a.setDisabled();
    }

    public static void colorLogicOp(int opcode)
    {
        if (opcode != colorLogicState.field_179196_b)
        {
            colorLogicState.field_179196_b = opcode;
            GL11.glLogicOp(opcode);
        }
    }

    public static void enableTexGenCoord(GlStateManager.TexGen p_179087_0_)
    {
        texGenCoord(p_179087_0_).field_179067_a.setEnabled();
    }

    public static void disableTexGenCoord(GlStateManager.TexGen p_179100_0_)
    {
        texGenCoord(p_179100_0_).field_179067_a.setDisabled();
    }

    public static void texGen(GlStateManager.TexGen p_179149_0_, int p_179149_1_)
    {
        GlStateManager.TexGenCoord glstatemanager$texgencoord = texGenCoord(p_179149_0_);

        if (p_179149_1_ != glstatemanager$texgencoord.field_179066_c)
        {
            glstatemanager$texgencoord.field_179066_c = p_179149_1_;
            GL11.glTexGeni(glstatemanager$texgencoord.field_179065_b, GL11.GL_TEXTURE_GEN_MODE, p_179149_1_);
        }
    }

    public static void func_179105_a(GlStateManager.TexGen p_179105_0_, int pname, FloatBuffer params)
    {
        GL11.glTexGen(texGenCoord(p_179105_0_).field_179065_b, pname, params);
    }

    private static GlStateManager.TexGenCoord texGenCoord(GlStateManager.TexGen p_179125_0_)
    {
        switch (p_179125_0_)
        {
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

    public static void setActiveTexture(int texture)
    {
        if (activeTextureUnit != texture - OpenGlHelper.defaultTexUnit)
        {
            activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(texture);
        }
    }

    public static void deleteTexture(int texture)
    {
        GL11.glDeleteTextures(texture);

        for (GlStateManager.TextureState glstatemanager$texturestate : textureState)
        {
            if (glstatemanager$texturestate.textureName == texture)
            {
                glstatemanager$texturestate.textureName = -1;
            }
        }
    }

    public static void enableNormalize()
    {
        normalizeState.setEnabled();
    }

    public static void disableNormalize()
    {
        normalizeState.setDisabled();
    }

    public static void enableRescaleNormal()
    {
        rescaleNormalState.setEnabled();
    }

    public static void disableRescaleNormal()
    {
        rescaleNormalState.setDisabled();
    }

    static
    {
        for (int i = 0; i < 8; ++i)
        {
            lightState[i] = new GlStateManager.BooleanState(16384 + i);
        }

        for (int j = 0; j < 8; ++j)
        {
            textureState[j] = new GlStateManager.TextureState();
        }
    }

    static class AlphaState
    {
        public GlStateManager.BooleanState field_179208_a;
        public int func;
        public float ref;

        private AlphaState()
        {
            this.field_179208_a = new GlStateManager.BooleanState(3008);
            this.func = 519;
            this.ref = -1.0F;
        }
    }

    static class BlendState
    {
        public GlStateManager.BooleanState field_179213_a;
        public int srcFactor;
        public int dstFactor;
        public int srcFactorAlpha;
        public int dstFactorAlpha;

        private BlendState()
        {
            this.field_179213_a = new GlStateManager.BooleanState(3042);
            this.srcFactor = 1;
            this.dstFactor = 0;
            this.srcFactorAlpha = 1;
            this.dstFactorAlpha = 0;
        }
    }

    static class BooleanState
    {
        private final int capability;
        private boolean currentState = false;

        public BooleanState(int capabilityIn)
        {
            this.capability = capabilityIn;
        }

        public void setDisabled()
        {
            this.setState(false);
        }

        public void setEnabled()
        {
            this.setState(true);
        }

        public void setState(boolean state)
        {
            if (state != this.currentState)
            {
                this.currentState = state;

                if (state)
                {
                    GL11.glEnable(this.capability);
                }
                else
                {
                    GL11.glDisable(this.capability);
                }
            }
        }
    }

    static class ColorLogicState
    {
        public GlStateManager.BooleanState field_179197_a;
        public int field_179196_b;

        private ColorLogicState()
        {
            this.field_179197_a = new GlStateManager.BooleanState(3058);
            this.field_179196_b = 5379;
        }
    }

    static class DepthState
    {
        public GlStateManager.BooleanState depthTest;
        public boolean maskEnabled;
        public int depthFunc;

        private DepthState()
        {
            this.depthTest = new GlStateManager.BooleanState(2929);
            this.maskEnabled = true;
            this.depthFunc = 513;
        }
    }

    static class FogState
    {
        public GlStateManager.BooleanState field_179049_a;
        public int field_179047_b;
        public float field_179048_c;
        public float field_179045_d;
        public float field_179046_e;

        private FogState()
        {
            this.field_179049_a = new GlStateManager.BooleanState(2912);
            this.field_179047_b = 2048;
            this.field_179048_c = 1.0F;
            this.field_179045_d = 0.0F;
            this.field_179046_e = 1.0F;
        }
    }

    static class PolygonOffsetState
    {
        public GlStateManager.BooleanState field_179044_a;
        public GlStateManager.BooleanState field_179042_b;
        public float field_179043_c;
        public float field_179041_d;

        private PolygonOffsetState()
        {
            this.field_179044_a = new GlStateManager.BooleanState(32823);
            this.field_179042_b = new GlStateManager.BooleanState(10754);
            this.field_179043_c = 0.0F;
            this.field_179041_d = 0.0F;
        }
    }

    public enum TexGen
    {
        S,
        T,
        R,
        Q;
    }

    static class TexGenCoord
    {
        public GlStateManager.BooleanState field_179067_a;
        public int field_179065_b;
        public int field_179066_c = -1;

        public TexGenCoord(int p_i46254_1_, int p_i46254_2_)
        {
            this.field_179065_b = p_i46254_1_;
            this.field_179067_a = new GlStateManager.BooleanState(p_i46254_2_);
        }
    }

    static class TexGenState
    {
        public GlStateManager.TexGenCoord field_179064_a;
        public GlStateManager.TexGenCoord field_179062_b;
        public GlStateManager.TexGenCoord field_179063_c;
        public GlStateManager.TexGenCoord field_179061_d;

        private TexGenState()
        {
            this.field_179064_a = new GlStateManager.TexGenCoord(8192, 3168);
            this.field_179062_b = new GlStateManager.TexGenCoord(8193, 3169);
            this.field_179063_c = new GlStateManager.TexGenCoord(8194, 3170);
            this.field_179061_d = new GlStateManager.TexGenCoord(8195, 3171);
        }
    }

    static class TextureState
    {
        public GlStateManager.BooleanState texture2DState;
        public int textureName;

        private TextureState()
        {
            this.texture2DState = new GlStateManager.BooleanState(3553);
            this.textureName = 0;
        }
    }
}
