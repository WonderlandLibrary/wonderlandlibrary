package dev.zprestige.prestige.client.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.zprestige.prestige.api.mixin.IShaderProgram;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

//#SKIDDED
public class GlProgram {
    private static final List<Pair<Function<ResourceFactory, ShaderProgram>, Consumer<ShaderProgram>>> REGISTERED_PROGRAMS = new ArrayList<>();

    public ShaderProgram backingProgram;

    public GlProgram(Identifier id, VertexFormat vertexFormat) {
        REGISTERED_PROGRAMS.add(new Pair<>(resourceFactory -> {
            try {
                return new Shader(resourceFactory, id.toString(), vertexFormat);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialized shader program", e);
            }
        }, program -> {
            backingProgram = program;
            setup();
        }));
    }

    public void use() {
        RenderSystem.setShader(() -> backingProgram);
    }

    protected void setup() {}

    protected GlUniform findUniform(String name) {
        return (GlUniform) ((IShaderProgram) backingProgram).getUniformsHook().get(name);
    }

    public static void forEachProgram(Consumer<Pair<Function<ResourceFactory, ShaderProgram>, Consumer<ShaderProgram>>> loader) {
        REGISTERED_PROGRAMS.forEach(loader);
    }

    public static class Shader extends ShaderProgram {
        private Shader(ResourceFactory factory, String name, VertexFormat format) throws IOException {
            super(factory, name, format);
        }
    }
}
