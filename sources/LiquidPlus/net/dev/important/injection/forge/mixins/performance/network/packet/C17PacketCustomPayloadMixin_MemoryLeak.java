/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.client.C17PacketCustomPayload
 */
package net.dev.important.injection.forge.mixins.performance.network.packet;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={C17PacketCustomPayload.class})
public class C17PacketCustomPayloadMixin_MemoryLeak {
    @Shadow
    private PacketBuffer field_149561_c;

    @Inject(method={"processPacket(Lnet/minecraft/network/play/INetHandlerPlayServer;)V"}, at={@At(value="TAIL")})
    private void patcher$releaseData(INetHandlerPlayServer handler, CallbackInfo ci) {
        if (this.field_149561_c != null) {
            this.field_149561_c.release();
        }
    }
}

