package xyz.cucumber.base.utils.render;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader {
    private final int programID;

    private final int program;

    public Shader(String fragmentShaderLoc) {
        program = GL20.glCreateProgram();
        String vertexLoc;
        vertexLoc = "#version 120\n" +
                "\n" +
                "void main() {\n" +
                "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
                "    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;\n" +
                "}";
        GL20.glAttachShader(this.program, createShader(fragmentShaderLoc, GL20.GL_FRAGMENT_SHADER));
        GL20.glAttachShader(this.program, createShader(vertexLoc, GL20.GL_VERTEX_SHADER));
        GL20.glLinkProgram(program);
        this.programID = program;
    }

    public void startProgram() {
    	GL20.glUseProgram(programID);
    }

    public static void uniformFB(final int programId, final String name, final FloatBuffer floatBuffer) {
        GL20.glUniform1(getLocation(programId, name), floatBuffer);
    }

    public static void uniform1i(final int programId, final String name, final int i) {
        GL20.glUniform1i(getLocation(programId, name), i);
    }

    public static void uniform2i(final int programId, final String name, final int i, final int j) {
        GL20.glUniform2i(getLocation(programId, name), i, j);
    }

    public static void uniform1f(final int programId, final String name, final float f) {
        GL20.glUniform1f(getLocation(programId, name), f);
    }

    public static void uniform2f(final int programId, final String name, final float f, final float g) {
        GL20.glUniform2f(getLocation(programId, name), f, g);
    }

    public static void uniform3f(final int programId, final String name, final float f, final float g, final float h) {
        GL20.glUniform3f(getLocation(programId, name), f, g, h);
    }

    public static void uniform4f(final int programId, final String name, final float f, final float g, final float h, final float i) {
        GL20.glUniform4f(getLocation(programId, name), f, g, h, i);
    }

    private static int getLocation(final int programId, final String name) {
        return GL20.glGetUniformLocation(programId, name);
    }
    
    public int getProgramID() {
        return programID;
    }

    public void stopProgram() {
    	GL20.glUseProgram(0);
    }
    public void renderShader(double x,double y , double width, double height) {
    	GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2d(x, y);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2d(x, y+height);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2d(x+width, y+height);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2d(x+width, y);
        GL11.glEnd();
    }
    

    public int getUniformLoc(String uniform) {
        return GL20.glGetUniformLocation(programID, uniform);
    }

    private int createShader(String oskar, int shaderType) {
    		final int shader;
            shader = GL20.glCreateShader(shaderType);
            GL20.glShaderSource(shader, oskar);
            GL20.glCompileShader(shader);
            System.out.println(GL20.glGetShaderInfoLog(shader, 1024));
            return shader;
        }
}
