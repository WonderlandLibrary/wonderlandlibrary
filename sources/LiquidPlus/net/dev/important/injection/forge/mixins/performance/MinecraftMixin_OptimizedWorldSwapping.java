/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package net.dev.important.injection.forge.mixins.performance;

import net.dev.important.modules.module.modules.misc.Patcher;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={Minecraft.class})
public class MinecraftMixin_OptimizedWorldSwapping {
    @Redirect(method={"loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V"}, at=@At(value="INVOKE", target="Ljava/lang/System;gc()V"))
    private void patcher$optimizedWorldSwapping() {
        if (!((Boolean)Patcher.optimizedWorldSwapping.get()).booleanValue()) {
            System.gc();
        }
    }
}

