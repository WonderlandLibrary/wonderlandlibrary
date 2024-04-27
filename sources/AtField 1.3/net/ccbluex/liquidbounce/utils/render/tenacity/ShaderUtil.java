/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.tenacity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import net.ccbluex.liquidbounce.utils.render.tenacity.FileUtils;
import net.ccbluex.liquidbounce.utils.render.tenacity.normal.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderUtil
implements Utils {
    private final String roundedRectGradient = "#version 120\n\nuniform vec2 location, rectSize;\nuniform vec4 color1, color2, color3, color4;\nuniform float radius;\n\n#define NOISE .5/255.0\n\nfloat roundSDF(vec2 p, vec2 b, float r) {\n    return length(max(abs(p) - b , 0.0)) - r;\n}\n\nvec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){\n    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);\n    //Dithering the color\n    // from https://shader-tutorial.dev/advanced/color-banding-dithering/\n    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));\n    return color;\n}\n\nvoid main() {\n    vec2 st = gl_TexCoord[0].st;\n    vec2 halfSize = rectSize * .5;\n    \n    float smoothedAlpha =  (1.0-smoothstep(0.0, 2., roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - radius - 1., radius))) * color1.a;\n    gl_FragColor = vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha);\n}";
    private final int programID;
    private String roundedRect = "#version 120\n\nuniform vec2 location, rectSize;\nuniform vec4 color;\nuniform float radius;\nuniform bool blur;\n\nfloat roundSDF(vec2 p, vec2 b, float r) {\n    return length(max(abs(p) - b, 0.0)) - r;\n}\n\n\nvoid main() {\n    vec2 rectHalf = rectSize * .5;\n    // Smooth the result (free antialiasing).\n    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;\n    gl_FragColor = vec4(color.rgb, smoothedAlpha);// mix(quadColor, shadowColor, 0.0);\n\n}";

    public void unload() {
        GL20.glUseProgram((int)0);
    }

    public void setUniformf(String string, float ... fArray) {
        int n = GL20.glGetUniformLocation((int)this.programID, (CharSequence)string);
        switch (fArray.length) {
            case 1: {
                GL20.glUniform1f((int)n, (float)fArray[0]);
                break;
            }
            case 2: {
                GL20.glUniform2f((int)n, (float)fArray[0], (float)fArray[1]);
                break;
            }
            case 3: {
                GL20.glUniform3f((int)n, (float)fArray[0], (float)fArray[1], (float)fArray[2]);
                break;
            }
            case 4: {
                GL20.glUniform4f((int)n, (float)fArray[0], (float)fArray[1], (float)fArray[2], (float)fArray[3]);
            }
        }
    }

    public ShaderUtil(String string, String string2) {
        int n = GL20.glCreateProgram();
        try {
            int n2;
            switch (string) {
                case "roundedRect": {
                    n2 = this.createShader(new ByteArrayInputStream(this.roundedRect.getBytes()), 35632);
                    break;
                }
                case "roundedRectGradient": {
                    n2 = this.createShader(new ByteArrayInputStream("#version 120\n\nuniform vec2 location, rectSize;\nuniform vec4 color1, color2, color3, color4;\nuniform float radius;\n\n#define NOISE .5/255.0\n\nfloat roundSDF(vec2 p, vec2 b, float r) {\n    return length(max(abs(p) - b , 0.0)) - r;\n}\n\nvec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){\n    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);\n    //Dithering the color\n    // from https://shader-tutorial.dev/advanced/color-banding-dithering/\n    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));\n    return color;\n}\n\nvoid main() {\n    vec2 st = gl_TexCoord[0].st;\n    vec2 halfSize = rectSize * .5;\n    \n    float smoothedAlpha =  (1.0-smoothstep(0.0, 2., roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - radius - 1., radius))) * color1.a;\n    gl_FragColor = vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha);\n}".getBytes()), 35632);
                    break;
                }
                default: {
                    n2 = this.createShader(Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation(string)).func_110527_b(), 35632);
                }
            }
            GL20.glAttachShader((int)n, (int)n2);
            int n3 = this.createShader(Minecraft.func_71410_x().func_110442_L().func_110536_a(new ResourceLocation(string2)).func_110527_b(), 35633);
            GL20.glAttachShader((int)n, (int)n3);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        GL20.glLinkProgram((int)n);
        int n4 = GL20.glGetProgrami((int)n, (int)35714);
        if (n4 == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = n;
    }

    public static void drawQuads(float f, float f2, float f3, float f4) {
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)f, (float)(f2 + f4));
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)(f + f3), (float)(f2 + f4));
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)(f + f3), (float)f2);
        GL11.glEnd();
    }

    public void init() {
        GL20.glUseProgram((int)this.programID);
    }

    public ShaderUtil(String string) {
        this(string, "More/shader/vertex.vsh");
    }

    public int getUniform(String string) {
        return GL20.glGetUniformLocation((int)this.programID, (CharSequence)string);
    }

    public void setUniformi(String string, int ... nArray) {
        int n = GL20.glGetUniformLocation((int)this.programID, (CharSequence)string);
        if (nArray.length > 1) {
            GL20.glUniform2i((int)n, (int)nArray[0], (int)nArray[1]);
        } else {
            GL20.glUniform1i((int)n, (int)nArray[0]);
        }
    }

    private int createShader(InputStream inputStream, int n) {
        int n2 = GL20.glCreateShader((int)n);
        GL20.glShaderSource((int)n2, (CharSequence)FileUtils.readInputStream(inputStream));
        GL20.glCompileShader((int)n2);
        if (GL20.glGetShaderi((int)n2, (int)35713) == 0) {
            System.out.println(GL20.glGetShaderInfoLog((int)n2, (int)4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", n));
        }
        return n2;
    }

    public static void drawQuads() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        float f = (float)scaledResolution.func_78327_c();
        float f2 = (float)scaledResolution.func_78324_d();
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)0.0f, (float)f2);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)f, (float)f2);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)f, (float)0.0f);
        GL11.glEnd();
    }
}

