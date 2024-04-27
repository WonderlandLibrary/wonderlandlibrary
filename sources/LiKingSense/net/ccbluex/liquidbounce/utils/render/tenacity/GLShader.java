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
    private final Map<String, Integer> uniformLocationMap = new HashMap<String, Integer>();

    public GLShader(String vertexSource, String fragSource) {
        this.program = GL20.glCreateProgram();
        GL20.glAttachShader((int)this.program, (int)GLShader.createShader(vertexSource, 35633));
        GL20.glAttachShader((int)this.program, (int)GLShader.createShader(fragSource, 35632));
        GL20.glLinkProgram((int)this.program);
        int status = GL20.glGetProgrami((int)this.program, (int)35714);
        if (status == 0) {
            this.program = -1;
            return;
        }
        this.setupUniforms();
    }

    private static int createShader(String source, int type) {
        int shader = GL20.glCreateShader((int)type);
        GL20.glShaderSource((int)shader, (CharSequence)source);
        GL20.glCompileShader((int)shader);
        int status = GL20.glGetShaderi((int)shader, (int)35713);
        if (status == 0) {
            return -1;
        }
        return shader;
    }

    public void use() {
        GL20.glUseProgram((int)this.program);
        this.updateUniforms();
    }

    public int getProgram() {
        return this.program;
    }

    public void setupUniforms() {
    }

    public void updateUniforms() {
    }

    public void setupUniform(String uniform) {
        this.uniformLocationMap.put(uniform, GL20.glGetUniformLocation((int)this.program, (CharSequence)uniform));
    }

    public int getUniformLocation(String uniform) {
        return this.uniformLocationMap.get(uniform);
    }
}

