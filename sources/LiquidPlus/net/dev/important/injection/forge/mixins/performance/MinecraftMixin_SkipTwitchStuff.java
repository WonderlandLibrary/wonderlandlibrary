/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.stream.IStream
 */
package net.dev.important.injection.forge.mixins.performance;

import net.minecraft.client.Minecraft;
import net.minecraft.client.stream.IStream;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={Minecraft.class})
public class MinecraftMixin_SkipTwitchStuff {
    @Redirect(method={"runGameLoop"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/stream/IStream;func_152935_j()V"))
    private void patcher$skipTwitchCode1(IStream instance) {
    }

    @Redirect(method={"runGameLoop"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/stream/IStream;func_152922_k()V"))
    private void patcher$skipTwitchCode2(IStream instance) {
    }
}

