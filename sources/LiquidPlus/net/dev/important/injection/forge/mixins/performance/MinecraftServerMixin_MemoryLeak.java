/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 *  net.minecraft.server.MinecraftServer
 */
package net.dev.important.injection.forge.mixins.performance;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={MinecraftServer.class})
public class MinecraftServerMixin_MemoryLeak {
    @ModifyVariable(method={"addFaviconToStatusResponse"}, at=@At(value="INVOKE", target="Lnet/minecraft/network/ServerStatusResponse;setFavicon(Ljava/lang/String;)V", shift=At.Shift.AFTER), ordinal=1)
    private ByteBuf patcher$releaseByteBuf(ByteBuf buf1) {
        buf1.release();
        return buf1;
    }
}

