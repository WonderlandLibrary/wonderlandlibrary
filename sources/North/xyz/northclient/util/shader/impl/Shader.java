package xyz.northclient.util.shader.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import xyz.northclient.util.shader.RectUtil;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public abstract class Shader {
    protected int programID;
    public abstract String getSource();

    private String vertexShaderSrc = "#version 120\n" +
            "\n" +
            "void main() {\n" +
            "    gl_Position = ftransform();\n" +
            "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
            "}";
    public abstract void render(float x, float y, float width, float height);

    public static void renderQuad(float x, float y,float width, float height) {
        GL11.glBegin(GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x, y + height);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x + width, y + height);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();

        RectUtil.end();
    }

    public static void renderQuad() {
        renderQuad(0,0,new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(),new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight());
    }

    public Shader() {
        int vertexShader;
        int fragmentShader;

        vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(vertexShader, vertexShaderSrc);
        GL20.glShaderSource(fragmentShader, getSource());

        GL20.glCompileShader(vertexShader);
        GL20.glCompileShader(fragmentShader);

        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID,vertexShader);
        GL20.glAttachShader(programID,fragmentShader);
        GL20.glLinkProgram(programID);

        if(GL20.glGetShaderi(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.out.println("Failed to compile " + this.getClass().getSimpleName() + ": " + GL20.glGetProgramInfoLog(programID, 512));
        }
    }

    public void uniform1f(String name, float v) {
        GL20.glUniform1f(GL20.glGetUniformLocation(programID,name),v);
    }

    public void uniform2f(String name, float v, float v1) {
        GL20.glUniform2f(GL20.glGetUniformLocation(programID,name),v,v1);
    }

    public void uniform3f(String name, float v, float v1, float v2) {
        GL20.glUniform3f(GL20.glGetUniformLocation(programID,name),v,v1,v2);
    }

    public void uniform4f(String name, float v, float v1, float v2, float v3) {
        GL20.glUniform4f(GL20.glGetUniformLocation(programID,name),v,v1,v2,v3);
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void uniform1i(String name, int v) {
        GL20.glUniform1i(GL20.glGetUniformLocation(programID,name),v);
    }
}
