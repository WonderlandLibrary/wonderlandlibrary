/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.ARBFramebufferObject
 *  org.lwjgl.opengl.ARBMultitexture
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.ARBVertexBufferObject
 *  org.lwjgl.opengl.ARBVertexShader
 *  org.lwjgl.opengl.ContextCapabilities
 *  org.lwjgl.opengl.EXTBlendFuncSeparate
 *  org.lwjgl.opengl.EXTFramebufferObject
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL13
 *  org.lwjgl.opengl.GL14
 *  org.lwjgl.opengl.GL15
 *  org.lwjgl.opengl.GL20
 *  org.lwjgl.opengl.GL30
 *  org.lwjgl.opengl.GLContext
 *  oshi.SystemInfo
 *  oshi.hardware.Processor
 */
package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTBlendFuncSeparate;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import oshi.SystemInfo;
import oshi.hardware.Processor;

public class OpenGlHelper {
    public static int GL_STATIC_DRAW;
    public static int GL_OPERAND1_ALPHA;
    private static boolean openGL14;
    public static int lightmapTexUnit;
    public static int GL_PREVIOUS;
    public static int GL_CONSTANT;
    public static int defaultTexUnit;
    public static int GL_SOURCE0_RGB;
    public static int GL_FB_INCOMPLETE_MISS_ATTACH;
    private static String field_183030_aa;
    public static boolean vboSupported;
    public static int GL_INTERPOLATE;
    public static int GL_FRAMEBUFFER_COMPLETE;
    public static boolean framebufferSupported;
    public static int GL_SOURCE2_ALPHA;
    private static String logText;
    public static int GL_DEPTH_ATTACHMENT;
    public static boolean openGL21;
    public static int GL_ARRAY_BUFFER;
    public static int GL_COMBINE_ALPHA;
    public static int GL_COMBINE_RGB;
    private static boolean arbShaders;
    public static int GL_FRAGMENT_SHADER;
    public static int GL_LINK_STATUS;
    public static int GL_COMBINE;
    public static int GL_OPERAND0_ALPHA;
    public static int GL_VERTEX_SHADER;
    private static boolean arbVbo;
    public static int GL_OPERAND1_RGB;
    public static int GL_OPERAND0_RGB;
    public static int GL_RENDERBUFFER;
    public static int GL_FB_INCOMPLETE_READ_BUFFER;
    public static boolean shadersSupported;
    public static boolean extBlendFuncSeparate;
    public static int GL_SOURCE2_RGB;
    public static int GL_COLOR_ATTACHMENT0;
    public static boolean field_181062_Q;
    public static int GL_TEXTURE2;
    public static int GL_SOURCE1_RGB;
    public static int GL_PRIMARY_COLOR;
    public static boolean nvidia;
    private static boolean arbMultitexture;
    public static int GL_COMPILE_STATUS;
    public static boolean field_181063_b;
    private static boolean arbTextureEnvCombine;
    public static int GL_FB_INCOMPLETE_ATTACHMENT;
    public static int GL_FRAMEBUFFER;
    public static int GL_OPERAND2_ALPHA;
    public static int GL_SOURCE0_ALPHA;
    public static int GL_FB_INCOMPLETE_DRAW_BUFFER;
    public static int GL_SOURCE1_ALPHA;
    private static boolean shadersAvailable;
    private static int framebufferType;
    public static int GL_OPERAND2_RGB;

    public static void glFramebufferTexture2D(int n, int n2, int n3, int n4, int n5) {
        if (framebufferSupported) {
            switch (framebufferType) {
                case 0: {
                    GL30.glFramebufferTexture2D((int)n, (int)n2, (int)n3, (int)n4, (int)n5);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glFramebufferTexture2D((int)n, (int)n2, (int)n3, (int)n4, (int)n5);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glFramebufferTexture2DEXT((int)n, (int)n2, (int)n3, (int)n4, (int)n5);
                }
            }
        }
    }

    public static String getLogText() {
        return logText;
    }

    public static String func_183029_j() {
        return field_183030_aa == null ? "<unknown>" : field_183030_aa;
    }

