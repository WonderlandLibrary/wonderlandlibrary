package intent.AquaDev.aqua.utils.shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class ShaderProgram {
    private final String vertexName;
    private final String fragmentName;
    private final int vertexStage;
    private final int fragmentStage;
    private int programID;

    public ShaderProgram(String vertexName, String fragmentName) {
        this.vertexName = vertexName;
        this.fragmentName = fragmentName;
        this.vertexStage = ShaderProgram.createShaderStage(35633, vertexName);
        this.fragmentStage = ShaderProgram.createShaderStage(35632, fragmentName);
        if (this.vertexStage != -1 && this.fragmentStage != -1) {
            this.programID = GL20.glCreateProgram();
            GL20.glAttachShader((int)this.programID, (int)this.vertexStage);
            GL20.glAttachShader((int)this.programID, (int)this.fragmentStage);
            GL20.glLinkProgram((int)this.programID);
        }
    }

    private static int createShaderStage(int shaderStage, String shaderName) {
        boolean compiled;
        int stageId = GL20.glCreateShader((int)shaderStage);
        GL20.glShaderSource((int)stageId, (CharSequence)ShaderProgram.readShader(shaderName));
        GL20.glCompileShader((int)stageId);
        boolean bl = compiled = GL20.glGetShaderi((int)stageId, (int)35713) == 1;
        if (!compiled) {
            String shaderLog = GL20.glGetShaderInfoLog((int)stageId, (int)2048);
            System.out.printf("Failed to compile shader %s (stage: %s); Message\n%s%n", new Object[]{shaderName, GL11.glGetString((int)shaderStage), shaderLog});
            return -1;
        }
        return stageId;
    }

    private static String readShader(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line;
            InputStream inputStream = ShaderProgram.class.getResourceAsStream(String.format((String)"/shaders/%s", (Object[])new Object[]{fileName}));
            assert (inputStream != null);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader((Reader)inputStreamReader);
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public void deleteShaderProgram() {
        GL20.glUseProgram((int)0);
        GL20.glDeleteProgram((int)this.programID);
        GL20.glDeleteShader((int)this.vertexStage);
        GL20.glDeleteShader((int)this.fragmentStage);
    }

    public void init() {
        GL20.glUseProgram((int)this.programID);
    }

    public void doRenderPass(float width, float height) {
        GL11.glBegin((int)7);
        GL11.glTexCoord2f((float)0.0f, (float)1.0f);
        GL11.glVertex2d((double)0.0, (double)0.0);
        GL11.glTexCoord2f((float)0.0f, (float)0.0f);
        GL11.glVertex2d((double)0.0, (double)height);
        GL11.glTexCoord2f((float)1.0f, (float)0.0f);
        GL11.glVertex2d((double)width, (double)height);
        GL11.glTexCoord2f((float)1.0f, (float)1.0f);
        GL11.glVertex2d((double)width, (double)0.0);
        GL11.glEnd();
    }

    public void uninit() {
        GL20.glUseProgram((int)0);
    }

    public int uniform(String name) {
        return GL20.glGetUniformLocation((int)this.programID, (CharSequence)name);
    }
}
