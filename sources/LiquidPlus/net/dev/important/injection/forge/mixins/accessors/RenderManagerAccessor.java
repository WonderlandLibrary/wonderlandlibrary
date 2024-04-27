/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.renderer.entity.RenderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={RenderManager.class})
public interface RenderManagerAccessor {
    @Accessor
    public double getRenderPosX();

    @Accessor
    public double getRenderPosY();

    @Accessor
    public double getRenderPosZ();
}

