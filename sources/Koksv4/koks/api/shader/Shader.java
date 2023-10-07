package koks.api.shader;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */
public class Shader {

    @Getter
    private final int programId, vertexShaderId, fragmentShaderId;

    public Shader() {
        final Info info = getClass().getAnnotation(Info.class);
        fragmentShaderId = loadShader(GL_FRAGMENT_SHADER, "fragment/" + info.fragment());
        vertexShaderId = loadShader(GL_VERTEX_SHADER, "vertex/" + info.vertex());
        programId = glCreateProgram();

        glAttachShader(programId, fragmentShaderId);
        glAttachShader(programId, vertexShaderId);

        glLinkProgram(programId);
        glValidateProgram(programId);
    }

    public int getUniform(String uniformName) {
        return glGetUniformLocation(getProgramId(), uniformName);
    }

    public void use() {
        glUseProgram(programId);
    }

    public void unUse() {
        glUseProgram(0);
    }

    public void cleanUp() {
        unUse();
        glDetachShader(programId, vertexShaderId);
        glDetachShader(programId, fragmentShaderId);

        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
        glDeleteProgram(programId);
    }

    public int loadShader(int type, String name) {
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation("koks/shader/" +  name + ".glsl"))));
            while (reader.ready())
                builder.append(reader.readLine()).append("\n");
            reader.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int id = glCreateShader(type);
        glShaderSource(id, builder);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Failed to compile shader: " + glGetShaderInfoLog(id, Integer.MAX_VALUE));

        return id;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Info {
        String vertex() default "vertex";
        String fragment();
    }
}