    public static void glBindBuffer(int n, int n2) {
        if (arbVbo) {
            ARBVertexBufferObject.glBindBufferARB((int)n, (int)n2);
        } else {
            GL15.glBindBuffer((int)n, (int)n2);
        }
    }

    public static String glGetProgramInfoLog(int n, int n2) {
        return arbShaders ? ARBShaderObjects.glGetInfoLogARB((int)n, (int)n2) : GL20.glGetProgramInfoLog((int)n, (int)n2);
    }

    public static void glRenderbufferStorage(int n, int n2, int n3, int n4) {
        if (framebufferSupported) {
            switch (framebufferType) {
                case 0: {
                    GL30.glRenderbufferStorage((int)n, (int)n2, (int)n3, (int)n4);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glRenderbufferStorage((int)n, (int)n2, (int)n3, (int)n4);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glRenderbufferStorageEXT((int)n, (int)n2, (int)n3, (int)n4);
                }
            }
        }
    }

    public static int glGetUniformLocation(int n, CharSequence charSequence) {
        return arbShaders ? ARBShaderObjects.glGetUniformLocationARB((int)n, (CharSequence)charSequence) : GL20.glGetUniformLocation((int)n, (CharSequence)charSequence);
    }

    public static boolean useVbo() {
        if (vboSupported) {
            Minecraft.getMinecraft();
            if (Minecraft.gameSettings.useVbo) {
                return true;
            }
        }
        return false;
    }

