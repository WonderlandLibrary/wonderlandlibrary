// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.Sys;
import java.net.URI;
import java.io.IOException;
import net.minecraft.util.Util;
import java.io.File;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.ARBCopyBuffer;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.EXTBlendFuncSeparate;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL30;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.ARBVertexShader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.ARBShaderObjects;
import oshi.hardware.Processor;
import org.lwjgl.opengl.ContextCapabilities;
import oshi.SystemInfo;
import net.minecraft.client.settings.GameSettings;
import java.util.Locale;
import org.lwjgl.opengl.GL11;
import java.util.List;
import java.util.ArrayList;
import org.lwjgl.opengl.GLContext;
import net.minecraft.src.Config;
import org.apache.logging.log4j.Logger;

public class OpenGlHelper
{
    private static final Logger LOGGER;
    public static boolean nvidia;
    public static boolean ati;
    public static int GL_FRAMEBUFFER;
    public static int GL_RENDERBUFFER;
    public static int GL_COLOR_ATTACHMENT0;
    public static int GL_DEPTH_ATTACHMENT;
    public static int GL_FRAMEBUFFER_COMPLETE;
    public static int GL_FB_INCOMPLETE_ATTACHMENT;
    public static int GL_FB_INCOMPLETE_MISS_ATTACH;
    public static int GL_FB_INCOMPLETE_DRAW_BUFFER;
    public static int GL_FB_INCOMPLETE_READ_BUFFER;
    private static FboMode framebufferType;
    public static boolean framebufferSupported;
    private static boolean shadersAvailable;
    private static boolean arbShaders;
    public static int GL_LINK_STATUS;
    public static int GL_COMPILE_STATUS;
    public static int GL_VERTEX_SHADER;
    public static int GL_FRAGMENT_SHADER;
    private static boolean arbMultitexture;
    public static int defaultTexUnit;
    public static int lightmapTexUnit;
    public static int GL_TEXTURE2;
    private static boolean arbTextureEnvCombine;
    public static int GL_COMBINE;
    public static int GL_INTERPOLATE;
    public static int GL_PRIMARY_COLOR;
    public static int GL_CONSTANT;
    public static int GL_PREVIOUS;
    public static int GL_COMBINE_RGB;
    public static int GL_SOURCE0_RGB;
    public static int GL_SOURCE1_RGB;
    public static int GL_SOURCE2_RGB;
    public static int GL_OPERAND0_RGB;
    public static int GL_OPERAND1_RGB;
    public static int GL_OPERAND2_RGB;
    public static int GL_COMBINE_ALPHA;
    public static int GL_SOURCE0_ALPHA;
    public static int GL_SOURCE1_ALPHA;
    public static int GL_SOURCE2_ALPHA;
    public static int GL_OPERAND0_ALPHA;
    public static int GL_OPERAND1_ALPHA;
    public static int GL_OPERAND2_ALPHA;
    private static boolean openGL14;
    public static boolean extBlendFuncSeparate;
    public static boolean openGL21;
    public static boolean shadersSupported;
    private static String logText;
    private static String cpu;
    public static boolean vboSupported;
    public static boolean vboSupportedAti;
    private static boolean arbVbo;
    public static int GL_ARRAY_BUFFER;
    public static int GL_STATIC_DRAW;
    public static float lastBrightnessX;
    public static float lastBrightnessY;
    public static boolean openGL31;
    public static boolean vboRegions;
    public static int GL_COPY_READ_BUFFER;
    public static int GL_COPY_WRITE_BUFFER;
    public static final int GL_QUADS = 7;
    public static final int GL_TRIANGLES = 4;
    
