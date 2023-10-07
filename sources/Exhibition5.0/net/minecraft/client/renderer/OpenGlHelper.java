// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

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
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import net.minecraft.optifine.Config;

public class OpenGlHelper
{
    public static boolean field_153197_d;
    public static int field_153198_e;
    public static int field_153199_f;
    public static int field_153200_g;
    public static int field_153201_h;
    public static int field_153202_i;
    public static int field_153203_j;
    public static int field_153204_k;
    public static int field_153205_l;
    public static int field_153206_m;
    private static int field_153212_w;
    public static boolean framebufferSupported;
    private static boolean field_153213_x;
    private static boolean field_153214_y;
    public static int GL_LINK_STATUS;
    public static int GL_COMPILE_STATUS;
    public static int GL_VERTEX_SHADER;
    public static int GL_FRAGMENT_SHADER;
    private static boolean field_153215_z;
    public static int defaultTexUnit;
    public static int lightmapTexUnit;
    public static int field_176096_r;
    private static boolean field_176088_V;
    public static int field_176095_s;
    public static int field_176094_t;
    public static int field_176093_u;
    public static int field_176092_v;
    public static int field_176091_w;
    public static int field_176099_x;
    public static int field_176098_y;
    public static int field_176097_z;
    public static int field_176080_A;
    public static int field_176081_B;
    public static int field_176082_C;
    public static int field_176076_D;
    public static int field_176077_E;
    public static int field_176078_F;
    public static int field_176079_G;
    public static int field_176084_H;
    public static int field_176085_I;
    public static int field_176086_J;
    public static int field_176087_K;
    private static boolean openGL14;
    public static boolean field_153211_u;
    public static boolean openGL21;
    public static boolean shadersSupported;
    private static String field_153196_B;
    public static boolean field_176083_O;
    private static boolean field_176090_Y;
    public static int field_176089_P;
    public static int anisotropicFilteringMax;
    private static final String __OBFID = "CL_00001179";
    public static float lastBrightnessX;
    public static float lastBrightnessY;
    
