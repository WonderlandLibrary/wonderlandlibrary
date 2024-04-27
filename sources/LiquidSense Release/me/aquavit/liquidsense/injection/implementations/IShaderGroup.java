package me.aquavit.liquidsense.injection.implementations;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;

import java.util.List;

public interface IShaderGroup {

    Framebuffer mainFramebuffer();

    List<Shader> getShaders();
}
