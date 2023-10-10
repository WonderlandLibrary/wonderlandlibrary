/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package com.wallhacks.losebypass.utils;

import com.wallhacks.losebypass.utils.MC;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderUtil
implements MC {
    private final int programID;

    public ShaderUtil(String fragmentShaderLoc, String vertexShaderLoc) {
        int program = GL20.glCreateProgram();
        try {
            int fragmentShaderID = this.createShader(mc.getResourceManager().getResource(new ResourceLocation(fragmentShaderLoc)).getInputStream(), 35632);
            GL20.glAttachShader((int)program, (int)fragmentShaderID);
            int vertexShaderID = this.createShader(mc.getResourceManager().getResource(new ResourceLocation(vertexShaderLoc)).getInputStream(), 35633);
            GL20.glAttachShader((int)program, (int)vertexShaderID);
        }
        catch (Exception fragmentShaderID) {
            // empty catch block
        }
        GL20.glLinkProgram((int)program);
        int status = GL20.glGetProgrami((int)program, (int)35714);
        if (status == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = program;
    }

    public ShaderUtil(String fragmentShaderLoc) {
        this(fragmentShaderLoc, "textures/shaders/vertex.vsh");
    }

    public void init() {
        GL20.glUseProgram((int)this.programID);
    }

    public void unload() {
        GL20.glUseProgram((int)0);
    }

    public int getUniform(String name) {
        return GL20.glGetUniformLocation((int)this.programID, (CharSequence)name);
    }

    public void setUniformf(String name, float ... args) {
        int loc = GL20.glGetUniformLocation((int)this.programID, (CharSequence)name);
        switch (args.length) {
            case 1: {
                GL20.glUniform1f((int)loc, (float)args[0]);
                return;
            }
            case 2: {
                GL20.glUniform2f((int)loc, (float)args[0], (float)args[1]);
                return;
            }
            case 3: {
                GL20.glUniform3f((int)loc, (float)args[0], (float)args[1], (float)args[2]);
                return;
            }
            case 4: {
                GL20.glUniform4f((int)loc, (float)args[0], (float)args[1], (float)args[2], (float)args[3]);
                return;
            }
        }
    }

    public void setUniformi(String name, int ... args) {
        int loc = GL20.glGetUniformLocation((int)this.programID, (CharSequence)name);
        if (args.length > 1) {
            GL20.glUniform2i((int)loc, (int)args[0], (int)args[1]);
            return;
        }
        GL20.glUniform1i((int)loc, (int)args[0]);
    }

    public static void drawQuads() {
        ScaledResolution sr = new ScaledResolution(mc);
        float width = (float)sr.getScaledWidth_double();
        float height = (float)sr.getScaledHeight_double();
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2f((float)0.0f, (float)0.0f);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2f((float)0.0f, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2f((float)width, (float)height);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)width, (float)0.0f);
        GL11.glEnd();
    }

    private int createShader(InputStream inputStream, int shaderType) {
        int shader = GL20.glCreateShader((int)shaderType);
        GL20.glShaderSource((int)shader, (CharSequence)ShaderUtil.readInputStream(inputStream));
        GL20.glCompileShader((int)shader);
        if (GL20.glGetShaderi((int)shader, (int)35713) != 0) return shader;
        System.out.println(GL20.glGetShaderInfoLog((int)shader, (int)4096));
        throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            return stringBuilder.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}