    public static void initializeTextures() {
        Config.initDisplay();
        final ContextCapabilities var0 = GLContext.getCapabilities();
        OpenGlHelper.field_153215_z = (var0.GL_ARB_multitexture && !var0.OpenGL13);
        OpenGlHelper.field_176088_V = (var0.GL_ARB_texture_env_combine && !var0.OpenGL13);
        if (OpenGlHelper.field_153215_z) {
            OpenGlHelper.field_153196_B += "Using ARB_multitexture.\n";
            OpenGlHelper.defaultTexUnit = 33984;
            OpenGlHelper.lightmapTexUnit = 33985;
            OpenGlHelper.field_176096_r = 33986;
        }
        else {
            OpenGlHelper.field_153196_B += "Using GL 1.3 multitexturing.\n";
            OpenGlHelper.defaultTexUnit = 33984;
            OpenGlHelper.lightmapTexUnit = 33985;
            OpenGlHelper.field_176096_r = 33986;
        }
        if (OpenGlHelper.field_176088_V) {
            OpenGlHelper.field_153196_B += "Using ARB_texture_env_combine.\n";
            OpenGlHelper.field_176095_s = 34160;
            OpenGlHelper.field_176094_t = 34165;
            OpenGlHelper.field_176093_u = 34167;
            OpenGlHelper.field_176092_v = 34166;
            OpenGlHelper.field_176091_w = 34168;
            OpenGlHelper.field_176099_x = 34161;
            OpenGlHelper.field_176098_y = 34176;
            OpenGlHelper.field_176097_z = 34177;
            OpenGlHelper.field_176080_A = 34178;
            OpenGlHelper.field_176081_B = 34192;
            OpenGlHelper.field_176082_C = 34193;
            OpenGlHelper.field_176076_D = 34194;
            OpenGlHelper.field_176077_E = 34162;
            OpenGlHelper.field_176078_F = 34184;
            OpenGlHelper.field_176079_G = 34185;
            OpenGlHelper.field_176084_H = 34186;
            OpenGlHelper.field_176085_I = 34200;
            OpenGlHelper.field_176086_J = 34201;
            OpenGlHelper.field_176087_K = 34202;
        }
        else {
            OpenGlHelper.field_153196_B += "Using GL 1.3 texture combiners.\n";
            OpenGlHelper.field_176095_s = 34160;
            OpenGlHelper.field_176094_t = 34165;
            OpenGlHelper.field_176093_u = 34167;
            OpenGlHelper.field_176092_v = 34166;
            OpenGlHelper.field_176091_w = 34168;
            OpenGlHelper.field_176099_x = 34161;
            OpenGlHelper.field_176098_y = 34176;
            OpenGlHelper.field_176097_z = 34177;
            OpenGlHelper.field_176080_A = 34178;
            OpenGlHelper.field_176081_B = 34192;
            OpenGlHelper.field_176082_C = 34193;
            OpenGlHelper.field_176076_D = 34194;
            OpenGlHelper.field_176077_E = 34162;
            OpenGlHelper.field_176078_F = 34184;
            OpenGlHelper.field_176079_G = 34185;
            OpenGlHelper.field_176084_H = 34186;
            OpenGlHelper.field_176085_I = 34200;
            OpenGlHelper.field_176086_J = 34201;
            OpenGlHelper.field_176087_K = 34202;
        }
        OpenGlHelper.field_153211_u = (var0.GL_EXT_blend_func_separate && !var0.OpenGL14);
        OpenGlHelper.openGL14 = (var0.OpenGL14 || var0.GL_EXT_blend_func_separate);
        OpenGlHelper.framebufferSupported = (OpenGlHelper.openGL14 && (var0.GL_ARB_framebuffer_object || var0.GL_EXT_framebuffer_object || var0.OpenGL30));
        if (OpenGlHelper.framebufferSupported) {
            OpenGlHelper.field_153196_B += "Using framebuffer objects because ";
            if (var0.OpenGL30) {
                OpenGlHelper.field_153196_B += "OpenGL 3.0 is supported and separate blending is supported.\n";
                OpenGlHelper.field_153212_w = 0;
                OpenGlHelper.field_153198_e = 36160;
                OpenGlHelper.field_153199_f = 36161;
                OpenGlHelper.field_153200_g = 36064;
                OpenGlHelper.field_153201_h = 36096;
                OpenGlHelper.field_153202_i = 36053;
                OpenGlHelper.field_153203_j = 36054;
                OpenGlHelper.field_153204_k = 36055;
                OpenGlHelper.field_153205_l = 36059;
                OpenGlHelper.field_153206_m = 36060;
            }
            else if (var0.GL_ARB_framebuffer_object) {
                OpenGlHelper.field_153196_B += "ARB_framebuffer_object is supported and separate blending is supported.\n";
                OpenGlHelper.field_153212_w = 1;
                OpenGlHelper.field_153198_e = 36160;
                OpenGlHelper.field_153199_f = 36161;
                OpenGlHelper.field_153200_g = 36064;
                OpenGlHelper.field_153201_h = 36096;
                OpenGlHelper.field_153202_i = 36053;
                OpenGlHelper.field_153204_k = 36055;
                OpenGlHelper.field_153203_j = 36054;
                OpenGlHelper.field_153205_l = 36059;
                OpenGlHelper.field_153206_m = 36060;
            }
            else if (var0.GL_EXT_framebuffer_object) {
                OpenGlHelper.field_153196_B += "EXT_framebuffer_object is supported.\n";
                OpenGlHelper.field_153212_w = 2;
                OpenGlHelper.field_153198_e = 36160;
                OpenGlHelper.field_153199_f = 36161;
                OpenGlHelper.field_153200_g = 36064;
                OpenGlHelper.field_153201_h = 36096;
                OpenGlHelper.field_153202_i = 36053;
                OpenGlHelper.field_153204_k = 36055;
                OpenGlHelper.field_153203_j = 36054;
                OpenGlHelper.field_153205_l = 36059;
                OpenGlHelper.field_153206_m = 36060;
            }
        }
        else {
            OpenGlHelper.field_153196_B += "Not using framebuffer objects because ";
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "OpenGL 1.4 is " + (var0.OpenGL14 ? "" : "not ") + "supported, ";
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "EXT_blend_func_separate is " + (var0.GL_EXT_blend_func_separate ? "" : "not ") + "supported, ";
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "OpenGL 3.0 is " + (var0.OpenGL30 ? "" : "not ") + "supported, ";
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "ARB_framebuffer_object is " + (var0.GL_ARB_framebuffer_object ? "" : "not ") + "supported, and ";
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "EXT_framebuffer_object is " + (var0.GL_EXT_framebuffer_object ? "" : "not ") + "supported.\n";
        }
        OpenGlHelper.openGL21 = var0.OpenGL21;
        OpenGlHelper.field_153213_x = (OpenGlHelper.openGL21 || (var0.GL_ARB_vertex_shader && var0.GL_ARB_fragment_shader && var0.GL_ARB_shader_objects));
        OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "Shaders are " + (OpenGlHelper.field_153213_x ? "" : "not ") + "available because ";
        if (OpenGlHelper.field_153213_x) {
            if (var0.OpenGL21) {
                OpenGlHelper.field_153196_B += "OpenGL 2.1 is supported.\n";
                OpenGlHelper.field_153214_y = false;
                OpenGlHelper.GL_LINK_STATUS = 35714;
                OpenGlHelper.GL_COMPILE_STATUS = 35713;
                OpenGlHelper.GL_VERTEX_SHADER = 35633;
                OpenGlHelper.GL_FRAGMENT_SHADER = 35632;
            }
            else {
                OpenGlHelper.field_153196_B += "ARB_shader_objects, ARB_vertex_shader, and ARB_fragment_shader are supported.\n";
                OpenGlHelper.field_153214_y = true;
                OpenGlHelper.GL_LINK_STATUS = 35714;
                OpenGlHelper.GL_COMPILE_STATUS = 35713;
                OpenGlHelper.GL_VERTEX_SHADER = 35633;
                OpenGlHelper.GL_FRAGMENT_SHADER = 35632;
            }
        }
        else {
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "OpenGL 2.1 is " + (var0.OpenGL21 ? "" : "not ") + "supported, ";
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "ARB_shader_objects is " + (var0.GL_ARB_shader_objects ? "" : "not ") + "supported, ";
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "ARB_vertex_shader is " + (var0.GL_ARB_vertex_shader ? "" : "not ") + "supported, and ";
            OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "ARB_fragment_shader is " + (var0.GL_ARB_fragment_shader ? "" : "not ") + "supported.\n";
        }
        OpenGlHelper.shadersSupported = (OpenGlHelper.framebufferSupported && OpenGlHelper.field_153213_x);
        OpenGlHelper.field_153197_d = GL11.glGetString(7936).toLowerCase().contains("nvidia");
        OpenGlHelper.field_176090_Y = (!var0.OpenGL15 && var0.GL_ARB_vertex_buffer_object);
        OpenGlHelper.field_176083_O = (var0.OpenGL15 || OpenGlHelper.field_176090_Y);
        OpenGlHelper.field_153196_B = OpenGlHelper.field_153196_B + "VBOs are " + (OpenGlHelper.field_176083_O ? "" : "not ") + "available because ";
        if (OpenGlHelper.field_176083_O) {
            if (OpenGlHelper.field_176090_Y) {
                OpenGlHelper.field_153196_B += "ARB_vertex_buffer_object is supported.\n";
                OpenGlHelper.anisotropicFilteringMax = 35044;
                OpenGlHelper.field_176089_P = 34962;
            }
            else {
                OpenGlHelper.field_153196_B += "OpenGL 1.5 is supported.\n";
                OpenGlHelper.anisotropicFilteringMax = 35044;
                OpenGlHelper.field_176089_P = 34962;
            }
        }
    }
    
