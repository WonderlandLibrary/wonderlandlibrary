package info.sigmaclient.sigma.utils.render.rendermanagers;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.glBlendFunc;

public class GlStateManager
{
    private static final FloatBuffer BUF_FLOAT_16 = BufferUtils.createFloatBuffer(16);
    private static final FloatBuffer BUF_FLOAT_4 = BufferUtils.createFloatBuffer(4);
    private static final AlphaState alphaState = new AlphaState();
    private static final BooleanState lightingState = new BooleanState(2896);
    private static final BooleanState[] lightState = new BooleanState[8];
    private static final ColorMaterialState colorMaterialState;
    private static final BlendState blendState;
    private static final DepthState depthState;
    private static final FogState fogState;
    private static final CullState cullState;
    private static final PolygonOffsetState polygonOffsetState;
    private static final ColorLogicState colorLogicState;
    private static final TexGenState texGenState;
    private static final ClearState clearState;
    private static final StencilState stencilState;
    private static final BooleanState normalizeState;
    public static int activeTextureUnit;
    public static final TextureState[] textureState;
    private static int activeShadeModel;
    private static final BooleanState rescaleNormalState;
    private static final ColorMask colorMaskState;
    private static final Color colorState;
    public static boolean clearEnabled = true;

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
        alphaState.alphaTest.setDisabled();
    }

    public static void enableAlpha()
    {
        alphaState.alphaTest.setEnabled();
    }

    public static void alphaFunc(int func, float ref)
    {
        if (func != alphaState.func || ref != alphaState.ref)
        {
            alphaState.func = func;
            alphaState.ref = ref;
            GL11.glAlphaFunc(func, ref);
        }
    }

    public static void enableLighting()
    {
        lightingState.setEnabled();
    }

    public static void disableLighting()
    {
        lightingState.setDisabled();
    }

    public static void enableLight(int light)
    {
        lightState[light].setEnabled();
    }

    public static void disableLight(int light)
    {
        lightState[light].setDisabled();
    }

    public static void enableColorMaterial()
    {
        colorMaterialState.colorMaterial.setEnabled();
    }

    public static void disableColorMaterial()
    {
        colorMaterialState.colorMaterial.setDisabled();
    }

    public static void colorMaterial(int face, int mode)
    {
        if (face != colorMaterialState.face || mode != colorMaterialState.mode)
        {
            colorMaterialState.face = face;
            colorMaterialState.mode = mode;
            GL11.glColorMaterial(face, mode);
        }
    }

    public static void glNormal3f(float nx, float ny, float nz)
    {
        GL11.glNormal3f(nx, ny, nz);
    }

    public static void disableDepth()
    {
        depthState.depthTest.setDisabled();
    }

    public static void enableDepth()
    {
        depthState.depthTest.setEnabled();
    }

    public static void depthFunc(int depthFunc)
    {
        if (depthFunc != depthState.depthFunc)
        {
            depthState.depthFunc = depthFunc;
            GL11.glDepthFunc(depthFunc);
        }
    }

    public static void depthMask(boolean flagIn)
    {
        if (flagIn != depthState.maskEnabled)
        {
            depthState.maskEnabled = flagIn;
            GL11.glDepthMask(flagIn);
        }
    }

    public static void disableBlend()
    {
//        blendState.blend.setDisabled();
        RenderSystem.disableBlend();
    }

    public static void enableBlend()
    {
        RenderSystem.enableBlend();
//        blendState.blend.setEnabled();
    }

    public static void blendFunc(SourceFactor srcFactor, DestFactor dstFactor)
    {
        blendFunc(srcFactor.factor, dstFactor.factor);
    }

    public static void blendFunc(int srcFactor, int dstFactor)
    {
        RenderSystem.blendFunc(srcFactor, dstFactor);
    }

    public static void tryBlendFuncSeparate(SourceFactor srcFactor, DestFactor dstFactor, SourceFactor srcFactorAlpha, DestFactor dstFactorAlpha)
    {
        RenderSystem.blendFuncSeparate(srcFactor.factor, dstFactor.factor, srcFactorAlpha.factor, dstFactorAlpha.factor);
//        tryBlendFuncSeparate(srcFactor.factor, dstFactor.factor, srcFactorAlpha.factor, dstFactorAlpha.factor);
    }

    public static void tryBlendFuncSeparate(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha)
    {
        if (srcFactor != blendState.srcFactor || dstFactor != blendState.dstFactor || srcFactorAlpha != blendState.srcFactorAlpha || dstFactorAlpha != blendState.dstFactorAlpha)
        {
            blendState.srcFactor = srcFactor;
            blendState.dstFactor = dstFactor;
            blendState.srcFactorAlpha = srcFactorAlpha;
            blendState.dstFactorAlpha = dstFactorAlpha;
            glBlendFunc(srcFactor, dstFactor);
        }
    }

    public static void glBlendEquation(int blendEquation)
    {
        GL14.glBlendEquation(blendEquation);
    }

    public static void disableOutlineMode()
    {
        glTexEnvi(8960, 8704, 8448);
        glTexEnvi(8960, 34161, 8448);
        glTexEnvi(8960, 34162, 8448);
        glTexEnvi(8960, 34176, 5890);
        glTexEnvi(8960, 34184, 5890);
        glTexEnvi(8960, 34192, 768);
        glTexEnvi(8960, 34200, 770);
    }

    public static void enableFog()
    {
        fogState.fog.setEnabled();
    }

    public static void disableFog()
    {
        fogState.fog.setDisabled();
    }

    public static void setFog(FogMode fogMode)
    {
        setFog(fogMode.capabilityId);
    }

    private static void setFog(int param)
    {
        if (param != fogState.mode)
        {
            fogState.mode = param;
            GL11.glFogi(GL11.GL_FOG_MODE, param);
        }
    }

    public static void setFogDensity(float param)
    {
        if (param != fogState.density)
        {
            fogState.density = param;
            GL11.glFogf(GL11.GL_FOG_DENSITY, param);
        }
    }

    public static void setFogStart(float param)
    {
        if (param != fogState.start)
        {
            fogState.start = param;
            GL11.glFogf(GL11.GL_FOG_START, param);
        }
    }

    public static void setFogEnd(float param)
    {
        if (param != fogState.end)
        {
            fogState.end = param;
            GL11.glFogf(GL11.GL_FOG_END, param);
        }
    }


    public static void glFogi(int pname, int param)
    {
        GL11.glFogi(pname, param);
    }

    public static void enableCull()
    {
        cullState.cullFace.setEnabled();
    }

    public static void disableCull()
    {
        cullState.cullFace.setDisabled();
    }

    public static void cullFace(CullFace cullFace)
    {
        cullFace(cullFace.mode);
    }

    private static void cullFace(int mode)
    {
        if (mode != cullState.mode)
        {
            cullState.mode = mode;
            GL11.glCullFace(mode);
        }
    }

    public static void glPolygonMode(int face, int mode)
    {
        GL11.glPolygonMode(face, mode);
    }

    public static void enablePolygonOffset()
    {
        polygonOffsetState.polygonOffsetFill.setEnabled();
    }

    public static void disablePolygonOffset()
    {
        polygonOffsetState.polygonOffsetFill.setDisabled();
    }

    public static void doPolygonOffset(float factor, float units)
    {
        if (factor != polygonOffsetState.factor || units != polygonOffsetState.units)
        {
            polygonOffsetState.factor = factor;
            polygonOffsetState.units = units;
            GL11.glPolygonOffset(factor, units);
        }
    }

    public static void enableColorLogic()
    {
        colorLogicState.colorLogicOp.setEnabled();
    }

    public static void disableColorLogic()
    {
        colorLogicState.colorLogicOp.setDisabled();
    }

    public static void colorLogicOp(LogicOp logicOperation)
    {
        colorLogicOp(logicOperation.opcode);
    }

    public static void colorLogicOp(int opcode)
    {
        if (opcode != colorLogicState.opcode)
        {
            colorLogicState.opcode = opcode;
            GL11.glLogicOp(opcode);
        }
    }

    public static void enableTexGenCoord(TexGen texGen)
    {
        texGenCoord(texGen).textureGen.setEnabled();
    }

    public static void disableTexGenCoord(TexGen texGen)
    {
        texGenCoord(texGen).textureGen.setDisabled();
    }

    public static void texGen(TexGen texGen, int param)
    {
        TexGenCoord glstatemanager$texgencoord = texGenCoord(texGen);

        if (param != glstatemanager$texgencoord.param)
        {
            glstatemanager$texgencoord.param = param;
            GL11.glTexGeni(glstatemanager$texgencoord.coord, GL11.GL_TEXTURE_GEN_MODE, param);
        }
    }

    private static TexGenCoord texGenCoord(TexGen texGen)
    {
        switch (texGen)
        {
            case S:
                return texGenState.s;

            case T:
                return texGenState.t;

            case R:
                return texGenState.r;

            case Q:
                return texGenState.q;

            default:
                return texGenState.s;
        }
    }
    public static void enableTexture2D()
    {
        textureState[activeTextureUnit].texture2DState.setEnabled();
    }

    public static void disableTexture2D()
    {
        textureState[activeTextureUnit].texture2DState.setDisabled();
    }


    public static void glTexEnvi(int p_187399_0_, int p_187399_1_, int p_187399_2_)
    {
        GL11.glTexEnvi(p_187399_0_, p_187399_1_, p_187399_2_);
    }

    public static void glTexEnvf(int p_187436_0_, int p_187436_1_, float p_187436_2_)
    {
        GL11.glTexEnvf(p_187436_0_, p_187436_1_, p_187436_2_);
    }

    public static void glTexParameterf(int p_187403_0_, int p_187403_1_, float p_187403_2_)
    {
        GL11.glTexParameterf(p_187403_0_, p_187403_1_, p_187403_2_);
    }

    public static void glTexParameteri(int p_187421_0_, int p_187421_1_, int p_187421_2_)
    {
        GL11.glTexParameteri(p_187421_0_, p_187421_1_, p_187421_2_);
    }

    public static int glGetTexLevelParameteri(int p_187411_0_, int p_187411_1_, int p_187411_2_)
    {
        return GL11.glGetTexLevelParameteri(p_187411_0_, p_187411_1_, p_187411_2_);
    }

    public static int generateTexture()
    {
        return GL11.glGenTextures();
    }

    public static void deleteTexture(int texture)
    {
        if (texture != 0)
        {
            GL11.glDeleteTextures(texture);

            for (TextureState glstatemanager$texturestate : textureState)
            {
                if (glstatemanager$texturestate.textureName == texture)
                {
                    glstatemanager$texturestate.textureName = 0;
                }
            }
        }
    }

    public static void bindTexture(int texture)
    {
        RenderSystem.bindTexture(texture);
    }

    public static void glTexImage2D(int p_187419_0_, int p_187419_1_, int p_187419_2_, int p_187419_3_, int p_187419_4_, int p_187419_5_, int p_187419_6_, int p_187419_7_, @Nullable IntBuffer p_187419_8_)
    {
        GL11.glTexImage2D(p_187419_0_, p_187419_1_, p_187419_2_, p_187419_3_, p_187419_4_, p_187419_5_, p_187419_6_, p_187419_7_, p_187419_8_);
    }

    public static void glTexSubImage2D(int p_187414_0_, int p_187414_1_, int p_187414_2_, int p_187414_3_, int p_187414_4_, int p_187414_5_, int p_187414_6_, int p_187414_7_, IntBuffer p_187414_8_)
    {
        GL11.glTexSubImage2D(p_187414_0_, p_187414_1_, p_187414_2_, p_187414_3_, p_187414_4_, p_187414_5_, p_187414_6_, p_187414_7_, p_187414_8_);
    }

    public static void glCopyTexSubImage2D(int p_187443_0_, int p_187443_1_, int p_187443_2_, int p_187443_3_, int p_187443_4_, int p_187443_5_, int p_187443_6_, int p_187443_7_)
    {
        GL11.glCopyTexSubImage2D(p_187443_0_, p_187443_1_, p_187443_2_, p_187443_3_, p_187443_4_, p_187443_5_, p_187443_6_, p_187443_7_);
    }

    public static void glGetTexImage(int p_187433_0_, int p_187433_1_, int p_187433_2_, int p_187433_3_, IntBuffer p_187433_4_)
    {
        GL11.glGetTexImage(p_187433_0_, p_187433_1_, p_187433_2_, p_187433_3_, p_187433_4_);
    }

    public static void enableNormalize()
    {
        normalizeState.setEnabled();
    }

    public static void disableNormalize()
    {
        normalizeState.setDisabled();
    }

    public static void shadeModel(int mode)
    {
        if (mode != activeShadeModel)
        {
            activeShadeModel = mode;
            GL11.glShadeModel(mode);
        }
    }

    public static void enableRescaleNormal()
    {
        rescaleNormalState.setEnabled();
    }

    public static void disableRescaleNormal()
    {
        rescaleNormalState.setDisabled();
    }

    public static void viewport(int x, int y, int width, int height)
    {
        GL11.glViewport(x, y, width, height);
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha)
    {
        if (red != colorMaskState.red || green != colorMaskState.green || blue != colorMaskState.blue || alpha != colorMaskState.alpha)
        {
            colorMaskState.red = red;
            colorMaskState.green = green;
            colorMaskState.blue = blue;
            colorMaskState.alpha = alpha;
            GL11.glColorMask(red, green, blue, alpha);
        }
    }

    public static void clearDepth(double depth)
    {
        if (depth != clearState.depth)
        {
            clearState.depth = depth;
            GL11.glClearDepth(depth);
        }
    }

    public static void clearColor(float red, float green, float blue, float alpha)
    {
        if (red != clearState.color.red || green != clearState.color.green || blue != clearState.color.blue || alpha != clearState.color.alpha)
        {
            clearState.color.red = red;
            clearState.color.green = green;
            clearState.color.blue = blue;
            clearState.color.alpha = alpha;
            GL11.glClearColor(red, green, blue, alpha);
        }
    }

    public static void clear(int mask)
    {
        if (clearEnabled)
        {
            GL11.glClear(mask);
        }
    }

    public static void matrixMode(int mode)
    {
        GL11.glMatrixMode(mode);
    }

    public static void loadIdentity()
    {
        GL11.glLoadIdentity();
    }

    public static void pushMatrix()
    {
        GL11.glPushMatrix();
    }

    public static void popMatrix()
    {
        GL11.glPopMatrix();
    }


    public static void ortho(double left, double right, double bottom, double top, double zNear, double zFar)
    {
        GL11.glOrtho(left, right, bottom, top, zNear, zFar);
    }

    public static void rotate(float angle, float x, float y, float z)
    {
        GL11.glRotatef(angle, x, y, z);
    }

    public static void scale(float x, float y, float z)
    {
        GL11.glScalef(x, y, z);
    }

    public static void scale(double x, double y, double z)
    {
        GL11.glScaled(x, y, z);
    }

    public static void translate(float x, float y, float z)
    {
        GL11.glTranslatef(x, y, z);
    }

    public static void translate(double x, double y, double z)
    {
        GL11.glTranslated(x, y, z);
    }

    public static void color(float colorRed, float colorGreen, float colorBlue, float colorAlpha)
    {
        if (colorRed != colorState.red || colorGreen != colorState.green || colorBlue != colorState.blue || colorAlpha != colorState.alpha)
        {
            colorState.red = colorRed;
            colorState.green = colorGreen;
            colorState.blue = colorBlue;
            colorState.alpha = colorAlpha;
            GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
        }
    }

    public static void color(float colorRed, float colorGreen, float colorBlue)
    {
        color(colorRed, colorGreen, colorBlue, 1.0F);
    }

    public static void glTexCoord2f(float p_187426_0_, float p_187426_1_)
    {
        GL11.glTexCoord2f(p_187426_0_, p_187426_1_);
    }

    public static void glVertex3f(float p_187435_0_, float p_187435_1_, float p_187435_2_)
    {
        GL11.glVertex3f(p_187435_0_, p_187435_1_, p_187435_2_);
    }

    public static void resetColor()
    {
        colorState.red = -1.0F;
        colorState.green = -1.0F;
        colorState.blue = -1.0F;
        colorState.alpha = -1.0F;
    }

    public static void glNormalPointer(int p_187446_0_, int p_187446_1_, ByteBuffer p_187446_2_)
    {
        GL11.glNormalPointer(p_187446_0_, p_187446_1_, p_187446_2_);
    }

    public static void glTexCoordPointer(int p_187405_0_, int p_187405_1_, int p_187405_2_, int p_187405_3_)
    {
        GL11.glTexCoordPointer(p_187405_0_, p_187405_1_, p_187405_2_, (long)p_187405_3_);
    }

    public static void glTexCoordPointer(int p_187404_0_, int p_187404_1_, int p_187404_2_, ByteBuffer p_187404_3_)
    {
        GL11.glTexCoordPointer(p_187404_0_, p_187404_1_, p_187404_2_, p_187404_3_);
    }

    public static void glVertexPointer(int p_187420_0_, int p_187420_1_, int p_187420_2_, int p_187420_3_)
    {
        GL11.glVertexPointer(p_187420_0_, p_187420_1_, p_187420_2_, (long)p_187420_3_);
    }

    public static void glVertexPointer(int p_187427_0_, int p_187427_1_, int p_187427_2_, ByteBuffer p_187427_3_)
    {
        GL11.glVertexPointer(p_187427_0_, p_187427_1_, p_187427_2_, p_187427_3_);
    }

    public static void glColorPointer(int p_187406_0_, int p_187406_1_, int p_187406_2_, int p_187406_3_)
    {
        GL11.glColorPointer(p_187406_0_, p_187406_1_, p_187406_2_, (long)p_187406_3_);
    }

    public static void glColorPointer(int p_187400_0_, int p_187400_1_, int p_187400_2_, ByteBuffer p_187400_3_)
    {
        GL11.glColorPointer(p_187400_0_, p_187400_1_, p_187400_2_, p_187400_3_);
    }

    public static void glDisableClientState(int p_187429_0_)
    {
        GL11.glDisableClientState(p_187429_0_);
    }

    public static void glEnableClientState(int p_187410_0_)
    {
        GL11.glEnableClientState(p_187410_0_);
    }

    public static void glBegin(int p_187447_0_)
    {
        GL11.glBegin(p_187447_0_);
    }

    public static void glEnd()
    {
        GL11.glEnd();
    }

    public static void glDrawArrays(int p_187439_0_, int p_187439_1_, int p_187439_2_)
    {
        GL11.glDrawArrays(p_187439_0_, p_187439_1_, p_187439_2_);
    }

    public static void glLineWidth(float p_187441_0_)
    {
        GL11.glLineWidth(p_187441_0_);
    }

    public static void callList(int list)
    {
        GL11.glCallList(list);
    }

    public static void glDeleteLists(int p_187449_0_, int p_187449_1_)
    {
        GL11.glDeleteLists(p_187449_0_, p_187449_1_);
    }

    public static void glNewList(int p_187423_0_, int p_187423_1_)
    {
        GL11.glNewList(p_187423_0_, p_187423_1_);
    }

    public static void glEndList()
    {
        GL11.glEndList();
    }

    public static int glGenLists(int p_187442_0_)
    {
        return GL11.glGenLists(p_187442_0_);
    }

    public static void glPixelStorei(int p_187425_0_, int p_187425_1_)
    {
        GL11.glPixelStorei(p_187425_0_, p_187425_1_);
    }

    public static void glReadPixels(int p_187413_0_, int p_187413_1_, int p_187413_2_, int p_187413_3_, int p_187413_4_, int p_187413_5_, IntBuffer p_187413_6_)
    {
        GL11.glReadPixels(p_187413_0_, p_187413_1_, p_187413_2_, p_187413_3_, p_187413_4_, p_187413_5_, p_187413_6_);
    }

    public static int glGetError()
    {
        return GL11.glGetError();
    }

    public static String glGetString(int p_187416_0_)
    {
        return GL11.glGetString(p_187416_0_);
    }

    public static int glGetInteger(int p_187397_0_)
    {
        return GL11.glGetInteger(p_187397_0_);
    }

    public static void bindCurrentTexture()
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureState[activeTextureUnit].textureName);
    }

    public static int getBoundTexture()
    {
        return textureState[activeTextureUnit].textureName;
    }


    public static void deleteTextures(IntBuffer p_deleteTextures_0_)
    {
        p_deleteTextures_0_.rewind();

        while (p_deleteTextures_0_.position() < p_deleteTextures_0_.limit())
        {
            int i = p_deleteTextures_0_.get();
            deleteTexture(i);
        }

        p_deleteTextures_0_.rewind();
    }

    public static boolean isFogEnabled()
    {
        return fogState.fog.currentState;
    }

    public static void setFogEnabled(boolean p_setFogEnabled_0_)
    {
        fogState.fog.setState(p_setFogEnabled_0_);
    }

    static
    {
        for (int i = 0; i < 8; ++i)
        {
            lightState[i] = new BooleanState(16384 + i);
        }

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
        textureState = new TextureState[32];

        for (int j = 0; j < textureState.length; ++j)
        {
            textureState[j] = new TextureState();
        }

        activeShadeModel = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask();
        colorState = new Color();
    }
    static boolean arbMultitexture;
    static int defaultTexUnit;
    static{
        defaultTexUnit = 33984;
    }

    public static void setActiveTexture(int texture)
    {
        if (activeTextureUnit != texture - defaultTexUnit)
        {
            activeTextureUnit = texture - defaultTexUnit;
            GL13.glActiveTexture(texture);
        }
    }
    static class AlphaState
    {
        public BooleanState alphaTest;
        public int func;
        public float ref;

        private AlphaState()
        {
            this.alphaTest = new BooleanState(3008);
            this.func = 519;
            this.ref = -1.0F;
        }
    }

    static class BlendState
    {
        public BooleanState blend;
        public int srcFactor;
        public int dstFactor;
        public int srcFactorAlpha;
        public int dstFactorAlpha;

        private BlendState()
        {
            this.blend = new BooleanState(3042);
            this.srcFactor = 1;
            this.dstFactor = 0;
            this.srcFactorAlpha = 1;
            this.dstFactorAlpha = 0;
        }
    }

    static class BooleanState
    {
        private final int capability;
        private boolean currentState;

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

    static class ClearState
    {
        public double depth;
        public Color color;

        private ClearState()
        {
            this.depth = 1.0D;
            this.color = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    static class Color
    {
        public float red;
        public float green;
        public float blue;
        public float alpha;

        public Color()
        {
            this(1.0F, 1.0F, 1.0F, 1.0F);
        }

        public Color(float redIn, float greenIn, float blueIn, float alphaIn)
        {
            this.red = 1.0F;
            this.green = 1.0F;
            this.blue = 1.0F;
            this.alpha = 1.0F;
            this.red = redIn;
            this.green = greenIn;
            this.blue = blueIn;
            this.alpha = alphaIn;
        }
    }

    static class ColorLogicState
    {
        public BooleanState colorLogicOp;
        public int opcode;

        private ColorLogicState()
        {
            this.colorLogicOp = new BooleanState(3058);
            this.opcode = 5379;
        }
    }

    static class ColorMask
    {
        public boolean red;
        public boolean green;
        public boolean blue;
        public boolean alpha;

        private ColorMask()
        {
            this.red = true;
            this.green = true;
            this.blue = true;
            this.alpha = true;
        }
    }

    static class ColorMaterialState
    {
        public BooleanState colorMaterial;
        public int face;
        public int mode;

        private ColorMaterialState()
        {
            this.colorMaterial = new BooleanState(2903);
            this.face = 1032;
            this.mode = 5634;
        }
    }

    public static enum CullFace
    {
        FRONT(1028),
        BACK(1029),
        FRONT_AND_BACK(1032);

        public final int mode;

        private CullFace(int modeIn)
        {
            this.mode = modeIn;
        }
    }

    static class CullState
    {
        public BooleanState cullFace;
        public int mode;

        private CullState()
        {
            this.cullFace = new BooleanState(2884);
            this.mode = 1029;
        }
    }

    static class DepthState
    {
        public BooleanState depthTest;
        public boolean maskEnabled;
        public int depthFunc;

        private DepthState()
        {
            this.depthTest = new BooleanState(2929);
            this.maskEnabled = true;
            this.depthFunc = 513;
        }
    }

    public static enum DestFactor
    {
        CONSTANT_ALPHA(32771),
        CONSTANT_COLOR(32769),
        DST_ALPHA(772),
        DST_COLOR(774),
        ONE(1),
        ONE_MINUS_CONSTANT_ALPHA(32772),
        ONE_MINUS_CONSTANT_COLOR(32770),
        ONE_MINUS_DST_ALPHA(773),
        ONE_MINUS_DST_COLOR(775),
        ONE_MINUS_SRC_ALPHA(771),
        ONE_MINUS_SRC_COLOR(769),
        SRC_ALPHA(770),
        SRC_COLOR(768),
        ZERO(0);

        public final int factor;

        private DestFactor(int factorIn)
        {
            this.factor = factorIn;
        }
    }

    public static enum FogMode
    {
        LINEAR(9729),
        EXP(2048),
        EXP2(2049);

        public final int capabilityId;

        private FogMode(int capabilityIn)
        {
            this.capabilityId = capabilityIn;
        }
    }

    static class FogState
    {
        public BooleanState fog;
        public int mode;
        public float density;
        public float start;
        public float end;

        private FogState()
        {
            this.fog = new BooleanState(2912);
            this.mode = 2048;
            this.density = 1.0F;
            this.end = 1.0F;
        }
    }

    public static enum LogicOp
    {
        AND(5377),
        AND_INVERTED(5380),
        AND_REVERSE(5378),
        CLEAR(5376),
        COPY(5379),
        COPY_INVERTED(5388),
        EQUIV(5385),
        INVERT(5386),
        NAND(5390),
        NOOP(5381),
        NOR(5384),
        OR(5383),
        OR_INVERTED(5389),
        OR_REVERSE(5387),
        SET(5391),
        XOR(5382);

        public final int opcode;

        private LogicOp(int opcodeIn)
        {
            this.opcode = opcodeIn;
        }
    }

    static class PolygonOffsetState
    {
        public BooleanState polygonOffsetFill;
        public BooleanState polygonOffsetLine;
        public float factor;
        public float units;

        private PolygonOffsetState()
        {
            this.polygonOffsetFill = new BooleanState(32823);
            this.polygonOffsetLine = new BooleanState(10754);
        }
    }

    public static enum SourceFactor
    {
        CONSTANT_ALPHA(32771),
        CONSTANT_COLOR(32769),
        DST_ALPHA(772),
        DST_COLOR(774),
        ONE(1),
        ONE_MINUS_CONSTANT_ALPHA(32772),
        ONE_MINUS_CONSTANT_COLOR(32770),
        ONE_MINUS_DST_ALPHA(773),
        ONE_MINUS_DST_COLOR(775),
        ONE_MINUS_SRC_ALPHA(771),
        ONE_MINUS_SRC_COLOR(769),
        SRC_ALPHA(770),
        SRC_ALPHA_SATURATE(776),
        SRC_COLOR(768),
        ZERO(0);

        public final int factor;

        private SourceFactor(int factorIn)
        {
            this.factor = factorIn;
        }
    }

    static class StencilFunc
    {
        public int func;
        public int mask;

        private StencilFunc()
        {
            this.func = 519;
            this.mask = -1;
        }
    }

    static class StencilState
    {
        public StencilFunc func;
        public int mask;
        public int fail;
        public int zfail;
        public int zpass;

        private StencilState()
        {
            this.func = new StencilFunc();
            this.mask = -1;
            this.fail = 7680;
            this.zfail = 7680;
            this.zpass = 7680;
        }
    }

    public static enum TexGen
    {
        S,
        T,
        R,
        Q;
    }

    static class TexGenCoord
    {
        public BooleanState textureGen;
        public int coord;
        public int param = -1;

        public TexGenCoord(int coordIn, int capabilityIn)
        {
            this.coord = coordIn;
            this.textureGen = new BooleanState(capabilityIn);
        }
    }

    static class TexGenState
    {
        public TexGenCoord s;
        public TexGenCoord t;
        public TexGenCoord r;
        public TexGenCoord q;

        private TexGenState()
        {
            this.s = new TexGenCoord(8192, 3168);
            this.t = new TexGenCoord(8193, 3169);
            this.r = new TexGenCoord(8194, 3170);
            this.q = new TexGenCoord(8195, 3171);
        }
    }

    public static class TextureState
    {
        public BooleanState texture2DState;
        public int textureName;

        private TextureState()
        {
            this.texture2DState = new BooleanState(3553);
        }
    }
}
