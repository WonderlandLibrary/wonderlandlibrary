/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={Minecraft.class})
public interface MinecraftAccessor {
    @Accessor(value="fullscreen")
    public void setFullScreen(boolean var1);

    @Accessor
    public int getTempDisplayWidth();

    @Accessor
    public int getTempDisplayHeight();

    @Invoker
    public void callUpdateFramebufferSize();
}