    public static boolean areShadersSupported() {
        return OpenGlHelper.shadersSupported;
    }
    
    public static String func_153172_c() {
        return OpenGlHelper.field_153196_B;
    }
    
    public static int glGetProgrami(final int p_153175_0_, final int p_153175_1_) {
        return OpenGlHelper.field_153214_y ? ARBShaderObjects.glGetObjectParameteriARB(p_153175_0_, p_153175_1_) : GL20.glGetProgrami(p_153175_0_, p_153175_1_);
    }
    
    public static void glAttachShader(final int p_153178_0_, final int p_153178_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glAttachObjectARB(p_153178_0_, p_153178_1_);
        }
        else {
            GL20.glAttachShader(p_153178_0_, p_153178_1_);
        }
    }
    
    public static void glDeleteShader(final int p_153180_0_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glDeleteObjectARB(p_153180_0_);
        }
        else {
            GL20.glDeleteShader(p_153180_0_);
        }
    }
    
    public static int glCreateShader(final int p_153195_0_) {
        return OpenGlHelper.field_153214_y ? ARBShaderObjects.glCreateShaderObjectARB(p_153195_0_) : GL20.glCreateShader(p_153195_0_);
    }
    
    public static void glShaderSource(final int p_153169_0_, final ByteBuffer p_153169_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glShaderSourceARB(p_153169_0_, p_153169_1_);
        }
        else {
            GL20.glShaderSource(p_153169_0_, p_153169_1_);
        }
    }
    
    public static void glCompileShader(final int p_153170_0_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glCompileShaderARB(p_153170_0_);
        }
        else {
            GL20.glCompileShader(p_153170_0_);
        }
    }
    
    public static int glGetShaderi(final int p_153157_0_, final int p_153157_1_) {
        return OpenGlHelper.field_153214_y ? ARBShaderObjects.glGetObjectParameteriARB(p_153157_0_, p_153157_1_) : GL20.glGetShaderi(p_153157_0_, p_153157_1_);
    }
    
    public static String glGetShaderInfoLog(final int p_153158_0_, final int p_153158_1_) {
        return OpenGlHelper.field_153214_y ? ARBShaderObjects.glGetInfoLogARB(p_153158_0_, p_153158_1_) : GL20.glGetShaderInfoLog(p_153158_0_, p_153158_1_);
    }
    
    public static String glGetProgramInfoLog(final int p_153166_0_, final int p_153166_1_) {
        return OpenGlHelper.field_153214_y ? ARBShaderObjects.glGetInfoLogARB(p_153166_0_, p_153166_1_) : GL20.glGetProgramInfoLog(p_153166_0_, p_153166_1_);
    }
    
    public static void glUseProgram(final int p_153161_0_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUseProgramObjectARB(p_153161_0_);
        }
        else {
            GL20.glUseProgram(p_153161_0_);
        }
    }
    
    public static int glCreateProgram() {
        return OpenGlHelper.field_153214_y ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
    }
    
    public static void glDeleteProgram(final int p_153187_0_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glDeleteObjectARB(p_153187_0_);
        }
        else {
            GL20.glDeleteProgram(p_153187_0_);
        }
    }
    
    public static void glLinkProgram(final int p_153179_0_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glLinkProgramARB(p_153179_0_);
        }
        else {
            GL20.glLinkProgram(p_153179_0_);
        }
    }
    
    public static int glGetUniformLocation(final int p_153194_0_, final CharSequence p_153194_1_) {
        return OpenGlHelper.field_153214_y ? ARBShaderObjects.glGetUniformLocationARB(p_153194_0_, p_153194_1_) : GL20.glGetUniformLocation(p_153194_0_, p_153194_1_);
    }
    
    public static void glUniform1(final int p_153181_0_, final IntBuffer p_153181_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform1ARB(p_153181_0_, p_153181_1_);
        }
        else {
            GL20.glUniform1(p_153181_0_, p_153181_1_);
        }
    }
    
    public static void glUniform1i(final int p_153163_0_, final int p_153163_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform1iARB(p_153163_0_, p_153163_1_);
        }
        else {
            GL20.glUniform1i(p_153163_0_, p_153163_1_);
        }
    }
    
    public static void glUniform1(final int p_153168_0_, final FloatBuffer p_153168_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform1ARB(p_153168_0_, p_153168_1_);
        }
        else {
            GL20.glUniform1(p_153168_0_, p_153168_1_);
        }
    }
    
    public static void glUniform2(final int p_153182_0_, final IntBuffer p_153182_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform2ARB(p_153182_0_, p_153182_1_);
        }
        else {
            GL20.glUniform2(p_153182_0_, p_153182_1_);
        }
    }
    
    public static void glUniform2(final int p_153177_0_, final FloatBuffer p_153177_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform2ARB(p_153177_0_, p_153177_1_);
        }
        else {
            GL20.glUniform2(p_153177_0_, p_153177_1_);
        }
    }
    
    public static void glUniform3(final int p_153192_0_, final IntBuffer p_153192_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform3ARB(p_153192_0_, p_153192_1_);
        }
        else {
            GL20.glUniform3(p_153192_0_, p_153192_1_);
        }
    }
    
    public static void glUniform3(final int p_153191_0_, final FloatBuffer p_153191_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform3ARB(p_153191_0_, p_153191_1_);
        }
        else {
            GL20.glUniform3(p_153191_0_, p_153191_1_);
        }
    }
    
    public static void glUniform4(final int p_153162_0_, final IntBuffer p_153162_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform4ARB(p_153162_0_, p_153162_1_);
        }
        else {
            GL20.glUniform4(p_153162_0_, p_153162_1_);
        }
    }
    
    public static void glUniform4(final int p_153159_0_, final FloatBuffer p_153159_1_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniform4ARB(p_153159_0_, p_153159_1_);
        }
        else {
            GL20.glUniform4(p_153159_0_, p_153159_1_);
        }
    }
    
    public static void glUniformMatrix2(final int p_153173_0_, final boolean p_153173_1_, final FloatBuffer p_153173_2_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniformMatrix2ARB(p_153173_0_, p_153173_1_, p_153173_2_);
        }
        else {
            GL20.glUniformMatrix2(p_153173_0_, p_153173_1_, p_153173_2_);
        }
    }
    
    public static void glUniformMatrix3(final int p_153189_0_, final boolean p_153189_1_, final FloatBuffer p_153189_2_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniformMatrix3ARB(p_153189_0_, p_153189_1_, p_153189_2_);
        }
        else {
            GL20.glUniformMatrix3(p_153189_0_, p_153189_1_, p_153189_2_);
        }
    }
    
    public static void glUniformMatrix4(final int p_153160_0_, final boolean p_153160_1_, final FloatBuffer p_153160_2_) {
        if (OpenGlHelper.field_153214_y) {
            ARBShaderObjects.glUniformMatrix4ARB(p_153160_0_, p_153160_1_, p_153160_2_);
        }
        else {
            GL20.glUniformMatrix4(p_153160_0_, p_153160_1_, p_153160_2_);
        }
    }
    
    public static int glGetAttribLocation(final int p_153164_0_, final CharSequence p_153164_1_) {
        return OpenGlHelper.field_153214_y ? ARBVertexShader.glGetAttribLocationARB(p_153164_0_, p_153164_1_) : GL20.glGetAttribLocation(p_153164_0_, p_153164_1_);
    }
    
    public static int func_176073_e() {
        return OpenGlHelper.field_176090_Y ? ARBVertexBufferObject.glGenBuffersARB() : GL15.glGenBuffers();
    }
    
    public static void func_176072_g(final int p_176072_0_, final int p_176072_1_) {
        if (OpenGlHelper.field_176090_Y) {
            ARBVertexBufferObject.glBindBufferARB(p_176072_0_, p_176072_1_);
        }
        else {
            GL15.glBindBuffer(p_176072_0_, p_176072_1_);
        }
    }
    
    public static void func_176071_a(final int p_176071_0_, final ByteBuffer p_176071_1_, final int p_176071_2_) {
        if (OpenGlHelper.field_176090_Y) {
            ARBVertexBufferObject.glBufferDataARB(p_176071_0_, p_176071_1_, p_176071_2_);
        }
        else {
            GL15.glBufferData(p_176071_0_, p_176071_1_, p_176071_2_);
        }
    }
    
    public static void func_176074_g(final int p_176074_0_) {
        if (OpenGlHelper.field_176090_Y) {
            ARBVertexBufferObject.glDeleteBuffersARB(p_176074_0_);
        }
        else {
            GL15.glDeleteBuffers(p_176074_0_);
        }
    }
    
    public static boolean func_176075_f() {
        return OpenGlHelper.field_176083_O && Minecraft.getMinecraft().gameSettings.field_178881_t;
    }
    
    public static void func_153171_g(final int p_153171_0_, final int p_153171_1_) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.field_153212_w) {
                case 0: {
                    GL30.glBindFramebuffer(p_153171_0_, p_153171_1_);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glBindFramebuffer(p_153171_0_, p_153171_1_);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glBindFramebufferEXT(p_153171_0_, p_153171_1_);
                    break;
                }
            }
        }
    }
    
    public static void func_153176_h(final int p_153176_0_, final int p_153176_1_) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.field_153212_w) {
                case 0: {
                    GL30.glBindRenderbuffer(p_153176_0_, p_153176_1_);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glBindRenderbuffer(p_153176_0_, p_153176_1_);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glBindRenderbufferEXT(p_153176_0_, p_153176_1_);
                    break;
                }
            }
        }
    }
    
    public static void func_153184_g(final int p_153184_0_) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.field_153212_w) {
                case 0: {
                    GL30.glDeleteRenderbuffers(p_153184_0_);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glDeleteRenderbuffers(p_153184_0_);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glDeleteRenderbuffersEXT(p_153184_0_);
                    break;
                }
            }
        }
    }
    
    public static void func_153174_h(final int p_153174_0_) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.field_153212_w) {
                case 0: {
                    GL30.glDeleteFramebuffers(p_153174_0_);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glDeleteFramebuffers(p_153174_0_);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glDeleteFramebuffersEXT(p_153174_0_);
                    break;
                }
            }
        }
    }
    
    public static int func_153165_e() {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.field_153212_w) {
            case 0: {
                return GL30.glGenFramebuffers();
            }
            case 1: {
                return ARBFramebufferObject.glGenFramebuffers();
            }
            case 2: {
                return EXTFramebufferObject.glGenFramebuffersEXT();
            }
            default: {
                return -1;
            }
        }
    }
    
    public static int func_153185_f() {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.field_153212_w) {
            case 0: {
                return GL30.glGenRenderbuffers();
            }
            case 1: {
                return ARBFramebufferObject.glGenRenderbuffers();
            }
            case 2: {
                return EXTFramebufferObject.glGenRenderbuffersEXT();
            }
            default: {
                return -1;
            }
        }
    }
    
    public static void func_153186_a(final int p_153186_0_, final int p_153186_1_, final int p_153186_2_, final int p_153186_3_) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.field_153212_w) {
                case 0: {
                    GL30.glRenderbufferStorage(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glRenderbufferStorage(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glRenderbufferStorageEXT(p_153186_0_, p_153186_1_, p_153186_2_, p_153186_3_);
                    break;
                }
            }
        }
    }
    
    public static void func_153190_b(final int p_153190_0_, final int p_153190_1_, final int p_153190_2_, final int p_153190_3_) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.field_153212_w) {
                case 0: {
                    GL30.glFramebufferRenderbuffer(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glFramebufferRenderbuffer(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glFramebufferRenderbufferEXT(p_153190_0_, p_153190_1_, p_153190_2_, p_153190_3_);
                    break;
                }
            }
        }
    }
    
    public static int func_153167_i(final int p_153167_0_) {
        if (!OpenGlHelper.framebufferSupported) {
            return -1;
        }
        switch (OpenGlHelper.field_153212_w) {
            case 0: {
                return GL30.glCheckFramebufferStatus(p_153167_0_);
            }
            case 1: {
                return ARBFramebufferObject.glCheckFramebufferStatus(p_153167_0_);
            }
            case 2: {
                return EXTFramebufferObject.glCheckFramebufferStatusEXT(p_153167_0_);
            }
            default: {
                return -1;
            }
        }
    }
    
    public static void func_153188_a(final int p_153188_0_, final int p_153188_1_, final int p_153188_2_, final int p_153188_3_, final int p_153188_4_) {
        if (OpenGlHelper.framebufferSupported) {
            switch (OpenGlHelper.field_153212_w) {
                case 0: {
                    GL30.glFramebufferTexture2D(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
                    break;
                }
                case 1: {
                    ARBFramebufferObject.glFramebufferTexture2D(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
                    break;
                }
                case 2: {
                    EXTFramebufferObject.glFramebufferTexture2DEXT(p_153188_0_, p_153188_1_, p_153188_2_, p_153188_3_, p_153188_4_);
                    break;
                }
            }
        }
    }
    
    public static void setActiveTexture(final int p_77473_0_) {
        if (OpenGlHelper.field_153215_z) {
            ARBMultitexture.glActiveTextureARB(p_77473_0_);
        }
        else {
            GL13.glActiveTexture(p_77473_0_);
        }
    }
    
    public static void setClientActiveTexture(final int p_77472_0_) {
        if (OpenGlHelper.field_153215_z) {
            ARBMultitexture.glClientActiveTextureARB(p_77472_0_);
        }
        else {
            GL13.glClientActiveTexture(p_77472_0_);
        }
    }
    
    public static void setLightmapTextureCoords(final int p_77475_0_, final float p_77475_1_, final float p_77475_2_) {
        if (OpenGlHelper.field_153215_z) {
            ARBMultitexture.glMultiTexCoord2fARB(p_77475_0_, p_77475_1_, p_77475_2_);
        }
        else {
            GL13.glMultiTexCoord2f(p_77475_0_, p_77475_1_, p_77475_2_);
        }
        if (p_77475_0_ == OpenGlHelper.lightmapTexUnit) {
            OpenGlHelper.lastBrightnessX = p_77475_1_;
            OpenGlHelper.lastBrightnessY = p_77475_2_;
        }
    }
    
    public static void glBlendFunc(final int p_148821_0_, final int p_148821_1_, final int p_148821_2_, final int p_148821_3_) {
        if (OpenGlHelper.openGL14) {
            if (OpenGlHelper.field_153211_u) {
                EXTBlendFuncSeparate.glBlendFuncSeparateEXT(p_148821_0_, p_148821_1_, p_148821_2_, p_148821_3_);
            }
            else {
                GL14.glBlendFuncSeparate(p_148821_0_, p_148821_1_, p_148821_2_, p_148821_3_);
            }
        }
        else {
            GL11.glBlendFunc(p_148821_0_, p_148821_1_);
        }
    }
    
    public static boolean isFramebufferEnabled() {
        return OpenGlHelper.framebufferSupported && Minecraft.getMinecraft().gameSettings.fboEnable;
    }
    
    static {
        OpenGlHelper.field_153196_B = "";
        OpenGlHelper.lastBrightnessX = 0.0f;
        OpenGlHelper.lastBrightnessY = 0.0f;
    }
}
