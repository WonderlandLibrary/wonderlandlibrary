// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.utils.shader;

import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import org.lwjgl.opengl.GL20;

public class ShaderLoader
{
    private final int shaderProgram;
    private final int timeUniform;
    private final int mouseUniform;
    private final int resolutionUniform;
    
    public ShaderLoader(final String vertexSource, final String fragmentSource) {
        this.shaderProgram = GL20.glCreateProgram();
        final int vertexShader = GL20.glCreateShader(35633);
        final int fragmentShader = GL20.glCreateShader(35632);
        String vertexShaderSource = "";
        final StringBuilder fragmentShaderSource = new StringBuilder();
        final ClassLoader classLoader = this.getClass().getClassLoader();
        try (final InputStream inputStream = classLoader.getResourceAsStream(vertexSource)) {
            vertexShaderSource = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            System.err.println("Vertex shader was not load properly");
        }
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(fragmentSource));
            String line;
            while ((line = reader.readLine()) != null) {
                fragmentShaderSource.append(line).append('\n');
            }
            reader.close();
        }
        catch (IOException e) {
            System.err.println("Fragment shader was not load properly");
        }
        GL20.glShaderSource(vertexShader, vertexShaderSource);
        GL20.glCompileShader(vertexShader);
        if (GL20.glGetShaderi(vertexShader, 35713) == 0) {
            System.err.println("Vertex shader wasn't able to be compiled correctly :(");
        }
        GL20.glShaderSource(fragmentShader, fragmentShaderSource);
        GL20.glCompileShader(fragmentShader);
        if (GL20.glGetShaderi(fragmentShader, 35713) == 0) {
            System.err.println("Fragment shader wasn't able to be compiled correctly :(");
        }
        GL20.glAttachShader(this.shaderProgram, vertexShader);
        GL20.glAttachShader(this.shaderProgram, fragmentShader);
        GL20.glLinkProgram(this.shaderProgram);
        GL20.glValidateProgram(this.shaderProgram);
        GL20.glUseProgram(this.shaderProgram);
        this.timeUniform = GL20.glGetUniformLocation(this.shaderProgram, "time");
        this.mouseUniform = GL20.glGetUniformLocation(this.shaderProgram, "mouse");
        this.resolutionUniform = GL20.glGetUniformLocation(this.shaderProgram, "resolution");
        GL20.glUseProgram(0);
    }
    
    public void startShader(final int width, final int height, final float mouseX, final float mouseY, final float time) {
        GL20.glUseProgram(this.shaderProgram);
        GL20.glUniform2f(this.resolutionUniform, (float)width, (float)height);
        GL20.glUniform2f(this.mouseUniform, mouseX / width, 1.0f - mouseY / height);
        GL20.glUniform1f(this.timeUniform, time);
    }
    
    public void endShader() {
        GL20.glUseProgram(0);
    }
}