    public static void initializeTextures() {
        Config.initDisplay();
        final ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        OpenGlHelper.arbMultitexture = (contextcapabilities.GL_ARB_multitexture && !contextcapabilities.OpenGL13);
        OpenGlHelper.arbTextureEnvCombine = (contextcapabilities.GL_ARB_texture_env_combine && !contextcapabilities.OpenGL13);
        OpenGlHelper.openGL31 = contextcapabilities.OpenGL31;
        if (OpenGlHelper.openGL31) {
            OpenGlHelper.GL_COPY_READ_BUFFER = 36662;
            OpenGlHelper.GL_COPY_WRITE_BUFFER = 36663;
        }
        else {
            OpenGlHelper.GL_COPY_READ_BUFFER = 36662;
            OpenGlHelper.GL_COPY_WRITE_BUFFER = 36663;
        }
        final boolean flag = OpenGlHelper.openGL31 || contextcapabilities.GL_ARB_copy_buffer;
        final boolean flag2 = contextcapabilities.OpenGL14;
        if (!(OpenGlHelper.vboRegions = (flag && flag2))) {
            final List<String> list = new ArrayList<String>();
            if (!flag) {
                list.add("OpenGL 1.3, ARB_copy_buffer");
            }
            if (!flag2) {
                list.add("OpenGL 1.4");
            }
            final String s = "VboRegions not supported, missing: " + Config.listToString(list);
            Config.dbg(s);
            OpenGlHelper.logText = OpenGlHelper.logText + s + "\n";
        }
        if (OpenGlHelper.arbMultitexture) {
            OpenGlHelper.logText += "Using ARB_multitexture.\n";
            OpenGlHelper.defaultTexUnit = 33984;
            OpenGlHelper.lightmapTexUnit = 33985;
            OpenGlHelper.GL_TEXTURE2 = 33986;
        }
        else {
            OpenGlHelper.logText += "Using GL 1.3 multitexturing.\n";
            OpenGlHelper.defaultTexUnit = 33984;
            OpenGlHelper.lightmapTexUnit = 33985;
            OpenGlHelper.GL_TEXTURE2 = 33986;
        }
        if (OpenGlHelper.arbTextureEnvCombine) {
            OpenGlHelper.logText += "Using ARB_texture_env_combine.\n";
            OpenGlHelper.GL_COMBINE = 34160;
            OpenGlHelper.GL_INTERPOLATE = 34165;
            OpenGlHelper.GL_PRIMARY_COLOR = 34167;
            OpenGlHelper.GL_CONSTANT = 34166;
            OpenGlHelper.GL_PREVIOUS = 34168;
            OpenGlHelper.GL_COMBINE_RGB = 34161;
            OpenGlHelper.GL_SOURCE0_RGB = 34176;
            OpenGlHelper.GL_SOURCE1_RGB = 34177;
            OpenGlHelper.GL_SOURCE2_RGB = 34178;
            OpenGlHelper.GL_OPERAND0_RGB = 34192;
            OpenGlHelper.GL_OPERAND1_RGB = 34193;
            OpenGlHelper.GL_OPERAND2_RGB = 34194;
            OpenGlHelper.GL_COMBINE_ALPHA = 34162;
            OpenGlHelper.GL_SOURCE0_ALPHA = 34184;
            OpenGlHelper.GL_SOURCE1_ALPHA = 34185;
            OpenGlHelper.GL_SOURCE2_ALPHA = 34186;
            OpenGlHelper.GL_OPERAND0_ALPHA = 34200;
            OpenGlHelper.GL_OPERAND1_ALPHA = 34201;
            OpenGlHelper.GL_OPERAND2_ALPHA = 34202;
        }
        else {
            OpenGlHelper.logText += "Using GL 1.3 texture combiners.\n";
            OpenGlHelper.GL_COMBINE = 34160;
            OpenGlHelper.GL_INTERPOLATE = 34165;
            OpenGlHelper.GL_PRIMARY_COLOR = 34167;
            OpenGlHelper.GL_CONSTANT = 34166;
            OpenGlHelper.GL_PREVIOUS = 34168;
            OpenGlHelper.GL_COMBINE_RGB = 34161;
            OpenGlHelper.GL_SOURCE0_RGB = 34176;
            OpenGlHelper.GL_SOURCE1_RGB = 34177;
            OpenGlHelper.GL_SOURCE2_RGB = 34178;
            OpenGlHelper.GL_OPERAND0_RGB = 34192;
            OpenGlHelper.GL_OPERAND1_RGB = 34193;
            OpenGlHelper.GL_OPERAND2_RGB = 34194;
            OpenGlHelper.GL_COMBINE_ALPHA = 34162;
            OpenGlHelper.GL_SOURCE0_ALPHA = 34184;
            OpenGlHelper.GL_SOURCE1_ALPHA = 34185;
            OpenGlHelper.GL_SOURCE2_ALPHA = 34186;
            OpenGlHelper.GL_OPERAND0_ALPHA = 34200;
            OpenGlHelper.GL_OPERAND1_ALPHA = 34201;
            OpenGlHelper.GL_OPERAND2_ALPHA = 34202;
        }
        OpenGlHelper.extBlendFuncSeparate = (contextcapabilities.GL_EXT_blend_func_separate && !contextcapabilities.OpenGL14);
        OpenGlHelper.openGL14 = (contextcapabilities.OpenGL14 || contextcapabilities.GL_EXT_blend_func_separate);
        OpenGlHelper.framebufferSupported = (OpenGlHelper.openGL14 && (contextcapabilities.GL_ARB_framebuffer_object || contextcapabilities.GL_EXT_framebuffer_object || contextcapabilities.OpenGL30));
        if (OpenGlHelper.framebufferSupported) {
            OpenGlHelper.logText += "Using framebuffer objects because ";
            if (contextcapabilities.OpenGL30) {
                OpenGlHelper.logText += "OpenGL 3.0 is supported and separate blending is supported.\n";
                OpenGlHelper.framebufferType = FboMode.BASE;
                OpenGlHelper.GL_FRAMEBUFFER = 36160;
                OpenGlHelper.GL_RENDERBUFFER = 36161;
                OpenGlHelper.GL_COLOR_ATTACHMENT0 = 36064;
                OpenGlHelper.GL_DEPTH_ATTACHMENT = 36096;
                OpenGlHelper.GL_FRAMEBUFFER_COMPLETE = 36053;
                OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
            else if (contextcapabilities.GL_ARB_framebuffer_object) {
                OpenGlHelper.logText += "ARB_framebuffer_object is supported and separate blending is supported.\n";
                OpenGlHelper.framebufferType = FboMode.ARB;
                OpenGlHelper.GL_FRAMEBUFFER = 36160;
                OpenGlHelper.GL_RENDERBUFFER = 36161;
                OpenGlHelper.GL_COLOR_ATTACHMENT0 = 36064;
                OpenGlHelper.GL_DEPTH_ATTACHMENT = 36096;
                OpenGlHelper.GL_FRAMEBUFFER_COMPLETE = 36053;
                OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
            else if (contextcapabilities.GL_EXT_framebuffer_object) {
                OpenGlHelper.logText += "EXT_framebuffer_object is supported.\n";
                OpenGlHelper.framebufferType = FboMode.EXT;
                OpenGlHelper.GL_FRAMEBUFFER = 36160;
                OpenGlHelper.GL_RENDERBUFFER = 36161;
                OpenGlHelper.GL_COLOR_ATTACHMENT0 = 36064;
                OpenGlHelper.GL_DEPTH_ATTACHMENT = 36096;
                OpenGlHelper.GL_FRAMEBUFFER_COMPLETE = 36053;
                OpenGlHelper.GL_FB_INCOMPLETE_MISS_ATTACH = 36055;
                OpenGlHelper.GL_FB_INCOMPLETE_ATTACHMENT = 36054;
                OpenGlHelper.GL_FB_INCOMPLETE_DRAW_BUFFER = 36059;
                OpenGlHelper.GL_FB_INCOMPLETE_READ_BUFFER = 36060;
            }
        }
        else {
            OpenGlHelper.logText += "Not using framebuffer objects because ";
            OpenGlHelper.logText = OpenGlHelper.logText + "OpenGL 1.4 is " + (contextcapabilities.OpenGL14 ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = OpenGlHelper.logText + "EXT_blend_func_separate is " + (contextcapabilities.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = OpenGlHelper.logText + "OpenGL 3.0 is " + (contextcapabilities.OpenGL30 ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = OpenGlHelper.logText + "ARB_framebuffer_object is " + (contextcapabilities.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
            OpenGlHelper.logText = OpenGlHelper.logText + "EXT_framebuffer_object is " + (contextcapabilities.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
        }
        OpenGlHelper.openGL21 = contextcapabilities.OpenGL21;
        OpenGlHelper.shadersAvailable = (OpenGlHelper.openGL21 || (contextcapabilities.GL_ARB_vertex_shader && contextcapabilities.GL_ARB_fragment_shader && contextcapabilities.GL_ARB_shader_objects));
        OpenGlHelper.logText = OpenGlHelper.logText + "Shaders are " + (OpenGlHelper.shadersAvailable ? "" : "not ") + "available because ";
        if (OpenGlHelper.shadersAvailable) {
            if (contextcapabilities.OpenGL21) {
                OpenGlHelper.logText += "OpenGL 2.1 is supported.\n";
                OpenGlHelper.arbShaders = false;
                OpenGlHelper.GL_LINK_STATUS = 35714;
                OpenGlHelper.GL_COMPILE_STATUS = 35713;
                OpenGlHelper.GL_VERTEX_SHADER = 35633;
                OpenGlHelper.GL_FRAGMENT_SHADER = 35632;
            }
            else {
                OpenGlHelper.logText += "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
                OpenGlHelper.arbShaders = true;
                OpenGlHelper.GL_LINK_STATUS = 35714;
                OpenGlHelper.GL_COMPILE_STATUS = 35713;
                OpenGlHelper.GL_VERTEX_SHADER = 35633;
                OpenGlHelper.GL_FRAGMENT_SHADER = 35632;
            }
        }
        else {
            OpenGlHelper.logText = OpenGlHelper.logText + "OpenGL 2.1 is " + (contextcapabilities.OpenGL21 ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = OpenGlHelper.logText + "ARB_shader_objects is " + (contextcapabilities.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
            OpenGlHelper.logText = OpenGlHelper.logText + "ARB_vertex_shader is " + (contextcapabilities.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
            OpenGlHelper.logText = OpenGlHelper.logText + "ARB_fragment_shader is " + (contextcapabilities.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
        }
        OpenGlHelper.shadersSupported = (OpenGlHelper.framebufferSupported && OpenGlHelper.shadersAvailable);
        final String s2 = GL11.glGetString(7936).toLowerCase(Locale.ROOT);
        OpenGlHelper.nvidia = s2.contains("nvidia");
        OpenGlHelper.arbVbo = (!contextcapabilities.OpenGL15 && contextcapabilities.GL_ARB_vertex_buffer_object);
        OpenGlHelper.vboSupported = (contextcapabilities.OpenGL15 || OpenGlHelper.arbVbo);
        OpenGlHelper.logText = OpenGlHelper.logText + "VBOs are " + (OpenGlHelper.vboSupported ? "" : "not ") + "available because ";
        if (OpenGlHelper.vboSupported) {
            if (OpenGlHelper.arbVbo) {
                OpenGlHelper.logText += "ARB_vertex_buffer_object is supported.\n";
                OpenGlHelper.GL_STATIC_DRAW = 35044;
                OpenGlHelper.GL_ARRAY_BUFFER = 34962;
            }
            else {
                OpenGlHelper.logText += "OpenGL 1.5 is supported.\n";
                OpenGlHelper.GL_STATIC_DRAW = 35044;
                OpenGlHelper.GL_ARRAY_BUFFER = 34962;
            }
        }
        OpenGlHelper.ati = s2.contains("ati");
        if (OpenGlHelper.ati) {
            if (OpenGlHelper.vboSupported) {
                OpenGlHelper.vboSupportedAti = true;
            }
            else {
                GameSettings.Options.RENDER_DISTANCE.setValueMax(16.0f);
            }
        }
        try {
            final Processor[] aprocessor = new SystemInfo().getHardware().getProcessors();
            OpenGlHelper.cpu = String.format("%dx %s", aprocessor.length, aprocessor[0]).replaceAll("\\s+", " ");
        }
        catch (Throwable t) {}
    }
    
    public static boolean areShadersSupported() {
        return OpenGlHelper.shadersSupported;
    }
    
    public static String getLogText() {
        return OpenGlHelper.logText;
    }
    
    public static int glGetProgrami(final int program, final int pname) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(program, pname) : GL20.glGetProgrami(program, pname);
    }
    
    public static void glAttachShader(final int program, final int shaderIn) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glAttachObjectARB(program, shaderIn);
        }
        else {
            GL20.glAttachShader(program, shaderIn);
        }
    }
    
    public static void glDeleteShader(final int shaderIn) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glDeleteObjectARB(shaderIn);
        }
        else {
            GL20.glDeleteShader(shaderIn);
        }
    }
    
    public static int glCreateShader(final int type) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glCreateShaderObjectARB(type) : GL20.glCreateShader(type);
    }
    
    public static void glShaderSource(final int shaderIn, final ByteBuffer string) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glShaderSourceARB(shaderIn, string);
        }
        else {
            GL20.glShaderSource(shaderIn, string);
        }
    }
    
    public static void glCompileShader(final int shaderIn) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glCompileShaderARB(shaderIn);
        }
        else {
            GL20.glCompileShader(shaderIn);
        }
    }
    
    public static int glGetShaderi(final int shaderIn, final int pname) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetObjectParameteriARB(shaderIn, pname) : GL20.glGetShaderi(shaderIn, pname);
    }
    
