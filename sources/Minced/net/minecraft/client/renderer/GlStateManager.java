// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.lwjgl.opengl.GLContext;
import org.lwjgl.BufferUtils;
import java.nio.ByteBuffer;
import org.lwjgl.util.vector.Quaternion;
import javax.annotation.Nullable;
import java.nio.IntBuffer;
import net.optifine.SmartAnimations;
import org.lwjgl.opengl.GL14;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import org.lwjgl.opengl.GL11;
import net.optifine.render.GlBlendState;
import net.optifine.render.GlAlphaState;
import net.optifine.util.LockCounter;
import java.nio.FloatBuffer;

public class GlStateManager
{
    private static final FloatBuffer BUF_FLOAT_16;
    private static final FloatBuffer BUF_FLOAT_4;
    private static final AlphaState alphaState;
    private static final BooleanState lightingState;
    private static final BooleanState[] lightState;
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
    private static int activeTextureUnit;
    private static final TextureState[] textureState;
    private static int activeShadeModel;
    private static final BooleanState rescaleNormalState;
    private static final ColorMask colorMaskState;
    private static final Color colorState;
    public static boolean clearEnabled;
    private static LockCounter alphaLock;
    private static GlAlphaState alphaLockState;
    private static LockCounter blendLock;
    private static GlBlendState blendLockState;
    private static boolean creatingDisplayList;
    
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
    
    public static void enable(final int cap) {
        GL11.glEnable(cap);
    }
    
