/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL20
 */
package net.ccbluex.liquidbounce.utils.render.tenacity;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL20;

public class GLShader {
    private int program;
    private final Map uniformLocationMap = new HashMap();

    public void setupUniform(String string) {
        this.uniformLocationMap.put(string, GL20.glGetUniformLocation((int)this.program, (CharSequence)string));
    }

    public void updateUniforms() {
    }

    public void use() {
        GL20.glUseProgram((int)this.program);
        this.updateUniforms();
    }

    public GLShader(String string, String string2) {
        this.program = GL20.glCreateProgram();
        GL20.glAttachShader((int)this.program, (int)GLShader.createShader(string, 35633));
        GL20.glAttachShader((int)this.program, (int)GLShader.createShader(string2, 35632));
        GL20.glLinkProgram((int)this.program);
        int n = GL20.glGetProgrami((int)this.program, (int)35714);
        if (n == 0) {
            this.program = -1;
            return;
        }
        this.setupUniforms();
    }

    private static int createShader(String string, int n) {
        int n2 = GL20.glCreateShader((int)n);
        GL20.glShaderSource((int)n2, (CharSequence)string);
        GL20.glCompileShader((int)n2);
        int n3 = GL20.glGetShaderi((int)n2, (int)35713);
        if (n3 == 0) {
            return -1;
        }
        return n2;
    }

    public int getProgram() {
        return this.program;
    }

    public void setupUniforms() {
    }

    public int getUniformLocation(String string) {
        return (Integer)this.uniformLocationMap.get(string);
    }
}

