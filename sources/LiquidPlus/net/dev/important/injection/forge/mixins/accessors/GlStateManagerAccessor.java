/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$Color
 *  net.minecraft.client.renderer.GlStateManager$TextureState
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.renderer.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={GlStateManager.class})
public interface GlStateManagerAccessor {
    @Accessor
    public static GlStateManager.Color getColorState() {
        throw new UnsupportedOperationException("Mixin failed to inject!");
    }

    @Accessor
    public static GlStateManager.TextureState[] getTextureState() {
        throw new UnsupportedOperationException("Mixin failed to inject!");
    }

    @Accessor
    public static int getActiveTextureUnit() {
        throw new UnsupportedOperationException("Mixin failed to inject!");
    }
}

