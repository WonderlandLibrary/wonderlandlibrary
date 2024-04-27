package net.ccbluex.liquidbounce.injection.forge.mixins.client;


import java.util.List;

import net.ccbluex.liquidbounce.injection.implementations.IShaderGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.google.common.collect.Lists;

import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;

@Mixin(ShaderGroup.class)
public class MixinShaderGroup implements IShaderGroup {
    @Shadow
    protected Framebuffer mainFramebuffer;

    @Shadow
    protected List<Shader> listShaders = Lists.<Shader>newArrayList();

    @Override
    public Framebuffer mainFramebuffer() {
        return mainFramebuffer;
    }

    @Override
    public List<Shader> getShaders() {
        return listShaders;
    }
}