    public static String glGetShaderInfoLog(final int shaderIn, final int maxLength) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetInfoLogARB(shaderIn, maxLength) : GL20.glGetShaderInfoLog(shaderIn, maxLength);
    }
    
    public static String glGetProgramInfoLog(final int program, final int maxLength) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetInfoLogARB(program, maxLength) : GL20.glGetProgramInfoLog(program, maxLength);
    }
    
    public static void glUseProgram(final int program) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUseProgramObjectARB(program);
        }
        else {
            GL20.glUseProgram(program);
        }
    }
    
    public static int glCreateProgram() {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
    }
    
    public static void glDeleteProgram(final int program) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glDeleteObjectARB(program);
        }
        else {
            GL20.glDeleteProgram(program);
        }
    }
    
    public static void glLinkProgram(final int program) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glLinkProgramARB(program);
        }
        else {
            GL20.glLinkProgram(program);
        }
    }
    
    public static int glGetUniformLocation(final int programObj, final CharSequence name) {
        return OpenGlHelper.arbShaders ? ARBShaderObjects.glGetUniformLocationARB(programObj, name) : GL20.glGetUniformLocation(programObj, name);
    }
    
    public static void glUniform1(final int location, final IntBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform1ARB(location, values);
        }
        else {
            GL20.glUniform1(location, values);
        }
    }
    
    public static void glUniform1i(final int location, final int v0) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform1iARB(location, v0);
        }
        else {
            GL20.glUniform1i(location, v0);
        }
    }
    
    public static void glUniform1(final int location, final FloatBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform1ARB(location, values);
        }
        else {
            GL20.glUniform1(location, values);
        }
    }
    
    public static void glUniform2(final int location, final IntBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform2ARB(location, values);
        }
        else {
            GL20.glUniform2(location, values);
        }
    }
    
    public static void glUniform2(final int location, final FloatBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform2ARB(location, values);
        }
        else {
            GL20.glUniform2(location, values);
        }
    }
    
    public static void glUniform3(final int location, final IntBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform3ARB(location, values);
        }
        else {
            GL20.glUniform3(location, values);
        }
    }
    
    public static void glUniform3(final int location, final FloatBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform3ARB(location, values);
        }
        else {
            GL20.glUniform3(location, values);
        }
    }
    
    public static void glUniform4(final int location, final IntBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform4ARB(location, values);
        }
        else {
            GL20.glUniform4(location, values);
        }
    }
    
    public static void glUniform4(final int location, final FloatBuffer values) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniform4ARB(location, values);
        }
        else {
            GL20.glUniform4(location, values);
        }
    }
    
    public static void glUniformMatrix2(final int location, final boolean transpose, final FloatBuffer matrices) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniformMatrix2ARB(location, transpose, matrices);
        }
        else {
            GL20.glUniformMatrix2(location, transpose, matrices);
        }
    }
    
    public static void glUniformMatrix3(final int location, final boolean transpose, final FloatBuffer matrices) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniformMatrix3ARB(location, transpose, matrices);
        }
        else {
            GL20.glUniformMatrix3(location, transpose, matrices);
        }
    }
    
    public static void glUniformMatrix4(final int location, final boolean transpose, final FloatBuffer matrices) {
        if (OpenGlHelper.arbShaders) {
            ARBShaderObjects.glUniformMatrix4ARB(location, transpose, matrices);
        }
        else {
            GL20.glUniformMatrix4(location, transpose, matrices);
        }
    }
    
    public static int glGetAttribLocation(final int program, final CharSequence name) {
        return OpenGlHelper.arbShaders ? ARBVertexShader.glGetAttribLocationARB(program, name) : GL20.glGetAttribLocation(program, name);
    }
    
    public static int glGenBuffers() {
        return OpenGlHelper.arbVbo ? ARBVertexBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
    }
    
    public static void glBindBuffer(final int target, final int buffer) {
        if (OpenGlHelper.arbVbo) {
            ARBVertexBufferObject.glBindBufferARB(target, buffer);
        }
        else {
            GL15.glBindBuffer(target, buffer);
        }
    }
    
    public static void glBufferData(final int target, final ByteBuffer data, final int usage) {
        if (OpenGlHelper.arbVbo) {
            ARBVertexBufferObject.glBufferDataARB(target, data, usage);
        }
        else {
            GL15.glBufferData(target, data, usage);
        }
    }
    
    public static void glDeleteBuffers(final int buffer) {
        if (OpenGlHelper.arbVbo) {
            ARBVertexBufferObject.glDeleteBuffersARB(buffer);
        }
        else {
            GL15.glDeleteBuffers(buffer);
        }
    }
    
    public static boolean useVbo() {
        return !Config.isMultiTexture() && (!Config.isRenderRegions() || OpenGlHelper.vboRegions) && OpenGlHelper.vboSupported && Minecraft.getMinecraft().gameSettings.useVbo;
    }
    
    public static void glBindFramebuffer(final int target, final int framebufferIn) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case BASE: {
                    GL30.glBindFramebuffer(target, framebufferIn);
                    break;
                }
                case ARB: {
                    ARBFramebufferObject.glBindFramebuffer(target, framebufferIn);
                    break;
                }
                case EXT: {
                    EXTFramebufferObject.glBindFramebufferEXT(target, framebufferIn);
                    break;
                }
            }
        }
    }
    
    public static void glBindRenderbuffer(final int target, final int renderbuffer) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case BASE: {
                    GL30.glBindRenderbuffer(target, renderbuffer);
                    break;
                }
                case ARB: {
                    ARBFramebufferObject.glBindRenderbuffer(target, renderbuffer);
                    break;
                }
                case EXT: {
                    EXTFramebufferObject.glBindRenderbufferEXT(target, renderbuffer);
                    break;
                }
            }
        }
    }
    
    public static void glDeleteRenderbuffers(final int renderbuffer) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case BASE: {
                    GL30.glDeleteRenderbuffers(renderbuffer);
                    break;
                }
                case ARB: {
                    ARBFramebufferObject.glDeleteRenderbuffers(renderbuffer);
                    break;
                }
                case EXT: {
                    EXTFramebufferObject.glDeleteRenderbuffersEXT(renderbuffer);
                    break;
                }
            }
        }
    }
    
    public static void glDeleteFramebuffers(final int framebufferIn) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case BASE: {
                    GL30.glDeleteFramebuffers(framebufferIn);
                    break;
                }
                case ARB: {
                    ARBFramebufferObject.glDeleteFramebuffers(framebufferIn);
                    break;
                }
                case EXT: {
                    EXTFramebufferObject.glDeleteFramebuffersEXT(framebufferIn);
                    break;
                }
            }
        }
    }
    
    public static int glGenFramebuffers() {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.framebufferType) {
            case BASE: {
                return GL30.glGenFramebuffers();
            }
            case ARB: {
                return ARBFramebufferObject.glGenFramebuffers();
            }
            case EXT: {
                return EXTFramebufferObject.glGenFramebuffersEXT();
            }
            default: {
                return -1;
            }
        }
    }
    
    public static int glGenRenderbuffers() {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.framebufferType) {
            case BASE: {
                return GL30.glGenRenderbuffers();
            }
            case ARB: {
                return ARBFramebufferObject.glGenRenderbuffers();
            }
            case EXT: {
                return EXTFramebufferObject.glGenRenderbuffersEXT();
            }
            default: {
                return -1;
            }
        }
    }
    
    public static void glRenderbufferStorage(final int target, final int internalFormat, final int width, final int height) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case BASE: {
                    GL30.glRenderbufferStorage(target, internalFormat, width, height);
                    break;
                }
                case ARB: {
                    ARBFramebufferObject.glRenderbufferStorage(target, internalFormat, width, height);
                    break;
                }
                case EXT: {
                    EXTFramebufferObject.glRenderbufferStorageEXT(target, internalFormat, width, height);
                    break;
                }
            }
        }
    }
    
    public static void glFramebufferRenderbuffer(final int target, final int attachment, final int renderBufferTarget, final int renderBuffer) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case BASE: {
                    GL30.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
                    break;
                }
                case ARB: {
                    ARBFramebufferObject.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
                    break;
                }
                case EXT: {
                    EXTFramebufferObject.glFramebufferRenderbufferEXT(target, attachment, renderBufferTarget, renderBuffer);
                    break;
                }
            }
        }
    }
    
    public static int glCheckFramebufferStatus(final int target) {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.framebufferType) {
            case BASE: {
                return GL30.glCheckFramebufferStatus(target);
            }
            case ARB: {
                return ARBFramebufferObject.glCheckFramebufferStatus(target);
            }
            case EXT: {
                return EXTFramebufferObject.glCheckFramebufferStatusEXT(target);
            }
            default: {
                return -1;
            }
        }
    }
    
    public static void glFramebufferTexture2D(final int target, final int attachment, final int textarget, final int texture, final int level) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.framebufferType) {
                case BASE: {
                    GL30.glFramebufferTexture2D(target, attachment, textarget, texture, level);
                    break;
                }
                case ARB: {
                    ARBFramebufferObject.glFramebufferTexture2D(target, attachment, textarget, texture, level);
                    break;
                }
                case EXT: {
                    EXTFramebufferObject.glFramebufferTexture2DEXT(target, attachment, textarget, texture, level);
                    break;
                }
            }
        }
    }
    
    public static void setActiveTexture(final int texture) {
        if (OpenGlHelper.arbMultitexture) {
            ARBMultitexture.glActiveTextureARB(texture);
        }
        else {
            GL13.glActiveTexture(texture);
        }
    }
    
    public static void setClientActiveTexture(final int texture) {
        if (OpenGlHelper.arbMultitexture) {
            ARBMultitexture.glClientActiveTextureARB(texture);
        }
        else {
            GL13.glClientActiveTexture(texture);
        }
    }
    
    public static void setLightmapTextureCoords(final int target, final float x, final float y) {
        if (OpenGlHelper.arbMultitexture) {
            ARBMultitexture.glMultiTexCoord2fARB(target, x, y);
        }
        else {
            GL13.glMultiTexCoord2f(target, x, y);
        }
        if (target == OpenGlHelper.lightmapTexUnit) {
            OpenGlHelper.lastBrightnessX = x;
            OpenGlHelper.lastBrightnessY = y;
        }
    }
    
    public static void glBlendFunc(final int sFactorRGB, final int dFactorRGB, final int sfactorAlpha, final int dfactorAlpha) {
        if (OpenGlHelper.openGL14) {
            if (OpenGlHelper.extBlendFuncSeparate) {
                EXTBlendFuncSeparate.glBlendFuncSeparateEXT(sFactorRGB, dFactorRGB, sfactorAlpha, dfactorAlpha);
            }
            else {
                GL14.glBlendFuncSeparate(sFactorRGB, dFactorRGB, sfactorAlpha, dfactorAlpha);
            }
        }
        else {
            GL11.glBlendFunc(sFactorRGB, dFactorRGB);
        }
    }
    
    public static boolean isFramebufferEnabled() {
        return !Config.isFastRender() && !Config.isAntialiasing() && OpenGlHelper.framebufferSupported && Minecraft.getMinecraft().gameSettings.fboEnable;
    }
    
    public static void glBufferData(final int p_glBufferData_0_, final long p_glBufferData_1_, final int p_glBufferData_3_) {
        if (OpenGlHelper.arbVbo) {
            ARBVertexBufferObject.glBufferDataARB(p_glBufferData_0_, p_glBufferData_1_, p_glBufferData_3_);
        }
        else {
            GL15.glBufferData(p_glBufferData_0_, p_glBufferData_1_, p_glBufferData_3_);
        }
    }
    
    public static void glBufferSubData(final int p_glBufferSubData_0_, final long p_glBufferSubData_1_, final ByteBuffer p_glBufferSubData_3_) {
        if (OpenGlHelper.arbVbo) {
            ARBVertexBufferObject.glBufferSubDataARB(p_glBufferSubData_0_, p_glBufferSubData_1_, p_glBufferSubData_3_);
        }
        else {
            GL15.glBufferSubData(p_glBufferSubData_0_, p_glBufferSubData_1_, p_glBufferSubData_3_);
        }
    }
    
    public static void glCopyBufferSubData(final int p_glCopyBufferSubData_0_, final int p_glCopyBufferSubData_1_, final long p_glCopyBufferSubData_2_, final long p_glCopyBufferSubData_4_, final long p_glCopyBufferSubData_6_) {
        if (OpenGlHelper.openGL31) {
            GL31.glCopyBufferSubData(p_glCopyBufferSubData_0_, p_glCopyBufferSubData_1_, p_glCopyBufferSubData_2_, p_glCopyBufferSubData_4_, p_glCopyBufferSubData_6_);
        }
        else {
            ARBCopyBuffer.glCopyBufferSubData(p_glCopyBufferSubData_0_, p_glCopyBufferSubData_1_, p_glCopyBufferSubData_2_, p_glCopyBufferSubData_4_, p_glCopyBufferSubData_6_);
        }
    }
    
    public static String getCpu() {
        return (OpenGlHelper.cpu == null) ? "<unknown>" : OpenGlHelper.cpu;
    }
    
    public static void renderDirections(final int p_188785_0_) {
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GL11.glLineWidth(4.0f);
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(p_188785_0_, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, p_188785_0_, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).color(0, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, p_188785_0_).color(0, 0, 0, 255).endVertex();
        tessellator.draw();
        GL11.glLineWidth(2.0f);
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(0.0, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
        bufferbuilder.pos(p_188785_0_, 0.0, 0.0).color(255, 0, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).color(0, 255, 0, 255).endVertex();
        bufferbuilder.pos(0.0, p_188785_0_, 0.0).color(0, 255, 0, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).color(127, 127, 255, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, p_188785_0_).color(127, 127, 255, 255).endVertex();
        tessellator.draw();
        GL11.glLineWidth(1.0f);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
    }
    
    public static void openFile(final File fileIn) {
        final String s = fileIn.getAbsolutePath();
        Label_0107: {
            if (Util.getOSType() == Util.EnumOS.OSX) {
                try {
                    OpenGlHelper.LOGGER.info(s);
                    Runtime.getRuntime().exec(new String[] { "/usr/bin/open", s });
                    return;
                }
                catch (IOException ioexception1) {
                    OpenGlHelper.LOGGER.error("Couldn't open file", (Throwable)ioexception1);
                    break Label_0107;
                }
            }
            if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                final String s2 = String.format("cmd.exe /C start \"Open file\" \"%s\"", s);
                try {
                    Runtime.getRuntime().exec(s2);
                    return;
                }
                catch (IOException ioexception2) {
                    OpenGlHelper.LOGGER.error("Couldn't open file", (Throwable)ioexception2);
                }
            }
        }
        boolean flag = false;
        try {
            final Class<?> oclass = Class.forName("java.awt.Desktop");
            final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", URI.class).invoke(object, fileIn.toURI());
        }
        catch (Throwable throwable1) {
            OpenGlHelper.LOGGER.error("Couldn't open link", throwable1);
            flag = true;
        }
        if (flag) {
            OpenGlHelper.LOGGER.info("Opening via system class!");
            Sys.openURL("file://" + s);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        OpenGlHelper.logText = "";
        OpenGlHelper.lastBrightnessX = 0.0f;
        OpenGlHelper.lastBrightnessY = 0.0f;
    }
    
    enum FboMode
    {
        BASE, 
        ARB, 
        EXT;
    }
}
