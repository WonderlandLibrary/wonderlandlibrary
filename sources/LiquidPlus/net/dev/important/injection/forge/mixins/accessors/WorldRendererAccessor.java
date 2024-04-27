/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.WorldRenderer
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.renderer.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={WorldRenderer.class})
public interface WorldRendererAccessor {
    @Accessor(value="isDrawing")
    public boolean isDrawing();
}