    public static void disable(final int cap) {
        GL11.glDisable(cap);
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
    
    public static void glLight(final int light, final int pname, final FloatBuffer params) {
        GL11.glLight(light, pname, params);
    }
    
    public static void glLightModel(final int pname, final FloatBuffer params) {
        GL11.glLightModel(pname, params);
    }
    
    public static void glNormal3f(final float nx, final float ny, final float nz) {
        GL11.glNormal3f(nx, ny, nz);
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
    
    public static void blendFunc(final SourceFactor srcFactor, final DestFactor dstFactor) {
        blendFunc(srcFactor.factor, dstFactor.factor);
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
    
    public static void tryBlendFuncSeparate(final SourceFactor srcFactor, final DestFactor dstFactor, final SourceFactor srcFactorAlpha, final DestFactor dstFactorAlpha) {
        tryBlendFuncSeparate(srcFactor.factor, dstFactor.factor, srcFactorAlpha.factor, dstFactorAlpha.factor);
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
    
    public static void glBlendEquation(final int blendEquation) {
        GL14.glBlendEquation(blendEquation);
    }
    
    public static void enableOutlineMode(final int color) {
        GlStateManager.BUF_FLOAT_4.put(0, (color >> 16 & 0xFF) / 255.0f);
        GlStateManager.BUF_FLOAT_4.put(1, (color >> 8 & 0xFF) / 255.0f);
        GlStateManager.BUF_FLOAT_4.put(2, (color >> 0 & 0xFF) / 255.0f);
        GlStateManager.BUF_FLOAT_4.put(3, (color >> 24 & 0xFF) / 255.0f);
        glTexEnv(8960, 8705, GlStateManager.BUF_FLOAT_4);
        glTexEnvi(8960, 8704, 34160);
        glTexEnvi(8960, 34161, 7681);
        glTexEnvi(8960, 34176, 34166);
        glTexEnvi(8960, 34192, 768);
        glTexEnvi(8960, 34162, 7681);
        glTexEnvi(8960, 34184, 5890);
        glTexEnvi(8960, 34200, 770);
    }
    
    public static void disableOutlineMode() {
        glTexEnvi(8960, 8704, 8448);
        glTexEnvi(8960, 34161, 8448);
        glTexEnvi(8960, 34162, 8448);
        glTexEnvi(8960, 34176, 5890);
        glTexEnvi(8960, 34184, 5890);
        glTexEnvi(8960, 34192, 768);
        glTexEnvi(8960, 34200, 770);
    }
    
    public static void enableFog() {
        GlStateManager.fogState.fog.setEnabled();
    }
    
    public static void disableFog() {
        GlStateManager.fogState.fog.setDisabled();
    }
    
    public static void setFog(final FogMode fogMode) {
        setFog(fogMode.capabilityId);
    }
    
    private static void setFog(final int param) {
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
    
    public static void glFog(final int pname, final FloatBuffer param) {
        GL11.glFog(pname, param);
    }
    
    public static void glFogi(final int pname, final int param) {
        GL11.glFogi(pname, param);
    }
    
    public static void enableCull() {
        GlStateManager.cullState.cullFace.setEnabled();
    }
    
    public static void disableCull() {
        GlStateManager.cullState.cullFace.setDisabled();
    }
    
    public static void cullFace(final CullFace cullFace) {
        cullFace(cullFace.mode);
    }
    
    private static void cullFace(final int mode) {
        if (mode != GlStateManager.cullState.mode) {
            GL11.glCullFace(GlStateManager.cullState.mode = mode);
        }
    }
    
    public static void glPolygonMode(final int face, final int mode) {
        GL11.glPolygonMode(face, mode);
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
    
    public static void colorLogicOp(final LogicOp logicOperation) {
        colorLogicOp(logicOperation.opcode);
    }
    
    public static void colorLogicOp(final int opcode) {
        if (opcode != GlStateManager.colorLogicState.opcode) {
            GL11.glLogicOp(GlStateManager.colorLogicState.opcode = opcode);
        }
    }
    
    public static void enableTexGenCoord(final TexGen texGen) {
        texGenCoord(texGen).textureGen.setEnabled();
    }
    
    public static void disableTexGenCoord(final TexGen texGen) {
        texGenCoord(texGen).textureGen.setDisabled();
    }
    
    public static void texGen(final TexGen texGen, final int param) {
        final TexGenCoord glstatemanager$texgencoord = texGenCoord(texGen);
        if (param != glstatemanager$texgencoord.param) {
            glstatemanager$texgencoord.param = param;
            GL11.glTexGeni(glstatemanager$texgencoord.coord, 9472, param);
        }
    }
    
    public static void texGen(final TexGen texGen, final int pname, final FloatBuffer params) {
        GL11.glTexGen(texGenCoord(texGen).coord, pname, params);
    }
    
    private static TexGenCoord texGenCoord(final TexGen texGen) {
        switch (texGen) {
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
    
    public static void glTexEnv(final int target, final int parameterName, final FloatBuffer parameters) {
        GL11.glTexEnv(target, parameterName, parameters);
    }
    
    public static void glTexEnvi(final int target, final int parameterName, final int parameter) {
        GL11.glTexEnvi(target, parameterName, parameter);
    }
    
    public static void glTexEnvf(final int target, final int parameterName, final float parameter) {
        GL11.glTexEnvf(target, parameterName, parameter);
    }
    
    public static void glTexParameterf(final int target, final int parameterName, final float parameter) {
        GL11.glTexParameterf(target, parameterName, parameter);
    }
    
    public static void glTexParameteri(final int target, final int parameterName, final int parameter) {
        GL11.glTexParameteri(target, parameterName, parameter);
    }
    
    public static int glGetTexLevelParameteri(final int target, final int level, final int parameterName) {
        return GL11.glGetTexLevelParameteri(target, level, parameterName);
    }
    
    public static int generateTexture() {
        return GL11.glGenTextures();
    }
    
    public static void deleteTexture(final int texture) {
        if (texture != 0) {
            GL11.glDeleteTextures(texture);
            for (final TextureState glstatemanager$texturestate : GlStateManager.textureState) {
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
    
    public static void glTexImage2D(final int target, final int level, final int internalFormat, final int width, final int height, final int border, final int format, final int type, @Nullable final IntBuffer pixels) {
        GL11.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
    }
    
    public static void glTexSubImage2D(final int target, final int level, final int xOffset, final int yOffset, final int width, final int height, final int format, final int type, final IntBuffer pixels) {
        GL11.glTexSubImage2D(target, level, xOffset, yOffset, width, height, format, type, pixels);
    }
    
    public static void glCopyTexSubImage2D(final int target, final int level, final int xOffset, final int yOffset, final int x, final int y, final int width, final int height) {
        GL11.glCopyTexSubImage2D(target, level, xOffset, yOffset, x, y, width, height);
    }
    
    public static void glGetTexImage(final int target, final int level, final int format, final int type, final IntBuffer pixels) {
        GL11.glGetTexImage(target, level, format, type, pixels);
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
    
    public static void rotate(final Quaternion quaternionIn) {
        multMatrix(quatToGlMatrix(GlStateManager.BUF_FLOAT_16, quaternionIn));
    }
    
    public static FloatBuffer quatToGlMatrix(final FloatBuffer buffer, final Quaternion quaternionIn) {
        buffer.clear();
        final float f = quaternionIn.x * quaternionIn.x;
        final float f2 = quaternionIn.x * quaternionIn.y;
        final float f3 = quaternionIn.x * quaternionIn.z;
        final float f4 = quaternionIn.x * quaternionIn.w;
        final float f5 = quaternionIn.y * quaternionIn.y;
        final float f6 = quaternionIn.y * quaternionIn.z;
        final float f7 = quaternionIn.y * quaternionIn.w;
        final float f8 = quaternionIn.z * quaternionIn.z;
        final float f9 = quaternionIn.z * quaternionIn.w;
        buffer.put(1.0f - 2.0f * (f5 + f8));
        buffer.put(2.0f * (f2 + f9));
        buffer.put(2.0f * (f3 - f7));
        buffer.put(0.0f);
        buffer.put(2.0f * (f2 - f9));
        buffer.put(1.0f - 2.0f * (f + f8));
        buffer.put(2.0f * (f6 + f4));
        buffer.put(0.0f);
        buffer.put(2.0f * (f3 + f7));
        buffer.put(2.0f * (f6 - f4));
        buffer.put(1.0f - 2.0f * (f + f5));
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(0.0f);
        buffer.put(1.0f);
        buffer.rewind();
        return buffer;
    }
    
    public static void color(final float colorRed, final float colorGreen, final float colorBlue, final float colorAlpha) {
        if (colorRed != GlStateManager.colorState.red || colorGreen != GlStateManager.colorState.green || colorBlue != GlStateManager.colorState.blue || colorAlpha != GlStateManager.colorState.alpha) {
            GL11.glColor4f(GlStateManager.colorState.red = colorRed, GlStateManager.colorState.green = colorGreen, GlStateManager.colorState.blue = colorBlue, GlStateManager.colorState.alpha = colorAlpha);
        }
    }
    
    public static void color(final float colorRed, final float colorGreen, final float colorBlue) {
        color(colorRed, colorGreen, colorBlue, 1.0f);
    }
    
    public static void glTexCoord2f(final float sCoord, final float tCoord) {
        GL11.glTexCoord2f(sCoord, tCoord);
    }
    
    public static void glVertex3f(final float x, final float y, final float z) {
        GL11.glVertex3f(x, y, z);
    }
    
    public static void resetColor() {
        GlStateManager.colorState.red = -1.0f;
        GlStateManager.colorState.green = -1.0f;
        GlStateManager.colorState.blue = -1.0f;
        GlStateManager.colorState.alpha = -1.0f;
    }
    
    public static void glNormalPointer(final int type, final int stride, final ByteBuffer buffer) {
        GL11.glNormalPointer(type, stride, buffer);
    }
    
    public static void glTexCoordPointer(final int size, final int type, final int stride, final int buffer_offset) {
        GL11.glTexCoordPointer(size, type, stride, (long)buffer_offset);
    }
    
    public static void glTexCoordPointer(final int size, final int type, final int stride, final ByteBuffer buffer) {
        GL11.glTexCoordPointer(size, type, stride, buffer);
    }
    
    public static void glVertexPointer(final int size, final int type, final int stride, final int buffer_offset) {
        GL11.glVertexPointer(size, type, stride, (long)buffer_offset);
    }
    
    public static void glVertexPointer(final int size, final int type, final int stride, final ByteBuffer buffer) {
        GL11.glVertexPointer(size, type, stride, buffer);
    }
    
    public static void glColorPointer(final int size, final int type, final int stride, final int buffer_offset) {
        GL11.glColorPointer(size, type, stride, (long)buffer_offset);
    }
    
    public static void glColorPointer(final int size, final int type, final int stride, final ByteBuffer buffer) {
        GL11.glColorPointer(size, type, stride, buffer);
    }
    
    public static void glDisableClientState(final int cap) {
        GL11.glDisableClientState(cap);
    }
    
    public static void glEnableClientState(final int cap) {
        GL11.glEnableClientState(cap);
    }
    
    public static void glBegin(final int mode) {
        GL11.glBegin(mode);
    }
    
    public static void glEnd() {
        GL11.glEnd();
    }
    
    public static void glDrawArrays(final int mode, final int first, final int count) {
        GL11.glDrawArrays(mode, first, count);
        if (Config.isShaders() && !GlStateManager.creatingDisplayList) {
            final int i = Shaders.activeProgram.getCountInstances();
            if (i > 1) {
                for (int j = 1; j < i; ++j) {
                    Shaders.uniform_instanceId.setValue(j);
                    GL11.glDrawArrays(mode, first, count);
                }
                Shaders.uniform_instanceId.setValue(0);
            }
        }
    }
    
    public static void glLineWidth(final float width) {
        GL11.glLineWidth(width);
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
    
    public static void glDeleteLists(final int list, final int range) {
        GL11.glDeleteLists(list, range);
    }
    
    public static void glNewList(final int list, final int mode) {
        GL11.glNewList(list, mode);
        GlStateManager.creatingDisplayList = true;
    }
    
    public static void glEndList() {
        GL11.glEndList();
        GlStateManager.creatingDisplayList = false;
    }
    
    public static int glGenLists(final int range) {
        return GL11.glGenLists(range);
    }
    
    public static void glPixelStorei(final int parameterName, final int param) {
        GL11.glPixelStorei(parameterName, param);
    }
    
    public static void glReadPixels(final int x, final int y, final int width, final int height, final int format, final int type, final IntBuffer pixels) {
        GL11.glReadPixels(x, y, width, height, format, type, pixels);
    }
    
    public static int glGetError() {
        return GL11.glGetError();
    }
    
    public static String glGetString(final int name) {
        return GL11.glGetString(name);
    }
    
    public static void glGetInteger(final int parameterName, final IntBuffer parameters) {
        GL11.glGetInteger(parameterName, parameters);
    }
    
    public static int glGetInteger(final int parameterName) {
        return GL11.glGetInteger(parameterName);
    }
    
    public static void enableBlendProfile(final Profile p_187408_0_) {
        p_187408_0_.apply();
    }
    
    public static void disableBlendProfile(final Profile p_187440_0_) {
        p_187440_0_.clean();
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
    
    static {
        BUF_FLOAT_16 = BufferUtils.createFloatBuffer(16);
        BUF_FLOAT_4 = BufferUtils.createFloatBuffer(4);
        alphaState = new AlphaState();
        lightingState = new BooleanState(2896);
        lightState = new BooleanState[8];
        GlStateManager.clearEnabled = true;
        GlStateManager.alphaLock = new LockCounter();
        GlStateManager.alphaLockState = new GlAlphaState();
        GlStateManager.blendLock = new LockCounter();
        GlStateManager.blendLockState = new GlBlendState();
        GlStateManager.creatingDisplayList = false;
        for (int i = 0; i < 8; ++i) {
            GlStateManager.lightState[i] = new BooleanState(16384 + i);
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
        for (int j = 0; j < GlStateManager.textureState.length; ++j) {
            GlStateManager.textureState[j] = new TextureState();
        }
        GlStateManager.activeShadeModel = 7425;
        rescaleNormalState = new BooleanState(32826);
        colorMaskState = new ColorMask();
        colorState = new Color();
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
        
        private ClearState() {
            this.depth = 1.0;
            this.color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
    
    static class Color
    {
        public float red;
        public float green;
        public float blue;
        public float alpha;
        
        public Color() {
            this(1.0f, 1.0f, 1.0f, 1.0f);
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
    
    public enum CullFace
    {
        FRONT(1028), 
        BACK(1029), 
        FRONT_AND_BACK(1032);
        
        public final int mode;
        
        private CullFace(final int modeIn) {
            this.mode = modeIn;
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
    
    public enum DestFactor
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
        
        private DestFactor(final int factorIn) {
            this.factor = factorIn;
        }
    }
    
    public enum FogMode
    {
        LINEAR(9729), 
        EXP(2048), 
        EXP2(2049);
        
        public final int capabilityId;
        
        private FogMode(final int capabilityIn) {
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
        
        private FogState() {
            this.fog = new BooleanState(2912);
            this.mode = 2048;
            this.density = 1.0f;
            this.end = 1.0f;
        }
    }
    
    public enum LogicOp
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
        
        private LogicOp(final int opcodeIn) {
            this.opcode = opcodeIn;
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
        }
    }
    
    public enum Profile
    {
        DEFAULT {
            @Override
            public void apply() {
                GlStateManager.disableAlpha();
                GlStateManager.alphaFunc(519, 0.0f);
                GlStateManager.disableLighting();
                GL11.glLightModel(2899, RenderHelper.setColorBuffer(0.2f, 0.2f, 0.2f, 1.0f));
                for (int i = 0; i < 8; ++i) {
                    GlStateManager.disableLight(i);
                    GL11.glLight(16384 + i, 4608, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
                    GL11.glLight(16384 + i, 4611, RenderHelper.setColorBuffer(0.0f, 0.0f, 1.0f, 0.0f));
                    if (i == 0) {
                        GL11.glLight(16384 + i, 4609, RenderHelper.setColorBuffer(1.0f, 1.0f, 1.0f, 1.0f));
                        GL11.glLight(16384 + i, 4610, RenderHelper.setColorBuffer(1.0f, 1.0f, 1.0f, 1.0f));
                    }
                    else {
                        GL11.glLight(16384 + i, 4609, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
                        GL11.glLight(16384 + i, 4610, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 1.0f));
                    }
                }
                GlStateManager.disableColorMaterial();
                GlStateManager.colorMaterial(1032, 5634);
                GlStateManager.disableDepth();
                GlStateManager.depthFunc(513);
                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
                GlStateManager.blendFunc(SourceFactor.ONE, DestFactor.ZERO);
                GlStateManager.tryBlendFuncSeparate(SourceFactor.ONE, DestFactor.ZERO, SourceFactor.ONE, DestFactor.ZERO);
                GL14.glBlendEquation(32774);
                GlStateManager.disableFog();
                GL11.glFogi(2917, 2048);
                GlStateManager.setFogDensity(1.0f);
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(1.0f);
                GL11.glFog(2918, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                if (GLContext.getCapabilities().GL_NV_fog_distance) {
                    GL11.glFogi(2917, 34140);
                }
                GlStateManager.doPolygonOffset(0.0f, 0.0f);
                GlStateManager.disableColorLogic();
                GlStateManager.colorLogicOp(5379);
                GlStateManager.disableTexGenCoord(TexGen.S);
                GlStateManager.texGen(TexGen.S, 9216);
                GlStateManager.texGen(TexGen.S, 9474, RenderHelper.setColorBuffer(1.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.texGen(TexGen.S, 9217, RenderHelper.setColorBuffer(1.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.disableTexGenCoord(TexGen.T);
                GlStateManager.texGen(TexGen.T, 9216);
                GlStateManager.texGen(TexGen.T, 9474, RenderHelper.setColorBuffer(0.0f, 1.0f, 0.0f, 0.0f));
                GlStateManager.texGen(TexGen.T, 9217, RenderHelper.setColorBuffer(0.0f, 1.0f, 0.0f, 0.0f));
                GlStateManager.disableTexGenCoord(TexGen.R);
                GlStateManager.texGen(TexGen.R, 9216);
                GlStateManager.texGen(TexGen.R, 9474, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.texGen(TexGen.R, 9217, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.disableTexGenCoord(TexGen.Q);
                GlStateManager.texGen(TexGen.Q, 9216);
                GlStateManager.texGen(TexGen.Q, 9474, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.texGen(TexGen.Q, 9217, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GlStateManager.setActiveTexture(0);
                GL11.glTexParameteri(3553, 10240, 9729);
                GL11.glTexParameteri(3553, 10241, 9986);
                GL11.glTexParameteri(3553, 10242, 10497);
                GL11.glTexParameteri(3553, 10243, 10497);
                GL11.glTexParameteri(3553, 33085, 1000);
                GL11.glTexParameteri(3553, 33083, 1000);
                GL11.glTexParameteri(3553, 33082, -1000);
                GL11.glTexParameterf(3553, 34049, 0.0f);
                GL11.glTexEnvi(8960, 8704, 8448);
                GL11.glTexEnv(8960, 8705, RenderHelper.setColorBuffer(0.0f, 0.0f, 0.0f, 0.0f));
                GL11.glTexEnvi(8960, 34161, 8448);
                GL11.glTexEnvi(8960, 34162, 8448);
                GL11.glTexEnvi(8960, 34176, 5890);
                GL11.glTexEnvi(8960, 34177, 34168);
                GL11.glTexEnvi(8960, 34178, 34166);
                GL11.glTexEnvi(8960, 34184, 5890);
                GL11.glTexEnvi(8960, 34185, 34168);
                GL11.glTexEnvi(8960, 34186, 34166);
                GL11.glTexEnvi(8960, 34192, 768);
                GL11.glTexEnvi(8960, 34193, 768);
                GL11.glTexEnvi(8960, 34194, 770);
                GL11.glTexEnvi(8960, 34200, 770);
                GL11.glTexEnvi(8960, 34201, 770);
                GL11.glTexEnvi(8960, 34202, 770);
                GL11.glTexEnvf(8960, 34163, 1.0f);
                GL11.glTexEnvf(8960, 3356, 1.0f);
                GlStateManager.disableNormalize();
                GlStateManager.shadeModel(7425);
                GlStateManager.disableRescaleNormal();
                GlStateManager.colorMask(true, true, true, true);
                GlStateManager.clearDepth(1.0);
                GL11.glLineWidth(1.0f);
                GL11.glNormal3f(0.0f, 0.0f, 1.0f);
                GL11.glPolygonMode(1028, 6914);
                GL11.glPolygonMode(1029, 6914);
            }
            
            @Override
            public void clean() {
            }
        }, 
        PLAYER_SKIN {
            @Override
            public void apply() {
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            }
            
            @Override
            public void clean() {
                GlStateManager.disableBlend();
            }
        }, 
        TRANSPARENT_MODEL {
            @Override
            public void apply() {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.alphaFunc(516, 0.003921569f);
            }
            
            @Override
            public void clean() {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                GlStateManager.depthMask(true);
            }
        };
        
        public abstract void apply();
        
        public abstract void clean();
    }
    
    public enum SourceFactor
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
        
        private SourceFactor(final int factorIn) {
            this.factor = factorIn;
        }
    }
    
    static class StencilFunc
    {
        public int func;
        public int mask;
        
        private StencilFunc() {
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
        
        private StencilState() {
            this.func = new StencilFunc();
            this.mask = -1;
            this.fail = 7680;
            this.zfail = 7680;
            this.zpass = 7680;
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
        public BooleanState textureGen;
        public int coord;
        public int param;
        
        public TexGenCoord(final int coordIn, final int capabilityIn) {
            this.param = -1;
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
        }
    }
}
