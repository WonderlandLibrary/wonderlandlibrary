package de.tired.util.render.shaderloader.shader;


import de.tired.base.interfaces.IHook;
import de.tired.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements IHook {

    private final String vertexName, fragmentName;

    private final int programID;

    public ShaderProgram(final String vertexName, final String fragmentName) {
        this.vertexName = vertexName;
        this.fragmentName = fragmentName;

        final int program = glCreateProgram();

        final String vertexSource = RenderUtil.instance.readShader(vertexName);
        final int vertexShaderID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShaderID, vertexSource);
        glCompileShader(vertexShaderID);

        if (glGetShaderi(vertexShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(vertexShaderID, 4096));
            throw new IllegalStateException(String.format("Vertex Shader (%s) failed to compile!", GL_VERTEX_SHADER));
        }

        final String fragmentSource = RenderUtil.instance.readShader(fragmentName);
        int fragmentShaderID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShaderID, fragmentSource);
        glCompileShader(fragmentShaderID);

        if (glGetShaderi(fragmentShaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(fragmentShaderID, 4096));
            throw new IllegalStateException(String.format("Fragment Shader failed to compile!: " + fragmentName, GL_FRAGMENT_SHADER));
        }

        glAttachShader(program, vertexShaderID);
        glAttachShader(program, fragmentShaderID);
        glLinkProgram(program);
        this.programID = program;
    }

    public ShaderProgram(String fragmentName) {
        this("vertex/vertex.vert", fragmentName);
    }

    public void renderTexture() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float width = (float) sr.getScaledWidth_double();
        float height = (float) sr.getScaledHeight_double();
        renderFR(0, 0, width, height);
    }

    public void renderFrameBufferOnly(Framebuffer framebuffer) {
        final ScaledResolution scaledResolution = new ScaledResolution(MC);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebuffer.framebufferTexture);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2d(0, 1);
            GL11.glVertex2d(0, 0);
            GL11.glTexCoord2d(0, 0);
            GL11.glVertex2d(0, scaledResolution.getScaledHeight());
            GL11.glTexCoord2d(1, 0);
            GL11.glVertex2d(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
            GL11.glTexCoord2d(1, 1);
            GL11.glVertex2d(scaledResolution.getScaledWidth(), 0);
        }
        GL11.glEnd();
        GL20.glUseProgram(0);
    }

    public void renderFR(float x, float y, float width, float height) {
        if (MC.gameSettings.ofFastRender) return;
        glBegin(GL_QUADS);
        glTexCoord2f(0, 1);
        glVertex2f(x, y);
        glTexCoord2f(0, 0);
        glVertex2f(x, height);
        glTexCoord2f(1, 0);
        glVertex2f(width, height);
        glTexCoord2f(1, 1);
        glVertex2f(width, y);
        glEnd();
    }

    public void initShader() {
        glUseProgram(this.programID);
    }

    public void deleteShader() {
        glUseProgram(0);
    }

    public int getUniform(String name) {
        return glGetUniformLocation(this.programID, name);
    }

    public void setUniformf(String name, float... args) {
        int loc = glGetUniformLocation(programID, name);
        if (args.length > 1) {
            if (args.length > 2) {
                if (args.length > 3) glUniform4f(loc, args[0], args[1], args[2], args[3]);
                else glUniform3f(loc, args[0], args[1], args[2]);
            } else glUniform2f(loc, args[0], args[1]);
        } else glUniform1f(loc, args[0]);
    }


    public void setUniformi(String name, int... args) {
        int loc = glGetUniformLocation(programID, name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }

    @Override
    public String toString() {
        return "ShaderProgram{" + "programID=" + programID + ", vertexName='" + vertexName + '\'' + ", fragmentName='" + fragmentName + '\'' + '}';
    }
}