    public static void initializeTextures() {
        ContextCapabilities contextCapabilities = GLContext.getCapabilities();
        arbMultitexture = contextCapabilities.GL_ARB_multitexture && !contextCapabilities.OpenGL13;
        boolean bl = arbTextureEnvCombine = contextCapabilities.GL_ARB_texture_env_combine && !contextCapabilities.OpenGL13;
        if (arbMultitexture) {
            logText = String.valueOf(logText) + "Using ARB_multitexture.\n";
            defaultTexUnit = 33984;
            lightmapTexUnit = 33985;
            GL_TEXTURE2 = 33986;
        } else {
            logText = String.valueOf(logText) + "Using GL 1.3 multitexturing.\n";
            defaultTexUnit = 33984;
            lightmapTexUnit = 33985;
            GL_TEXTURE2 = 33986;
        }
        if (arbTextureEnvCombine) {
            logText = String.valueOf(logText) + "Using ARB_texture_env_combine.\n";
            GL_COMBINE = 34160;
            GL_INTERPOLATE = 34165;
            GL_PRIMARY_COLOR = 34167;
            GL_CONSTANT = 34166;
            GL_PREVIOUS = 34168;
            GL_COMBINE_RGB = 34161;
            GL_SOURCE0_RGB = 34176;
            GL_SOURCE1_RGB = 34177;
            GL_SOURCE2_RGB = 34178;
            GL_OPERAND0_RGB = 34192;
            GL_OPERAND1_RGB = 34193;
            GL_OPERAND2_RGB = 34194;
            GL_COMBINE_ALPHA = 34162;
            GL_SOURCE0_ALPHA = 34184;
            GL_SOURCE1_ALPHA = 34185;
            GL_SOURCE2_ALPHA = 34186;
            GL_OPERAND0_ALPHA = 34200;
            GL_OPERAND1_ALPHA = 34201;
            GL_OPERAND2_ALPHA = 34202;
        } else {
            logText = String.valueOf(logText) + "Using GL 1.3 texture combiners.\n";
            GL_COMBINE = 34160;
            GL_INTERPOLATE = 34165;
            GL_PRIMARY_COLOR = 34167;
            GL_CONSTANT = 34166;
            GL_PREVIOUS = 34168;
            GL_COMBINE_RGB = 34161;
            GL_SOURCE0_RGB = 34176;
            GL_SOURCE1_RGB = 34177;
            GL_SOURCE2_RGB = 34178;
            GL_OPERAND0_RGB = 34192;
            GL_OPERAND1_RGB = 34193;
            GL_OPERAND2_RGB = 34194;
            GL_COMBINE_ALPHA = 34162;
            GL_SOURCE0_ALPHA = 34184;
            GL_SOURCE1_ALPHA = 34185;
            GL_SOURCE2_ALPHA = 34186;
            GL_OPERAND0_ALPHA = 34200;
            GL_OPERAND1_ALPHA = 34201;
            GL_OPERAND2_ALPHA = 34202;
        }
        extBlendFuncSeparate = contextCapabilities.GL_EXT_blend_func_separate && !contextCapabilities.OpenGL14;
        openGL14 = contextCapabilities.OpenGL14 || contextCapabilities.GL_EXT_blend_func_separate;
        boolean bl2 = framebufferSupported = openGL14 && (contextCapabilities.GL_ARB_framebuffer_object || contextCapabilities.GL_EXT_framebuffer_object || contextCapabilities.OpenGL30);
        if (framebufferSupported) {
            logText = String.valueOf(logText) + "Using framebuffer objects because ";
            if (contextCapabilities.OpenGL30) {
                logText = String.valueOf(logText) + "OpenGL 3.0 is supported and separate blending is supported.\n";
                framebufferType = 0;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            } else if (contextCapabilities.GL_ARB_framebuffer_object) {
                logText = String.valueOf(logText) + "ARB_framebuffer_object is supported and separate blending is supported.\n";
                framebufferType = 1;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            } else if (contextCapabilities.GL_EXT_framebuffer_object) {
                logText = String.valueOf(logText) + "EXT_framebuffer_object is supported.\n";
                framebufferType = 2;
                GL_FRAMEBUFFER = 36160;
                GL_RENDERBUFFER = 36161;
                GL_COLOR_ATTACHMENT0 = 36064;
                GL_DEPTH_ATTACHMENT = 36096;
                GL_FRAMEBUFFER_COMPLETE = 36053;
                GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
        } else {
            logText = String.valueOf(logText) + "Not using framebuffer objects because ";
            logText = String.valueOf(logText) + "OpenGL 1.4 is " + (contextCapabilities.OpenGL14 ? "" : "not ") + "supported, ";
            logText = String.valueOf(logText) + "EXT_blend_func_separate is " + (contextCapabilities.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
            logText = String.valueOf(logText) + "OpenGL 3.0 is " + (contextCapabilities.OpenGL30 ? "" : "not ") + "supported, ";
            logText = String.valueOf(logText) + "ARB_framebuffer_object is " + (contextCapabilities.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
            logText = String.valueOf(logText) + "EXT_framebuffer_object is " + (contextCapabilities.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
        }
        openGL21 = contextCapabilities.OpenGL21;
        shadersAvailable = openGL21 || contextCapabilities.GL_ARB_vertex_shader && contextCapabilities.GL_ARB_fragment_shader && contextCapabilities.GL_ARB_shader_objects;
        logText = String.valueOf(logText) + "Shaders are " + (shadersAvailable ? "" : "not ") + "available because ";
        if (shadersAvailable) {
            if (contextCapabilities.OpenGL21) {
                logText = String.valueOf(logText) + "OpenGL 2.1 is supported.\n";
                arbShaders = false;
                GL_LINK_STATUS = 35714;
                GL_COMPILE_STATUS = 35713;
                GL_VERTEX_SHADER = 35633;
                GL_FRAGMENT_SHADER = 35632;
            } else {
                logText = String.valueOf(logText) + "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
                arbShaders = true;
                GL_LINK_STATUS = 35714;
                GL_COMPILE_STATUS = 35713;
                GL_VERTEX_SHADER = 35633;
                GL_FRAGMENT_SHADER = 35632;
            }
        } else {
            logText = String.valueOf(logText) + "OpenGL 2.1 is " + (contextCapabilities.OpenGL21 ? "" : "not ") + "supported, ";
            logText = String.valueOf(logText) + "ARB_shader_objects is " + (contextCapabilities.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
            logText = String.valueOf(logText) + "ARB_vertex_shader is " + (contextCapabilities.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
            logText = String.valueOf(logText) + "ARB_fragment_shader is " + (contextCapabilities.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
        }
        shadersSupported = framebufferSupported && shadersAvailable;
        String string = GL11.glGetString((int)7936).toLowerCase();
        nvidia = string.contains("nvidia");
        arbVbo = !contextCapabilities.OpenGL15 && contextCapabilities.GL_ARB_vertex_buffer_object;
        vboSupported = contextCapabilities.OpenGL15 || arbVbo;
        logText = String.valueOf(logText) + "VBOs are " + (vboSupported ? "" : "not ") + "available because ";
        if (vboSupported) {
            if (arbVbo) {
                logText = String.valueOf(logText) + "ARB_vertex_buffer_object is supported.\n";
                GL_STATIC_DRAW = 35044;
                GL_ARRAY_BUFFER = 34962;
            } else {
                logText = String.valueOf(logText) + "OpenGL 1.5 is supported.\n";
                GL_STATIC_DRAW = 35044;
                GL_ARRAY_BUFFER = 34962;
            }
        }
        if (field_181063_b = string.contains("ati")) {
            if (vboSupported) {
                field_181062_Q = true;
            } else {
                GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0f);
            }
        }
        try {
            Processor[] processorArray = new SystemInfo().getHardware().getProcessors();
            field_183030_aa = String.format("%dx %s", processorArray.length, processorArray[0]).replaceAll("\\s+", " ");
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    public static void setActiveTexture(int n) {
        if (arbMultitexture) {
            ARBMultitexture.glActiveTextureARB((int)n);
        } else {
            GL13.glActiveTexture((int)n);
        }
    }

    public static void glBufferData(int n, ByteBuffer byteBuffer, int n2) {
        if (arbVbo) {
            ARBVertexBufferObject.glBufferDataARB((int)n, (ByteBuffer)byteBuffer, (int)n2);
        } else {
            GL15.glBufferData((int)n, (ByteBuffer)byteBuffer, (int)n2);
        }
    }

    public static int glCreateShader(int n) {
        return arbShaders ? ARBShaderObjects.glCreateShaderObjectARB((int)n) : GL20.glCreateShader((int)n);
    }

    public static void glFramebufferRenderbuffer(int n, int n2, int n3, int n4) {
        if (framebufferSupported) {
            switch (framebufferType) {
                case 0: {
                    GL30.glFramebufferRenderbuffer((int)n, (int)n2, (int)n3, (int)n4);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glFramebufferRenderbuffer((int)n, (int)n2, (int)n3, (int)n4);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glFramebufferRenderbufferEXT((int)n, (int)n2, (int)n3, (int)n4);
                }
            }
        }
    }

    public static void glDeleteProgram(int n) {
        if (arbShaders) {
            ARBShaderObjects.glDeleteObjectARB((int)n);
        } else {
            GL20.glDeleteProgram((int)n);
        }
    }

    public static void glCompileShader(int n) {
        if (arbShaders) {
            ARBShaderObjects.glCompileShaderARB((int)n);
        } else {
            GL20.glCompileShader((int)n);
        }
    }

    public static void setClientActiveTexture(int n) {
        if (arbMultitexture) {
            ARBMultitexture.glClientActiveTextureARB((int)n);
        } else {
            GL13.glClientActiveTexture((int)n);
        }
    }

    public static void glUniform4(int n, FloatBuffer floatBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniform4ARB((int)n, (FloatBuffer)floatBuffer);
        } else {
            GL20.glUniform4((int)n, (FloatBuffer)floatBuffer);
        }
    }

    public static int glGetAttribLocation(int n, CharSequence charSequence) {
        return arbShaders ? ARBVertexShader.glGetAttribLocationARB((int)n, (CharSequence)charSequence) : GL20.glGetAttribLocation((int)n, (CharSequence)charSequence);
    }

    public static boolean areShadersSupported() {
        return shadersSupported;
    }

    public static void glUniform2(int n, FloatBuffer floatBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniform2ARB((int)n, (FloatBuffer)floatBuffer);
        } else {
            GL20.glUniform2((int)n, (FloatBuffer)floatBuffer);
        }
    }

    public static void glUseProgram(int n) {
        if (arbShaders) {
            ARBShaderObjects.glUseProgramObjectARB((int)n);
        } else {
            GL20.glUseProgram((int)n);
        }
    }

    public static void glUniform1(int n, FloatBuffer floatBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1ARB((int)n, (FloatBuffer)floatBuffer);
        } else {
            GL20.glUniform1((int)n, (FloatBuffer)floatBuffer);
        }
    }

    public static void glUniformMatrix2(int n, boolean bl, FloatBuffer floatBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix2ARB((int)n, (boolean)bl, (FloatBuffer)floatBuffer);
        } else {
            GL20.glUniformMatrix2((int)n, (boolean)bl, (FloatBuffer)floatBuffer);
        }
    }

    public static void glUniform2(int n, IntBuffer intBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniform2ARB((int)n, (IntBuffer)intBuffer);
        } else {
            GL20.glUniform2((int)n, (IntBuffer)intBuffer);
        }
    }

    public static void glLinkProgram(int n) {
        if (arbShaders) {
            ARBShaderObjects.glLinkProgramARB((int)n);
        } else {
            GL20.glLinkProgram((int)n);
        }
    }

    public static void glShaderSource(int n, ByteBuffer byteBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glShaderSourceARB((int)n, (ByteBuffer)byteBuffer);
        } else {
            GL20.glShaderSource((int)n, (ByteBuffer)byteBuffer);
        }
    }

    public static int glGenBuffers() {
        return arbVbo ? ARBVertexBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
    }

    public static void glBlendFunc(int n, int n2, int n3, int n4) {
        if (openGL14) {
            if (extBlendFuncSeparate) {
                EXTBlendFuncSeparate.glBlendFuncSeparateEXT((int)n, (int)n2, (int)n3, (int)n4);
            } else {
                GL14.glBlendFuncSeparate((int)n, (int)n2, (int)n3, (int)n4);
            }
        } else {
            GL11.glBlendFunc((int)n, (int)n2);
        }
    }

    public static void glUniform3(int n, FloatBuffer floatBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniform3ARB((int)n, (FloatBuffer)floatBuffer);
        } else {
            GL20.glUniform3((int)n, (FloatBuffer)floatBuffer);
        }
    }

    public static String glGetShaderInfoLog(int n, int n2) {
        return arbShaders ? ARBShaderObjects.glGetInfoLogARB((int)n, (int)n2) : GL20.glGetShaderInfoLog((int)n, (int)n2);
    }

    public static int glCreateProgram() {
        return arbShaders ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
    }

    public static void glDeleteBuffers(int n) {
        if (arbVbo) {
            ARBVertexBufferObject.glDeleteBuffersARB((int)n);
        } else {
            GL15.glDeleteBuffers((int)n);
        }
    }

    public static void glDeleteFramebuffers(int n) {
        if (framebufferSupported) {
            switch (framebufferType) {
                case 0: {
                    GL30.glDeleteFramebuffers((int)n);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glDeleteFramebuffers((int)n);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glDeleteFramebuffersEXT((int)n);
                }
            }
        }
    }

    public static void glDeleteShader(int n) {
        if (arbShaders) {
            ARBShaderObjects.glDeleteObjectARB((int)n);
        } else {
            GL20.glDeleteShader((int)n);
        }
    }

    public static void setLightmapTextureCoords(int n, float f, float f2) {
        if (arbMultitexture) {
            ARBMultitexture.glMultiTexCoord2fARB((int)n, (float)f, (float)f2);
        } else {
            GL13.glMultiTexCoord2f((int)n, (float)f, (float)f2);
        }
    }

    public static boolean isFramebufferEnabled() {
        if (framebufferSupported) {
            Minecraft.getMinecraft();
            if (Minecraft.gameSettings.fboEnable) {
                return true;
            }
        }
        return false;
    }

    public static void glUniform3(int n, IntBuffer intBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniform3ARB((int)n, (IntBuffer)intBuffer);
        } else {
            GL20.glUniform3((int)n, (IntBuffer)intBuffer);
        }
    }

