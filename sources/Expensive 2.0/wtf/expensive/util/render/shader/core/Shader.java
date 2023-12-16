package wtf.expensive.util.render.shader.core;

import lombok.SneakyThrows;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;
import wtf.expensive.util.misc.FileUtil;
import wtf.expensive.util.render.RenderUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.lwjgl.opengl.GL20.*;
import static wtf.expensive.util.IMinecraft.mc;

public class Shader {

    private int program;

    @SneakyThrows
    public Shader(String path) {
        init(FileUtil.readInputStream(mc.getResourceManager().getResource(new ResourceLocation("expensive/shader/impl/" + path + ".frag")).getInputStream()));
    }

    @SneakyThrows
    private void init(String source) {
        program = ARBShaderObjects.glCreateProgramObjectARB();
        int fragmentShaderID = createShader(source, GL_FRAGMENT_SHADER);

        ARBShaderObjects.glAttachObjectARB(program, fragmentShaderID);
        ARBShaderObjects.glAttachObjectARB(program, createShader(FileUtil.readInputStream(mc.getResourceManager().getResource(new ResourceLocation("expensive/shader/vertex.vert")).getInputStream()), GL_VERTEX_SHADER));
        ARBShaderObjects.glLinkProgramARB(program);
    }

    public void start() {
        ARBShaderObjects.glUseProgramObjectARB(program);
    }

    public void stop() {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    public void setUniformf(String name, float... value) {
        int var3 = ARBShaderObjects.glGetUniformLocationARB(program, name);
        switch (value.length) {
            case 1 -> ARBShaderObjects.glUniform1fARB(var3, value[0]);
            case 2 -> ARBShaderObjects.glUniform2fARB(var3,value[0], value[1]);
            case 3 -> ARBShaderObjects.glUniform3fARB(var3,value[0], value[1], value[2]);
            case 4 -> ARBShaderObjects.glUniform4fARB(var3,value[0], value[1], value[2], value[3]);
        }
    }

    public void setUniformi(String name, int... value) {
        int var3 = ARBShaderObjects.glGetUniformLocationARB(program, name);
        switch (value.length) {
            case 1 -> ARBShaderObjects.glUniform1iARB(var3, value[0]);
            case 2 -> ARBShaderObjects.glUniform2iARB(var3,value[0], value[1]);
            case 3 -> ARBShaderObjects.glUniform3iARB(var3,value[0], value[1], value[2]);
            case 4 -> ARBShaderObjects.glUniform4iARB(var3,value[0], value[1], value[2], value[3]);
        }
    }

    public void drawQuads(final float x,
                          final float y,
                          final float width,
                          final float height) {

        RenderUtil.Render2D.quadsBegin(x, y, width, height, GL_POLYGON);
    }


    private int createShader(String text, int shaderType) {
        int shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
        ARBShaderObjects.glShaderSourceARB(shader, FileUtil.readInputStream(new ByteArrayInputStream(text.getBytes())));
        ARBShaderObjects.glCompileShaderARB(shader);
        if (GL20.glGetShaderi(shader, 35713) == 0) {
            System.out.println(GL20.glGetShaderInfoLog(shader, 4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
        }
        return shader;
    }

}
