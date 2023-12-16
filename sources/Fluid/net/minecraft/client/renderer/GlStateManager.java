// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import org.lwjgl.opengl.GL14;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import net.optifine.SmartAnimations;
import java.nio.FloatBuffer;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import org.lwjgl.opengl.GL11;
import net.optifine.render.GlBlendState;
import net.optifine.render.GlAlphaState;
import net.optifine.util.LockCounter;

public class GlStateManager
{
    private static AlphaState alphaState;
    private static BooleanState lightingState;
    private static BooleanState[] lightState;
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
    private static int activeTextureUnit;
    private static TextureState[] textureState;
    private static int activeShadeModel;
    private static BooleanState rescaleNormalState;
    private static ColorMask colorMaskState;
    private static Color colorState;
    public static boolean clearEnabled;
    private static LockCounter alphaLock;
    private static GlAlphaState alphaLockState;
    private static LockCounter blendLock;
    private static GlBlendState blendLockState;
    private static boolean creatingDisplayList;
    
    static {
        GlStateManager.alphaState = new AlphaState(null);
        GlStateManager.lightingState = new BooleanState(2896);
        GlStateManager.lightState = new BooleanState[8];
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
        GlStateManager.activeTextureUnit = 0;
        GlStateManager.textureState = new TextureState[32];
        GlStateManager.activeShadeModel = 7425;
        GlStateManager.rescaleNormalState = new BooleanState(32826);
        GlStateManager.colorMaskState = new ColorMask(null);
        GlStateManager.colorState = new Color();
        GlStateManager.clearEnabled = true;
        GlStateManager.alphaLock = new LockCounter();
        GlStateManager.alphaLockState = new GlAlphaState();
        GlStateManager.blendLock = new LockCounter();
        GlStateManager.blendLockState = new GlBlendState();
        GlStateManager.creatingDisplayList = false;
        for (int i = 0; i < 8; ++i) {
            GlStateManager.lightState[i] = new BooleanState(16384 + i);
        }
        for (int j = 0; j < GlStateManager.textureState.length; ++j) {
            GlStateManager.textureState[j] = new TextureState(null);
        }
    }
    
    public static void pushAttrib() {
        GL11.glPushAttrib(8256);
    }
    
    public static void popAttrib() {
        GL11.glPopAttrib();
    }
    
    public static void disableAlpha() {
        if (GlStateManager.alphaLock.isLocked()) {
            GlStateManager.alphaLockState.setDisabled();
        }
        else {
            GlStateManager.alphaState.alphaTest.setDisabled();
        }
    }
    
    public static void enableAlpha() {
        if (GlStateManager.alphaLock.isLocked()) {
            GlStateManager.alphaLockState.setEnabled();
        }
        else {
            GlStateManager.alphaState.alphaTest.setEnabled();
        }
    }
    
    public static void alphaFunc(final int func, final float ref) {
        if (GlStateManager.alphaLock.isLocked()) {
            GlStateManager.alphaLockState.setFuncRef(func, ref);
        }
        else if (func != GlStateManager.alphaState.func || ref != GlStateManager.alphaState.ref) {
            GL11.glAlphaFunc(GlStateManager.alphaState.func = func, GlStateManager.alphaState.ref = ref);
        }
    }
    
    public static void enableLighting() {
        GlStateManager.lightingState.setEnabled();
    }
    
    public static void disableLighting() {
        GlStateManager.lightingState.setDisabled();
    }
    
    public static void enableLight(final int light) {
        GlStateManager.lightState[light].setEnabled();
    }
    
    public static void disableLight(final int light) {
        GlStateManager.lightState[light].setDisabled();
    }
    
