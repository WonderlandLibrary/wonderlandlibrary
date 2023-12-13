package best.azura.client.util.render;

import best.azura.client.impl.module.impl.other.ClientModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderUtil {

    private int program, vertexShader, fragmentShader;
    private boolean compiledCorrectly = true;
    private long initTime;

    public ShaderUtil(String vertexFile, String fragmentFile) {
        if(OpenGlHelper.shadersSupported) {
            this.program = glCreateProgram();
            this.fragmentShader = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
            this.vertexShader = loadShader(vertexFile, GL_VERTEX_SHADER);
            glAttachShader(program, fragmentShader);
            glAttachShader(program, vertexShader);
            glLinkProgram(program);
            if(glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
                System.err.println(glGetProgramInfoLog(program, 5000));
                compiledCorrectly = false;
            }
            glValidateProgram(program);
            if(glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE) {
                System.err.println(glGetProgramInfoLog(program, 5000));
                compiledCorrectly = false;
            }
            if (compiledCorrectly) {
                initTime = System.currentTimeMillis();
            }
        }
    }

    public ShaderUtil(InputStream vertexFile, InputStream fragmentFile) {
        if(OpenGlHelper.shadersSupported) {
            this.program = glCreateProgram();
            this.fragmentShader = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
            this.vertexShader = loadShader(vertexFile, GL_VERTEX_SHADER);
            glAttachShader(program, fragmentShader);
            glAttachShader(program, vertexShader);
            glLinkProgram(program);
            if(glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE) {
                System.err.println(glGetProgramInfoLog(program, 5000));
                compiledCorrectly = false;
            }
            glValidateProgram(program);
            if(glGetProgrami(program, GL_VALIDATE_STATUS) != GL_TRUE) {
                System.err.println(glGetProgramInfoLog(program, 5000));
                compiledCorrectly = false;
            }
            if (compiledCorrectly) {
                initTime = System.currentTimeMillis();
            }
        }
    }

    public void bind() {
        glUseProgram(program);
    }


    public void setUniform_f(String name, float... args) {
        int loc = glGetUniformLocation(program, name);
        if (args.length > 1) {
            if (args.length > 2) {
                if(args.length > 3) glUniform4f(loc, args[0], args[1], args[2], args[3]);
                else glUniform3f(loc, args[0], args[1], args[2]);
            }else glUniform2f(loc, args[0], args[1]);
        } else glUniform1f(loc, args[0]);
    }

    public void setUniform_i(String name, int... args) {
        int loc = glGetUniformLocation(program, name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }

    public void render(Minecraft mc, int mouseX, int mouseY) {
        if (!compiledCorrectly || !OpenGlHelper.shadersSupported) return;
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        bind();
        setupUniforms(mc, mouseX, mouseY);
        render(mc);
        unbind();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
    }

    public void render(Minecraft mc) {
        glBegin(GL_QUADS);
        glVertex2d(0, 0);
        glVertex2d(mc.displayWidth, 0);
        glVertex2d(mc.displayWidth, mc.displayHeight);
        glVertex2d(0, mc.displayHeight);
        glEnd();
    }

    public void setupUniforms(Minecraft mc, int mouseX, int mouseY) {
        glUniform2f(glGetUniformLocation(getProgram(), "u_resolution"), mc.displayWidth, mc.displayHeight);
        glUniform1f(glGetUniformLocation(getProgram(), "u_time"), (System.currentTimeMillis() - initTime) / 1000.0f);
        glUniform2f(glGetUniformLocation(getProgram(), "u_mouse"), mouseX, mouseY);
        final Color color1 = ClientModule.shaderColor1.getObject().getColor(), color2 = ClientModule.shaderColor2.getObject().getColor();
        glUniform3f(glGetUniformLocation(getProgram(), "u_color_1"), (color1.getRed() / 255f), (color1.getGreen() / 255f), (color1.getBlue() / 255f));
        glUniform3f(glGetUniformLocation(getProgram(), "u_color_2"), (color2.getRed() / 255f), (color2.getGreen() / 255f), (color2.getBlue() / 255f));
    }

    public void unbind() {
        glUseProgram(0);
    }

    private int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ShaderUtil.class.getClassLoader().getResourceAsStream(file))));
            String line;
            while ((line = reader.readLine()) != null) shaderSource.append(line).append("\n");
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource.toString());
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) != GL_TRUE) {
            System.err.println("Shader wasn't compiled: " + glGetShaderInfoLog(shaderID, 5000));
            compiledCorrectly = false;
        }

        return shaderID;
    }

    private int loadShader(InputStream file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));
            String line;
            while ((line = reader.readLine()) != null) shaderSource.append(line).append("\n");
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, shaderSource.toString());
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) != GL_TRUE) {
            System.err.println("Shader wasn't compiled: " + glGetShaderInfoLog(shaderID, 5000));
            compiledCorrectly = false;
        }

        return shaderID;
    }

    public int getFragmentShader() {
        return fragmentShader;
    }

    public int getVertexShader() {
        return vertexShader;
    }

    public int getProgram() {
        return program;
    }

    public boolean isCompiledCorrectly() {
        return compiledCorrectly;
    }
}
