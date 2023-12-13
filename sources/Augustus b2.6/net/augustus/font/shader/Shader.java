// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.font.shader;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Matrix3f;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Shader
{
    private int shaderProgramID;
    private boolean beingUsed;
    private String vertexSource;
    private String fragmentSource;
    private String filepath;
    
    public Shader(final String filepath) {
        this.beingUsed = false;
        this.filepath = filepath;
        try {
            final String source = new String(Files.readAllBytes(Paths.get(filepath, new String[0])));
            final String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            final String firstPattern = source.substring(index, eol).trim();
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            final String secondPattern = source.substring(index, eol).trim();
            if (firstPattern.equals("vertex")) {
                this.vertexSource = splitString[1];
            }
            else {
                if (!firstPattern.equals("fragment")) {
                    throw new IOException("Unexpected token '" + firstPattern + "'");
                }
                this.fragmentSource = splitString[1];
            }
            if (secondPattern.equals("vertex")) {
                this.vertexSource = splitString[2];
            }
            else {
                if (!secondPattern.equals("fragment")) {
                    throw new IOException("Unexpected token '" + secondPattern + "'");
                }
                this.fragmentSource = splitString[2];
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }
        this.compile();
    }
    
    public void compile() {
        final int vertexID = GL20.glCreateShader(35633);
        GL20.glShaderSource(vertexID, this.vertexSource);
        GL20.glCompileShader(vertexID);
        int success = GL20.glGetShaderi(vertexID, 35713);
        if (success == 0) {
            final int len = GL20.glGetShaderi(vertexID, 35716);
            System.out.println("ERROR: '" + this.filepath + "'\n\tVertex shader compilation failed.");
            System.out.println(GL20.glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }
        final int fragmentID = GL20.glCreateShader(35632);
        GL20.glShaderSource(fragmentID, this.fragmentSource);
        GL20.glCompileShader(fragmentID);
        success = GL20.glGetShaderi(fragmentID, 35713);
        if (success == 0) {
            final int len = GL20.glGetShaderi(fragmentID, 35716);
            System.out.println("ERROR: '" + this.filepath + "'\n\tFragment shader compilation failed.");
            System.out.println(GL20.glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }
        GL20.glAttachShader(this.shaderProgramID = GL20.glCreateProgram(), vertexID);
        GL20.glAttachShader(this.shaderProgramID, fragmentID);
        GL20.glLinkProgram(this.shaderProgramID);
        success = GL20.glGetProgrami(this.shaderProgramID, 35714);
        if (success == 0) {
            final int len = GL20.glGetProgrami(this.shaderProgramID, 35716);
            System.out.println("ERROR: '" + this.filepath + "'\n\tLinking of shaders failed.");
            System.out.println(GL20.glGetProgramInfoLog(this.shaderProgramID, len));
            assert false : "";
        }
    }
    
    public void use() {
        if (!this.beingUsed) {
            GL20.glUseProgram(this.shaderProgramID);
            this.beingUsed = true;
        }
    }
    
    public void detach() {
        GL20.glUseProgram(0);
        this.beingUsed = false;
    }
    
    public void uploadMat4f(final String varName, final Matrix4f mat4) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        final FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        GL20.glUniformMatrix4(varLocation, false, matBuffer);
    }
    
    public void uploadMat3f(final String varName, final Matrix3f mat3) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        final FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        GL20.glUniformMatrix3(varLocation, false, matBuffer);
    }
    
    public void uploadVec4f(final String varName, final Vector4f vec) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL20.glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }
    
    public void uploadVec3f(final String varName, final Vector3f vec) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL20.glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }
    
    public void uploadVec2f(final String varName, final Vector2f vec) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL20.glUniform2f(varLocation, vec.x, vec.y);
    }
    
    public void uploadFloat(final String varName, final float val) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL20.glUniform1f(varLocation, val);
    }
    
    public void uploadInt(final String varName, final int val) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL20.glUniform1i(varLocation, val);
    }
    
    public void uploadTexture(final String varName, final int slot) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
        GL20.glUniform1i(varLocation, slot);
    }
    
    public void uploadIntArray(final String varName, final int[] array) {
        final int varLocation = GL20.glGetUniformLocation(this.shaderProgramID, varName);
        this.use();
    }
}
