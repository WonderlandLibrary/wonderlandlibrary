/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.EntityRenderer
 *  net.minecraft.entity.Entity
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.dev.important.modules.module.modules.misc.Patcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={Minecraft.class})
public class MinecraftMixin_PersistentShaders {
    @Redirect(method={"runTick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/EntityRenderer;loadEntityShader(Lnet/minecraft/entity/Entity;)V"))
    private void patcher$keepShadersOnPerspectiveChange(EntityRenderer entityRenderer, Entity entityIn) {
        if (!((Boolean)Patcher.keepShadersOnPerspectiveChange.get()).booleanValue()) {
            entityRenderer.func_175066_a(entityIn);
        }
    }
}

