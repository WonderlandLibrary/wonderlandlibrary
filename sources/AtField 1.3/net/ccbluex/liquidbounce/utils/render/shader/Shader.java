/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.io.IOUtils
 *  org.lwjgl.opengl.ARBShaderObjects
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.shader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class Shader
extends MinecraftInstance {
    private int program;
    private Map uniformsMap;

    public abstract void updateUniforms();

    private int createShader(String string, int n) {
        int n2 = 0;
        try {
            n2 = ARBShaderObjects.glCreateShaderObjectARB((int)n);
            if (n2 == 0) {
                return 0;
            }
            ARBShaderObjects.glShaderSourceARB((int)n2, (CharSequence)string);
            ARBShaderObjects.glCompileShaderARB((int)n2);
            if (ARBShaderObjects.glGetObjectParameteriARB((int)n2, (int)35713) == 0) {
                throw new RuntimeException("Error creating shader: " + this.getLogInfo(n2));
            }
            return n2;
        }
        catch (Exception exception) {
            ARBShaderObjects.glDeleteObjectARB((int)n2);
            throw exception;
        }
    }

    public void setUniform(String string, int n) {
        this.uniformsMap.put(string, n);
    }

    private String getLogInfo(int n) {
        return ARBShaderObjects.glGetInfoLogARB((int)n, (int)ARBShaderObjects.glGetObjectParameteriARB((int)n, (int)35716));
    }

    public int getProgramId() {
        return this.program;
    }

    public int getUniform(String string) {
        return (Integer)this.uniformsMap.get(string);
    }

    public void setupUniform(String string) {
        this.setUniform(string, GL20.glGetUniformLocation((int)this.program, (CharSequence)string));
    }

    public Shader(String string) {
        int n;
        int n2;
        try {
            InputStream inputStream = this.getClass().getResourceAsStream("/assets/minecraft/More/shader/vertex.vert");
            n2 = this.createShader(IOUtils.toString((InputStream)inputStream), 35633);
            IOUtils.closeQuietly((InputStream)inputStream);
            InputStream inputStream2 = this.getClass().getResourceAsStream("/assets/minecraft/More/shader/fragment/" + string);
            n = this.createShader(IOUtils.toString((InputStream)inputStream2), 35632);
            IOUtils.closeQuietly((InputStream)inputStream2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
        if (n2 == 0 || n == 0) {
            return;
        }
        this.program = ARBShaderObjects.glCreateProgramObjectARB();
        if (this.program == 0) {
            return;
        }
        ARBShaderObjects.glAttachObjectARB((int)this.program, (int)n2);
        ARBShaderObjects.glAttachObjectARB((int)this.program, (int)n);
        ARBShaderObjects.glLinkProgramARB((int)this.program);
        ARBShaderObjects.glValidateProgramARB((int)this.program);
        ClientUtils.getLogger().info("[Shader] Successfully loaded: " + string);
    }

    public void startShader() {
        GL11.glPushMatrix();
        GL20.glUseProgram((int)this.program);
        if (this.uniformsMap == null) {
            this.uniformsMap = new HashMap();
            this.setupUniforms();
        }
        this.updateUniforms();
    }

    public void stopShader() {
        GL20.glUseProgram((int)0);
        GL11.glPopMatrix();
    }

    public abstract void setupUniforms();
}