    public static void enableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setEnabled();
    }
    
    public static void disableColorMaterial() {
        GlStateManager.colorMaterialState.colorMaterial.setDisabled();
    }
    
    public static void colorMaterial(final int face, final int mode) {
        if (face != GlStateManager.colorMaterialState.face || mode != GlStateManager.colorMaterialState.mode) {
            GL11.glColorMaterial(GlStateManager.colorMaterialState.face = face, GlStateManager.colorMaterialState.mode = mode);
        }
    }
    
    public static void disableDepth() {
        GlStateManager.depthState.depthTest.setDisabled();
    }
    
    public static void enableDepth() {
        GlStateManager.depthState.depthTest.setEnabled();
    }
    
    public static void depthFunc(final int depthFunc) {
        if (depthFunc != GlStateManager.depthState.depthFunc) {
            GL11.glDepthFunc(GlStateManager.depthState.depthFunc = depthFunc);
        }
    }
    
    public static void depthMask(final boolean flagIn) {
        if (flagIn != GlStateManager.depthState.maskEnabled) {
            GL11.glDepthMask(GlStateManager.depthState.maskEnabled = flagIn);
        }
    }
    
    public static void disableBlend() {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setDisabled();
        }
        else {
            GlStateManager.blendState.blend.setDisabled();
        }
    }
    
    public static void enableBlend() {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setEnabled();
        }
        else {
            GlStateManager.blendState.blend.setEnabled();
        }
    }
    
    public static void blendFunc(final int srcFactor, final int dstFactor) {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setFactors(srcFactor, dstFactor);
        }
        else if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactor != GlStateManager.blendState.srcFactorAlpha || dstFactor != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = srcFactor;
            GlStateManager.blendState.dstFactor = dstFactor;
            GlStateManager.blendState.srcFactorAlpha = srcFactor;
            GlStateManager.blendState.dstFactorAlpha = dstFactor;
            if (Config.isShaders()) {
                Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactor, dstFactor);
            }
            GL11.glBlendFunc(srcFactor, dstFactor);
        }
    }
    
    public static void tryBlendFuncSeparate(final int srcFactor, final int dstFactor, final int srcFactorAlpha, final int dstFactorAlpha) {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setFactors(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
        }
        else if (srcFactor != GlStateManager.blendState.srcFactor || dstFactor != GlStateManager.blendState.dstFactor || srcFactorAlpha != GlStateManager.blendState.srcFactorAlpha || dstFactorAlpha != GlStateManager.blendState.dstFactorAlpha) {
            GlStateManager.blendState.srcFactor = srcFactor;
            GlStateManager.blendState.dstFactor = dstFactor;
            GlStateManager.blendState.srcFactorAlpha = srcFactorAlpha;
            GlStateManager.blendState.dstFactorAlpha = dstFactorAlpha;
            if (Config.isShaders()) {
                Shaders.uniform_blendFunc.setValue(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
            }
            OpenGlHelper.glBlendFunc(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
        }
    }
    
    public static void enableFog() {
        GlStateManager.fogState.fog.setEnabled();
    }
    
    public static void disableFog() {
        GlStateManager.fogState.fog.setDisabled();
    }
    
    public static void setFog(final int param) {
        if (param != GlStateManager.fogState.mode) {
            GL11.glFogi(2917, GlStateManager.fogState.mode = param);
            if (Config.isShaders()) {
                Shaders.setFogMode(param);
            }
        }
    }
    
    public static void setFogDensity(float param) {
        if (param < 0.0f) {
            param = 0.0f;
        }
        if (param != GlStateManager.fogState.density) {
            GL11.glFogf(2914, GlStateManager.fogState.density = param);
            if (Config.isShaders()) {
                Shaders.setFogDensity(param);
            }
        }
    }
    
    public static void setFogStart(final float param) {
        if (param != GlStateManager.fogState.start) {
            GL11.glFogf(2915, GlStateManager.fogState.start = param);
        }
    }
    
    public static void setFogEnd(final float param) {
        if (param != GlStateManager.fogState.end) {
            GL11.glFogf(2916, GlStateManager.fogState.end = param);
        }
    }
    
    public static void glFog(final int p_glFog_0_, final FloatBuffer p_glFog_1_) {
        GL11.glFog(p_glFog_0_, p_glFog_1_);
    }
    
    public static void glFogi(final int p_glFogi_0_, final int p_glFogi_1_) {
        GL11.glFogi(p_glFogi_0_, p_glFogi_1_);
    }
    
    public static void enableCull() {
        GlStateManager.cullState.cullFace.setEnabled();
    }
    
    public static void disableCull() {
        GlStateManager.cullState.cullFace.setDisabled();
    }
    
    public static void cullFace(final int mode) {
        if (mode != GlStateManager.cullState.mode) {
            GL11.glCullFace(GlStateManager.cullState.mode = mode);
        }
    }
    
    public static void enablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setEnabled();
    }
    
    public static void disablePolygonOffset() {
        GlStateManager.polygonOffsetState.polygonOffsetFill.setDisabled();
    }
    
    public static void doPolygonOffset(final float factor, final float units) {
        if (factor != GlStateManager.polygonOffsetState.factor || units != GlStateManager.polygonOffsetState.units) {
            GL11.glPolygonOffset(GlStateManager.polygonOffsetState.factor = factor, GlStateManager.polygonOffsetState.units = units);
        }
    }
    
    public static void enableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setEnabled();
    }
    
    public static void disableColorLogic() {
        GlStateManager.colorLogicState.colorLogicOp.setDisabled();
    }
    
    public static void colorLogicOp(final int opcode) {
        if (opcode != GlStateManager.colorLogicState.opcode) {
            GL11.glLogicOp(GlStateManager.colorLogicState.opcode = opcode);
        }
    }
    
    public static void enableTexGenCoord(final TexGen p_179087_0_) {
        texGenCoord(p_179087_0_).textureGen.setEnabled();
    }
    
    public static void disableTexGenCoord(final TexGen p_179100_0_) {
        texGenCoord(p_179100_0_).textureGen.setDisabled();
    }
    
    public static void texGen(final TexGen texGen, final int param) {
        final TexGenCoord glstatemanager$texgencoord = texGenCoord(texGen);
        if (param != glstatemanager$texgencoord.param) {
            glstatemanager$texgencoord.param = param;
            GL11.glTexGeni(glstatemanager$texgencoord.coord, 9472, param);
        }
    }
    
    public static void texGen(final TexGen p_179105_0_, final int pname, final FloatBuffer params) {
        GL11.glTexGen(texGenCoord(p_179105_0_).coord, pname, params);
    }
    
    private static TexGenCoord texGenCoord(final TexGen p_179125_0_) {
        switch (p_179125_0_) {
            case S: {
                return GlStateManager.texGenState.s;
            }
            case T: {
                return GlStateManager.texGenState.t;
            }
            case R: {
                return GlStateManager.texGenState.r;
            }
            case Q: {
                return GlStateManager.texGenState.q;
            }
            default: {
                return GlStateManager.texGenState.s;
            }
        }
    }
    
    public static void setActiveTexture(final int texture) {
        if (GlStateManager.activeTextureUnit != texture - OpenGlHelper.defaultTexUnit) {
            GlStateManager.activeTextureUnit = texture - OpenGlHelper.defaultTexUnit;
            OpenGlHelper.setActiveTexture(texture);
        }
    }
    
    public static void enableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setEnabled();
    }
    
    public static void disableTexture2D() {
        GlStateManager.textureState[GlStateManager.activeTextureUnit].texture2DState.setDisabled();
    }
    
    public static int generateTexture() {
        return GL11.glGenTextures();
    }
    
    public static void deleteTexture(final int texture) {
        if (texture != 0) {
            GL11.glDeleteTextures(texture);
            TextureState[] textureState;
            for (int length = (textureState = GlStateManager.textureState).length, i = 0; i < length; ++i) {
                final TextureState glstatemanager$texturestate = textureState[i];
                if (glstatemanager$texturestate.textureName == texture) {
                    glstatemanager$texturestate.textureName = 0;
                }
            }
        }
    }
    
    public static void bindTexture(final int texture) {
        if (texture != GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName) {
            GL11.glBindTexture(3553, GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName = texture);
            if (SmartAnimations.isActive()) {
                SmartAnimations.textureRendered(texture);
            }
        }
    }
    
    public static void enableNormalize() {
        GlStateManager.normalizeState.setEnabled();
    }
    
    public static void disableNormalize() {
        GlStateManager.normalizeState.setDisabled();
    }
    
    public static void shadeModel(final int mode) {
        if (mode != GlStateManager.activeShadeModel) {
            GL11.glShadeModel(GlStateManager.activeShadeModel = mode);
        }
    }
    
    public static void enableRescaleNormal() {
        GlStateManager.rescaleNormalState.setEnabled();
    }
    
    public static void disableRescaleNormal() {
        GlStateManager.rescaleNormalState.setDisabled();
    }
    
    public static void viewport(final int x, final int y, final int width, final int height) {
        GL11.glViewport(x, y, width, height);
    }
    
    public static void colorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
        if (red != GlStateManager.colorMaskState.red || green != GlStateManager.colorMaskState.green || blue != GlStateManager.colorMaskState.blue || alpha != GlStateManager.colorMaskState.alpha) {
            GL11.glColorMask(GlStateManager.colorMaskState.red = red, GlStateManager.colorMaskState.green = green, GlStateManager.colorMaskState.blue = blue, GlStateManager.colorMaskState.alpha = alpha);
        }
    }
    
    public static void clearDepth(final double depth) {
        if (depth != GlStateManager.clearState.depth) {
            GL11.glClearDepth(GlStateManager.clearState.depth = depth);
        }
    }
    
    public static void clearColor(final float red, final float green, final float blue, final float alpha) {
        if (red != GlStateManager.clearState.color.red || green != GlStateManager.clearState.color.green || blue != GlStateManager.clearState.color.blue || alpha != GlStateManager.clearState.color.alpha) {
            GL11.glClearColor(GlStateManager.clearState.color.red = red, GlStateManager.clearState.color.green = green, GlStateManager.clearState.color.blue = blue, GlStateManager.clearState.color.alpha = alpha);
        }
    }
    
    public static void clear(final int mask) {
        if (GlStateManager.clearEnabled) {
            GL11.glClear(mask);
        }
    }
    
    public static void matrixMode(final int mode) {
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
    
    public static void getFloat(final int pname, final FloatBuffer params) {
        GL11.glGetFloat(pname, params);
    }
    
    public static void ortho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        GL11.glOrtho(left, right, bottom, top, zNear, zFar);
    }
    
    public static void rotate(final float angle, final float x, final float y, final float z) {
        GL11.glRotatef(angle, x, y, z);
    }
    
    public static void scale(final float x, final float y, final float z) {
        GL11.glScalef(x, y, z);
    }
    
    public static void scale(final double x, final double y, final double z) {
        GL11.glScaled(x, y, z);
    }
    
    public static void translate(final float x, final float y, final float z) {
        GL11.glTranslatef(x, y, z);
    }
    
    public static void translate(final double x, final double y, final double z) {
        GL11.glTranslated(x, y, z);
    }
    
    public static void multMatrix(final FloatBuffer matrix) {
        GL11.glMultMatrix(matrix);
    }
    
    public static void color(final float colorRed, final float colorGreen, final float colorBlue, final float colorAlpha) {
        if (colorRed != GlStateManager.colorState.red || colorGreen != GlStateManager.colorState.green || colorBlue != GlStateManager.colorState.blue || colorAlpha != GlStateManager.colorState.alpha) {
            GL11.glColor4f(GlStateManager.colorState.red = colorRed, GlStateManager.colorState.green = colorGreen, GlStateManager.colorState.blue = colorBlue, GlStateManager.colorState.alpha = colorAlpha);
        }
    }
    
    public static void color(final float colorRed, final float colorGreen, final float colorBlue) {
        color(colorRed, colorGreen, colorBlue, 1.0f);
    }
    
    public static void resetColor() {
        final Color colorState = GlStateManager.colorState;
        final Color colorState2 = GlStateManager.colorState;
        final Color colorState3 = GlStateManager.colorState;
        final Color colorState4 = GlStateManager.colorState;
        final float n = -1.0f;
        colorState4.alpha = n;
        colorState3.blue = n;
        colorState2.green = n;
        colorState.red = n;
    }
    
    public static void glNormalPointer(final int p_glNormalPointer_0_, final int p_glNormalPointer_1_, final ByteBuffer p_glNormalPointer_2_) {
        GL11.glNormalPointer(p_glNormalPointer_0_, p_glNormalPointer_1_, p_glNormalPointer_2_);
    }
    
    public static void glTexCoordPointer(final int p_glTexCoordPointer_0_, final int p_glTexCoordPointer_1_, final int p_glTexCoordPointer_2_, final int p_glTexCoordPointer_3_) {
        GL11.glTexCoordPointer(p_glTexCoordPointer_0_, p_glTexCoordPointer_1_, p_glTexCoordPointer_2_, (long)p_glTexCoordPointer_3_);
    }
    
    public static void glTexCoordPointer(final int p_glTexCoordPointer_0_, final int p_glTexCoordPointer_1_, final int p_glTexCoordPointer_2_, final ByteBuffer p_glTexCoordPointer_3_) {
        GL11.glTexCoordPointer(p_glTexCoordPointer_0_, p_glTexCoordPointer_1_, p_glTexCoordPointer_2_, p_glTexCoordPointer_3_);
    }
    
    public static void glVertexPointer(final int p_glVertexPointer_0_, final int p_glVertexPointer_1_, final int p_glVertexPointer_2_, final int p_glVertexPointer_3_) {
        GL11.glVertexPointer(p_glVertexPointer_0_, p_glVertexPointer_1_, p_glVertexPointer_2_, (long)p_glVertexPointer_3_);
    }
    
    public static void glVertexPointer(final int p_glVertexPointer_0_, final int p_glVertexPointer_1_, final int p_glVertexPointer_2_, final ByteBuffer p_glVertexPointer_3_) {
        GL11.glVertexPointer(p_glVertexPointer_0_, p_glVertexPointer_1_, p_glVertexPointer_2_, p_glVertexPointer_3_);
    }
    
    public static void glColorPointer(final int p_glColorPointer_0_, final int p_glColorPointer_1_, final int p_glColorPointer_2_, final int p_glColorPointer_3_) {
        GL11.glColorPointer(p_glColorPointer_0_, p_glColorPointer_1_, p_glColorPointer_2_, (long)p_glColorPointer_3_);
    }
    
    public static void glColorPointer(final int p_glColorPointer_0_, final int p_glColorPointer_1_, final int p_glColorPointer_2_, final ByteBuffer p_glColorPointer_3_) {
        GL11.glColorPointer(p_glColorPointer_0_, p_glColorPointer_1_, p_glColorPointer_2_, p_glColorPointer_3_);
    }
    
    public static void glDisableClientState(final int p_glDisableClientState_0_) {
        GL11.glDisableClientState(p_glDisableClientState_0_);
    }
    
    public static void glEnableClientState(final int p_glEnableClientState_0_) {
        GL11.glEnableClientState(p_glEnableClientState_0_);
    }
    
    public static void glBegin(final int p_glBegin_0_) {
        GL11.glBegin(p_glBegin_0_);
    }
    
    public static void glEnd() {
        GL11.glEnd();
    }
    
    public static void glDrawArrays(final int p_glDrawArrays_0_, final int p_glDrawArrays_1_, final int p_glDrawArrays_2_) {
        GL11.glDrawArrays(p_glDrawArrays_0_, p_glDrawArrays_1_, p_glDrawArrays_2_);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL11.glDrawArrays(p_glDrawArrays_0_, p_glDrawArrays_1_, p_glDrawArrays_2_);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public static void callList(final int list) {
        GL11.glCallList(list);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL11.glCallList(list);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public static void callLists(final IntBuffer p_callLists_0_) {
        GL11.glCallLists(p_callLists_0_);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL11.glCallLists(p_callLists_0_);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public static void glDeleteLists(final int p_glDeleteLists_0_, final int p_glDeleteLists_1_) {
        GL11.glDeleteLists(p_glDeleteLists_0_, p_glDeleteLists_1_);
    }
    
    public static void glNewList(final int p_glNewList_0_, final int p_glNewList_1_) {
        GL11.glNewList(p_glNewList_0_, p_glNewList_1_);
        GlStateManager.creatingDisplayList = true;
    }
    
    public static void glEndList() {
        GL11.glEndList();
        GlStateManager.creatingDisplayList = false;
    }
    
    public static int glGetError() {
        return GL11.glGetError();
    }
    
    public static void glTexImage2D(final int p_glTexImage2D_0_, final int p_glTexImage2D_1_, final int p_glTexImage2D_2_, final int p_glTexImage2D_3_, final int p_glTexImage2D_4_, final int p_glTexImage2D_5_, final int p_glTexImage2D_6_, final int p_glTexImage2D_7_, final IntBuffer p_glTexImage2D_8_) {
        GL11.glTexImage2D(p_glTexImage2D_0_, p_glTexImage2D_1_, p_glTexImage2D_2_, p_glTexImage2D_3_, p_glTexImage2D_4_, p_glTexImage2D_5_, p_glTexImage2D_6_, p_glTexImage2D_7_, p_glTexImage2D_8_);
    }
    
    public static void glTexSubImage2D(final int p_glTexSubImage2D_0_, final int p_glTexSubImage2D_1_, final int p_glTexSubImage2D_2_, final int p_glTexSubImage2D_3_, final int p_glTexSubImage2D_4_, final int p_glTexSubImage2D_5_, final int p_glTexSubImage2D_6_, final int p_glTexSubImage2D_7_, final IntBuffer p_glTexSubImage2D_8_) {
        GL11.glTexSubImage2D(p_glTexSubImage2D_0_, p_glTexSubImage2D_1_, p_glTexSubImage2D_2_, p_glTexSubImage2D_3_, p_glTexSubImage2D_4_, p_glTexSubImage2D_5_, p_glTexSubImage2D_6_, p_glTexSubImage2D_7_, p_glTexSubImage2D_8_);
    }
    
    public static void glCopyTexSubImage2D(final int p_glCopyTexSubImage2D_0_, final int p_glCopyTexSubImage2D_1_, final int p_glCopyTexSubImage2D_2_, final int p_glCopyTexSubImage2D_3_, final int p_glCopyTexSubImage2D_4_, final int p_glCopyTexSubImage2D_5_, final int p_glCopyTexSubImage2D_6_, final int p_glCopyTexSubImage2D_7_) {
        GL11.glCopyTexSubImage2D(p_glCopyTexSubImage2D_0_, p_glCopyTexSubImage2D_1_, p_glCopyTexSubImage2D_2_, p_glCopyTexSubImage2D_3_, p_glCopyTexSubImage2D_4_, p_glCopyTexSubImage2D_5_, p_glCopyTexSubImage2D_6_, p_glCopyTexSubImage2D_7_);
    }
    
    public static void glGetTexImage(final int p_glGetTexImage_0_, final int p_glGetTexImage_1_, final int p_glGetTexImage_2_, final int p_glGetTexImage_3_, final IntBuffer p_glGetTexImage_4_) {
        GL11.glGetTexImage(p_glGetTexImage_0_, p_glGetTexImage_1_, p_glGetTexImage_2_, p_glGetTexImage_3_, p_glGetTexImage_4_);
    }
    
    public static void glTexParameterf(final int p_glTexParameterf_0_, final int p_glTexParameterf_1_, final float p_glTexParameterf_2_) {
        GL11.glTexParameterf(p_glTexParameterf_0_, p_glTexParameterf_1_, p_glTexParameterf_2_);
    }
    
    public static void glTexParameteri(final int p_glTexParameteri_0_, final int p_glTexParameteri_1_, final int p_glTexParameteri_2_) {
        GL11.glTexParameteri(p_glTexParameteri_0_, p_glTexParameteri_1_, p_glTexParameteri_2_);
    }
    
    public static int glGetTexLevelParameteri(final int p_glGetTexLevelParameteri_0_, final int p_glGetTexLevelParameteri_1_, final int p_glGetTexLevelParameteri_2_) {
        return GL11.glGetTexLevelParameteri(p_glGetTexLevelParameteri_0_, p_glGetTexLevelParameteri_1_, p_glGetTexLevelParameteri_2_);
    }
    
    public static int getActiveTextureUnit() {
        return OpenGlHelper.defaultTexUnit + GlStateManager.activeTextureUnit;
    }
    
    public static void bindCurrentTexture() {
        GL11.glBindTexture(3553, GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName);
    }
    
    public static int getBoundTexture() {
        return GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName;
    }
    
    public static void checkBoundTexture() {
        if (Config.isMinecraftThread()) {
            final int i = GL11.glGetInteger(34016);
            final int j = GL11.glGetInteger(32873);
            final int k = getActiveTextureUnit();
            final int l = getBoundTexture();
            if (l > 0 && (i != k || j != l)) {
                Config.dbg("checkTexture: act: " + k + ", glAct: " + i + ", tex: " + l + ", glTex: " + j);
            }
        }
    }
    
    public static void deleteTextures(final IntBuffer p_deleteTextures_0_) {
        p_deleteTextures_0_.rewind();
        while (p_deleteTextures_0_.position() < p_deleteTextures_0_.limit()) {
            final int i = p_deleteTextures_0_.get();
            deleteTexture(i);
        }
        p_deleteTextures_0_.rewind();
    }
    
    public static boolean isFogEnabled() {
        return GlStateManager.fogState.fog.currentState;
    }
    
    public static void setFogEnabled(final boolean p_setFogEnabled_0_) {
        GlStateManager.fogState.fog.setState(p_setFogEnabled_0_);
    }
    
    public static void lockAlpha(final GlAlphaState p_lockAlpha_0_) {
        if (!GlStateManager.alphaLock.isLocked()) {
            getAlphaState(GlStateManager.alphaLockState);
            setAlphaState(p_lockAlpha_0_);
            GlStateManager.alphaLock.lock();
        }
    }
    
    public static void unlockAlpha() {
        if (GlStateManager.alphaLock.unlock()) {
            setAlphaState(GlStateManager.alphaLockState);
        }
    }
    
    public static void getAlphaState(final GlAlphaState p_getAlphaState_0_) {
        if (GlStateManager.alphaLock.isLocked()) {
            p_getAlphaState_0_.setState(GlStateManager.alphaLockState);
        }
        else {
            p_getAlphaState_0_.setState(GlStateManager.alphaState.alphaTest.currentState, GlStateManager.alphaState.func, GlStateManager.alphaState.ref);
        }
    }
    
    public static void setAlphaState(final GlAlphaState p_setAlphaState_0_) {
        if (GlStateManager.alphaLock.isLocked()) {
            GlStateManager.alphaLockState.setState(p_setAlphaState_0_);
        }
        else {
            GlStateManager.alphaState.alphaTest.setState(p_setAlphaState_0_.isEnabled());
            alphaFunc(p_setAlphaState_0_.getFunc(), p_setAlphaState_0_.getRef());
        }
    }
    
    public static void lockBlend(final GlBlendState p_lockBlend_0_) {
        if (!GlStateManager.blendLock.isLocked()) {
            getBlendState(GlStateManager.blendLockState);
            setBlendState(p_lockBlend_0_);
            GlStateManager.blendLock.lock();
        }
    }
    
    public static void unlockBlend() {
        if (GlStateManager.blendLock.unlock()) {
            setBlendState(GlStateManager.blendLockState);
        }
    }
    
    public static void getBlendState(final GlBlendState p_getBlendState_0_) {
        if (GlStateManager.blendLock.isLocked()) {
            p_getBlendState_0_.setState(GlStateManager.blendLockState);
        }
        else {
            p_getBlendState_0_.setState(GlStateManager.blendState.blend.currentState, GlStateManager.blendState.srcFactor, GlStateManager.blendState.dstFactor, GlStateManager.blendState.srcFactorAlpha, GlStateManager.blendState.dstFactorAlpha);
        }
    }
    
    public static void setBlendState(final GlBlendState p_setBlendState_0_) {
        if (GlStateManager.blendLock.isLocked()) {
            GlStateManager.blendLockState.setState(p_setBlendState_0_);
        }
        else {
            GlStateManager.blendState.blend.setState(p_setBlendState_0_.isEnabled());
            if (!p_setBlendState_0_.isSeparate()) {
                blendFunc(p_setBlendState_0_.getSrcFactor(), p_setBlendState_0_.getDstFactor());
            }
            else {
                tryBlendFuncSeparate(p_setBlendState_0_.getSrcFactor(), p_setBlendState_0_.getDstFactor(), p_setBlendState_0_.getSrcFactorAlpha(), p_setBlendState_0_.getDstFactorAlpha());
            }
        }
    }
    
    public static void glMultiDrawArrays(final int p_glMultiDrawArrays_0_, final IntBuffer p_glMultiDrawArrays_1_, final IntBuffer p_glMultiDrawArrays_2_) {
        GL14.glMultiDrawArrays(p_glMultiDrawArrays_0_, p_glMultiDrawArrays_1_, p_glMultiDrawArrays_2_);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL14.glMultiDrawArrays(p_glMultiDrawArrays_0_, p_glMultiDrawArrays_1_, p_glMultiDrawArrays_2_);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public enum TexGen
    {
        S("S", 0), 
        T("T", 1), 
        R("R", 2), 
        Q("Q", 3);
        
        private TexGen(final String s, final int n) {
        }
    }
    
    static class AlphaState
    {
        public BooleanState alphaTest;
        public int func;
        public float ref;
        
        private AlphaState() {
            this.alphaTest = new BooleanState(3008);
            this.func = 519;
            this.ref = -1.0f;
        }
    }
    
    static class BlendState
    {
        public BooleanState blend;
        public int srcFactor;
        public int dstFactor;
        public int srcFactorAlpha;
        public int dstFactorAlpha;
        
        private BlendState() {
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
        
        public BooleanState(final int capabilityIn) {
            this.currentState = false;
            this.capability = capabilityIn;
        }
        
        public void setDisabled() {
            this.setState(false);
        }
        
        public void setEnabled() {
            this.setState(true);
        }
        
        public void setState(final boolean state) {
            if (state != this.currentState) {
                this.currentState = state;
                if (state) {
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
        public double depth;
        public Color color;
        public int field_179204_c;
        
        private ClearState() {
            this.depth = 1.0;
            this.color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
            this.field_179204_c = 0;
        }
    }
    
    static class Color
    {
        public float red;
        public float green;
        public float blue;
        public float alpha;
        
        public Color() {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
        }
        
        public Color(final float redIn, final float greenIn, final float blueIn, final float alphaIn) {
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
            this.alpha = 1.0f;
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
        
        private ColorLogicState() {
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
        
        private ColorMask() {
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
        
        private ColorMaterialState() {
            this.colorMaterial = new BooleanState(2903);
            this.face = 1032;
            this.mode = 5634;
        }
    }
    
    static class CullState
    {
        public BooleanState cullFace;
        public int mode;
        
        private CullState() {
            this.cullFace = new BooleanState(2884);
            this.mode = 1029;
        }
    }
    
    static class DepthState
    {
        public BooleanState depthTest;
        public boolean maskEnabled;
        public int depthFunc;
        
        private DepthState() {
            this.depthTest = new BooleanState(2929);
            this.maskEnabled = true;
            this.depthFunc = 513;
        }
    }
    
    static class FogState
    {
        public BooleanState fog;
        public int mode;
        public float density;
        public float start;
        public float end;
        
        private FogState() {
            this.fog = new BooleanState(2912);
            this.mode = 2048;
            this.density = 1.0f;
            this.start = 0.0f;
            this.end = 1.0f;
        }
    }
    
    static class PolygonOffsetState
    {
        public BooleanState polygonOffsetFill;
        public BooleanState polygonOffsetLine;
        public float factor;
        public float units;
        
        private PolygonOffsetState() {
            this.polygonOffsetFill = new BooleanState(32823);
            this.polygonOffsetLine = new BooleanState(10754);
            this.factor = 0.0f;
            this.units = 0.0f;
        }
    }
    
    static class StencilFunc
    {
        public int field_179081_a;
        public int field_179079_b;
        public int field_179080_c;
        
        private StencilFunc() {
            this.field_179081_a = 519;
            this.field_179079_b = 0;
            this.field_179080_c = -1;
        }
    }
    
    static class StencilState
    {
        public StencilFunc field_179078_a;
        public int field_179076_b;
        public int field_179077_c;
        public int field_179074_d;
        public int field_179075_e;
        
        private StencilState() {
            this.field_179078_a = new StencilFunc(null);
            this.field_179076_b = -1;
            this.field_179077_c = 7680;
            this.field_179074_d = 7680;
            this.field_179075_e = 7680;
        }
    }
    
    static class TexGenCoord
    {
        public BooleanState textureGen;
        public int coord;
        public int param;
        
        public TexGenCoord(final int p_i46254_1_, final int p_i46254_2_) {
            this.param = -1;
            this.coord = p_i46254_1_;
            this.textureGen = new BooleanState(p_i46254_2_);
        }
    }
    
    static class TexGenState
    {
        public TexGenCoord s;
        public TexGenCoord t;
        public TexGenCoord r;
        public TexGenCoord q;
        
        private TexGenState() {
            this.s = new TexGenCoord(8192, 3168);
            this.t = new TexGenCoord(8193, 3169);
            this.r = new TexGenCoord(8194, 3170);
            this.q = new TexGenCoord(8195, 3171);
        }
    }
    
    static class TextureState
    {
        public BooleanState texture2DState;
        public int textureName;
        
        private TextureState() {
            this.texture2DState = new BooleanState(3553);
            this.textureName = 0;
        }
    }
}
