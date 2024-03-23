package xyz.northclient.util.shader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;

public enum GLSL {
    COLORS("/assets/minecraft/tecness/shader.glsl");


    private int programId;
    private int timeUniform;
    private int mouseUniform;
    private int resolutionUniform;

    GLSL(String fragmentShaderLocation) {
        try {
            int program = glCreateProgram();

            //glAttachShader(program, createShader(GLSLSandboxShader.class.getResourceAsStream("/passthrough.vsh"), GL_VERTEX_SHADER));
            glAttachShader(program, createShader(GLSL.class.getResourceAsStream(fragmentShaderLocation), GL_FRAGMENT_SHADER));

            glLinkProgram(program);

            int linked = glGetProgrami(program, GL_LINK_STATUS);


            // If linking failed
            if (linked == 0) {
                System.err.println(glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH)));

                throw new IllegalStateException("Shader failed to link");
            }

            this.programId = program;

            // Setup uniforms
            glUseProgram(program);

            this.timeUniform = glGetUniformLocation(program, "time");
            this.mouseUniform = glGetUniformLocation(program, "mouse");
            this.resolutionUniform = glGetUniformLocation(program, "resolution");

            glUseProgram(0);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void useShader(int width, int height, float mouseX, float mouseY, float time) {
        glUseProgram(this.programId);

        glUniform2f(this.resolutionUniform, width, height);
        glUniform2f(this.mouseUniform, mouseX / width, 1.0f - mouseY / height);
        glUniform1f(this.timeUniform, time);
    }

    private int createShader(InputStream inputStream, int shaderType) throws IOException {
        int shader = glCreateShader(shaderType);

        glShaderSource(shader, readStreamToString(inputStream));

        glCompileShader(shader);

        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);

        // If compilation failed
        if (compiled == 0) {
            System.err.println(glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)));

            throw new IllegalStateException("Failed to compile shader");
        }

        return shader;
    }

    private String readStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[512];

        int read;

        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
        }

        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }
}