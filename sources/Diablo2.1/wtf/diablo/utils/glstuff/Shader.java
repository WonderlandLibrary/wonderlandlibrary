package wtf.diablo.utils.glstuff;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public abstract class Shader {
    //Gl Program
    private final int glProgram;

    public final Map<String, Integer> uniformMap = new HashMap<>();

    public Shader(String vertexSrc, String fragSrc) {
        glProgram = glCreateProgram();

        //Attach Shaders
        glAttachShader(glProgram, makeShader(vertexSrc, GL_VERTEX_SHADER));
        glAttachShader(glProgram, makeShader(fragSrc, GL_FRAGMENT_SHADER));
        glLinkProgram(glProgram);

        this.addUniforms();
    }

    public void updateProgram() {
        glUseProgram(glProgram);
        this.updateUniforms();
    }

    public abstract void addUniforms();

    private int makeShader(String shaderSrc, int shaderType) {
        int newShader = glCreateShader(shaderType);

        glShaderSource(newShader, shaderSrc);

        glCompileShader(newShader);

        return newShader;
    }

    public void setupUniform(String uniform) {
        uniformMap.put(uniform, glGetUniformLocation(glProgram, uniform));
    }

    public int getUniform(String uniform) {
        if(uniformMap.isEmpty())
            System.out.println("Crashed due to uniformMap moment :(");
        return uniformMap.get(uniform);
    }

    public abstract void updateUniforms();
}