    public static void glAttachShader(int n, int n2) {
        if (arbShaders) {
            ARBShaderObjects.glAttachObjectARB((int)n, (int)n2);
        } else {
            GL20.glAttachShader((int)n, (int)n2);
        }
    }

    public static int glGenFramebuffers() {
        if (!framebufferSupported) {
            return -1;
        }
        switch (framebufferType) {
            case 0: {
                return GL30.glGenFramebuffers();
            }
            case 1: {
                return ARBFramebufferObject.glGenFramebuffers();
            }
            case 2: {
                return EXTFramebufferObject.glGenFramebuffersEXT();
            }
        }
        return -1;
    }

    public static void glUniform1i(int n, int n2) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1iARB((int)n, (int)n2);
        } else {
            GL20.glUniform1i((int)n, (int)n2);
        }
    }

    public static void glUniform4(int n, IntBuffer intBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniform4ARB((int)n, (IntBuffer)intBuffer);
        } else {
            GL20.glUniform4((int)n, (IntBuffer)intBuffer);
        }
    }

    public static int glGetShaderi(int n, int n2) {
        return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB((int)n, (int)n2) : GL20.glGetShaderi((int)n, (int)n2);
    }

    public static void glBindRenderbuffer(int n, int n2) {
        if (framebufferSupported) {
            switch (framebufferType) {
                case 0: {
                    GL30.glBindRenderbuffer((int)n, (int)n2);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glBindRenderbuffer((int)n, (int)n2);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glBindRenderbufferEXT((int)n, (int)n2);
                }
            }
        }
    }

    public static void glUniformMatrix3(int n, boolean bl, FloatBuffer floatBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix3ARB((int)n, (boolean)bl, (FloatBuffer)floatBuffer);
        } else {
            GL20.glUniformMatrix3((int)n, (boolean)bl, (FloatBuffer)floatBuffer);
        }
    }

    public static void glBindFramebuffer(int n, int n2) {
        if (framebufferSupported) {
            switch (framebufferType) {
                case 0: {
                    GL30.glBindFramebuffer((int)n, (int)n2);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glBindFramebuffer((int)n, (int)n2);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glBindFramebufferEXT((int)n, (int)n2);
                }
            }
        }
    }

    static {
        logText = "";
    }

    public static void glUniformMatrix4(int n, boolean bl, FloatBuffer floatBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniformMatrix4ARB((int)n, (boolean)bl, (FloatBuffer)floatBuffer);
        } else {
            GL20.glUniformMatrix4((int)n, (boolean)bl, (FloatBuffer)floatBuffer);
        }
    }

    public static void glDeleteRenderbuffers(int n) {
        if (framebufferSupported) {
            switch (framebufferType) {
                case 0: {
                    GL30.glDeleteRenderbuffers((int)n);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glDeleteRenderbuffers((int)n);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glDeleteRenderbuffersEXT((int)n);
                }
            }
        }
    }

    public static int glGenRenderbuffers() {
        if (!framebufferSupported) {
            return -1;
        }
        switch (framebufferType) {
            case 0: {
                return GL30.glGenRenderbuffers();
            }
            case 1: {
                return ARBFramebufferObject.glGenRenderbuffers();
            }
            case 2: {
                return EXTFramebufferObject.glGenRenderbuffersEXT();
            }
        }
        return -1;
    }

    public static void glUniform1(int n, IntBuffer intBuffer) {
        if (arbShaders) {
            ARBShaderObjects.glUniform1ARB((int)n, (IntBuffer)intBuffer);
        } else {
            GL20.glUniform1((int)n, (IntBuffer)intBuffer);
        }
    }

    public static int glGetProgrami(int n, int n2) {
        return arbShaders ? ARBShaderObjects.glGetObjectParameteriARB((int)n, (int)n2) : GL20.glGetProgrami((int)n, (int)n2);
    }

    public static int glCheckFramebufferStatus(int n) {
        if (!framebufferSupported) {
            return -1;
        }
        switch (framebufferType) {
            case 0: {
                return GL30.glCheckFramebufferStatus((int)n);
            }
            case 1: {
                return ARBFramebufferObject.glCheckFramebufferStatus((int)n);
            }
            case 2: {
                return EXTFramebufferObject.glCheckFramebufferStatusEXT((int)n);
            }
        }
        return -1;
    }
}

